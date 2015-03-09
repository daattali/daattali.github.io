import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

/**
 * Dean Attali<br>
 * March 18, 2006<br><br>
 *
 * This class handles all the networking stuff
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class NetworkScrabble extends JFrame implements ActionListener, Runnable
{
	private Scrabble parent;				// A reference to the main class
	private ServerSocket server = null;		// The server
	private Socket client = null;			// The client
	private BufferedReader reader = null;	// Input
	private PrintWriter writer = null;		// Output
	private boolean host;					// Whether this player is the host or not
	private boolean connected;				// Whether there is a connection or not
	private String status;					// Either "Server" or "Client"

	// The GUI
	private JTextField IPField, portField;
	private JLabel helpLabel;
	private JRadioButton joinButton, hostButton;
	private JButton startButton;

	/**
	 * Creates a new frame and the GUI
	 * @param p	The main class
	 */
	public NetworkScrabble(Scrabble p)
	{
		super("Scrabble");

		parent = p;
		getContentPane().setLayout(null);
		setResizable(false);

		hostButton = new JRadioButton("Host", true);
		hostButton.setMnemonic(KeyEvent.VK_H);
		getContentPane().add(hostButton);
		hostButton.setBounds(10, 10, 60, 20);

		joinButton = new JRadioButton("Join");
		joinButton.setMnemonic(KeyEvent.VK_J);
		getContentPane().add(joinButton);
		joinButton.setBounds(10, 40, 60, 20);

		ButtonGroup group = new ButtonGroup();
		group.add(hostButton);
		group.add(joinButton);

		hostButton.addActionListener(this);
		joinButton.addActionListener(this);

		JLabel	IPLabel = new JLabel("IP"),
			   	portLabel = new JLabel("Port");
		IPLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		portLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(IPLabel);
		getContentPane().add(portLabel);
		IPLabel.setBounds(85, 10, 20, 20);
		portLabel.setBounds(75, 40, 30, 20);

		IPField = new JTextField();
		portField = new JTextField();
		getContentPane().add(IPField);
		getContentPane().add(portField);
		IPField.setBounds(115, 10, 100, 20);
		portField.setBounds(115, 40, 40, 20);

		helpLabel = new JLabel();
		getContentPane().add(helpLabel);
		helpLabel.setBounds(0, 65, 230, 30);
		helpLabel.setHorizontalAlignment(SwingConstants.CENTER);

		startButton = new JButton("Start");
		startButton.addActionListener(this);
		getContentPane().add(startButton);
		startButton.setBounds(30, 100, 75, 20);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		cancelButton.setBounds(120, 100, 75, 20);

		changeToHost();

		setPreferredSize(new Dimension(230, 190));
		setVisible(true);
		setLocation(100,100);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
	}

	/**
	 * Sets this player to be the server
	 */
	private void setServer()
	{
		status = "Server";
		final Thread t = new Thread(this); // Create a thread that will constantly listen for inputs

		try
		{
			// Create the server
			int portNum = Integer.parseInt(portField.getText());
			server = new ServerSocket(portNum);

			// Wait for a connection in a separate thread, so that the GUI won't freeze
			new Thread(){
				public void run(){
					try
					{
						helpLabel.setText("Waiting for client to join...");
						client = server.accept();
						reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
						writer = new PrintWriter(client.getOutputStream(), true); // Auto flush (send message)
						send("Welcome"); // Send the client a message, to show it that we're ready
						connected = true;
						setEverything(false); // Disable all the GUI
						helpLabel.setText("Connected to "+client.getInetAddress().getHostAddress());
						t.start();
						LettersBag.setDefaultFrequency();
						parent.newGame(2, true, true);
						dispose(false);
					}
					catch(Exception ex)
					{
						closeConnection("Error connecting to client");
					}
				}
			}.start();
		}
		catch(BindException ex)
		{
			closeConnection("Error with the port");
		}
		catch(Exception ex)
		{
			closeConnection("Please enter a valid port number");
		}
	}

	/**
	 * Sets this player to be the client
	 */
	private void setClient()
	{
		status = "Client";
		final Thread t = new Thread(this); // Create a thread that will constantly listen for inputs

		try
		{
			// Get the IP and port of the server
			final int serverPort = Integer.parseInt(portField.getText());
			final String serverIP = IPField.getText();

			// Try to connect to the server in a separate thread to not disturb
			// the event-dispatcher thread
			new Thread(){
				public void run(){
					try
					{
						helpLabel.setText("Connecting...");
						InetSocketAddress servSockAddr = new InetSocketAddress(serverIP, serverPort);
						client = new Socket(); //Create unbound socket
						client.connect(servSockAddr, 5000); // Wait 5 seconds for connection
						reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
						writer = new PrintWriter(client.getOutputStream(), true); // Auto flush (send message)
						connected = true;
						setEverything(false);
						helpLabel.setText("Connected successfully");
						t.start();
						LettersBag.setDefaultFrequency();
						parent.setNetworkPlay(true);
						parent.newGame(2, true, false);
						dispose(false);
					}
					catch(UnknownHostException ex) {
						closeConnection("Bad IP address or port number");
					}
					catch(SocketTimeoutException ex){
						closeConnection("Connection timed out");
					}
					catch(Exception ex)
					{
						closeConnection("Error connecting to client");
					}
				}
			}.start();
		}
		catch(Exception ex){
			closeConnection("Please type a valid port number");
		}
	}

	/**
	 * Change all the GUI to host
	 */
	private void changeToHost()
	{
		IPField.setEnabled(false);
		try
		{
			IPField.setText(InetAddress.getLocalHost().getHostAddress());
		}catch(UnknownHostException ex) {helpLabel.setText("Couldn't get local IP");}
		portField.setText("20715");
		host = true;
		helpLabel.setText("");
		portField.requestFocus();
	}

	/**
	 * Change all the GUI to join
	 */
	private void changeToJoin()
	{
		IPField.setEnabled(true);
		IPField.setText("");
		IPField.requestFocus();
		portField.setText("20715");
		host = false;
		helpLabel.setText("");
	}

	/**
	 * Enables/disables all the GUI
	 */
	private void setEverything(boolean flag)
	{
			if(!host)IPField.setEnabled(flag);
			portField.setEnabled(flag);
			joinButton.setEnabled(flag);
			hostButton.setEnabled(flag);
			startButton.setEnabled(flag);
	}

	public void actionPerformed(ActionEvent e)
	{
		String str = e.getActionCommand();

		// Cancel
		// Close the connection is there is a connectino attempt,
		// close the frame otherwise
		if(str.equals("Cancel"))
		{
			if(startButton.isEnabled())
			{
				dispose();
			}
			else
			{
				closeConnection();
			}
		}
		// Host radio button
		else if(str.equals("Host"))
		{
			if(IPField.isEnabled()) changeToHost();
		}
		// Join radio button
		else if(str.equals("Join"))
		{
			if(!IPField.isEnabled()) changeToJoin();
		}
		// Start - create a server/try to connect to the server
		else if(str.equals("Start"))
		{
			setEverything(false);
			if(host) 	setServer();
			else 		setClient();
		}
	}

	/**
	 * Send the other socket a string
	 * @param line	The string to send
	 */
	public void send(String line)
	{
		if(writer != null) writer.println(line);
	}

	/**
	 * Constantly listen for inputs
	 */
	public void run()
		{
			String line;
			while(connected) //While there is a connection
			{
				try{
					if((line = reader.readLine()) != null)
					{
						Debug.println(status+" received: "+line);

						if(line.equals("close"))
						{
							closeConnection("Other person exited", true);
						}
						parent.getGame().process(line);
					}
				}catch(IOException ex){}
			}
	}

	/**
	 * Close the connection but not the frame with no reason
	 */
	public void closeConnection()
	{
		closeConnection("Connection Lost", false);
	}

	/**
	 * Close the connection but not the frame with a reason
	 * @param str	The reason why the connection was lost
	 */
	public void closeConnection(String str)
	{
		closeConnection(str, false);
	}

	/**
	 * Close the connection and maybe the frame too
	 * @param str	The reason why the connection was lost
	 * @param close	Whether to close the frame or not
	 */
	public void closeConnection(String str, boolean close)
	{
		Debug.println("Closing connection: "+str);
		connected = false;

		if(client != null && writer != null) send("close");

		try{
			if(reader != null){
				reader = null;
			}
			if(writer != null){
				writer.close(); writer = null;
			}
			if(client != null){
				client.close();
			}
			if(server != null){
				server.close(); server = null;
			}
		}
		catch(Exception ex){}

		Debug.println("Connection lost");
		helpLabel.setText(str);
		setEverything(true);
		portField.requestFocus();
		if(close) parent.getGame().endGame();
	}

	/**
	 * Close the frame
	 * @param close Whether to close the connection or not
	 */
	public void dispose(boolean close)
	{
		super.dispose();
		if(close) closeConnection();
	}

	/**
	 * Close the frame and the connection
	 */
	public void dispose(){
		dispose(true);
	}

	/**
	 * @return Returns whether this player is the host or not
	 */
	public boolean isHost() {
		return host;
	}
}