import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dean Attali<br>
 * March 10, 2006<br><br>
 *
 * The main class that starts a new visible game of Scrabble.<br>
 * This class sets up the GUI and created instances of all the needed classes.<br>
 * One nice thing is that this will look good on ALL screen resolutions.<br>
 * It took me almost an hour to change all the GUI so that it fits in proportion
 * to the screen and the letters size changes accordingly<br><br>
 *
 * Tests:<br>
 * 	- If the first play is not in the middle or less than 2 letters, it's invalid<br>
 *  - If any player places letters not in a line or not adjacent to existing ones, it's invalid<br>
 *  - If a player changes his letters, the changed letters go back to the letters bag<br>
 *  - If a play is challenged, the letters go back to the player and his score is lost<br>
 *  - If two or more players tie, it says exactly which players tied<br>
 *  - If you select your own letter frequencies, the game will have what you selected<br>
 *  - If you close the window while playing multiplayer, it will inform the other player<br>
 *  - If you place a blank letter on the board and put it back in the pile, it's blank again<br>
 *  - If there are no more letters, the game is over<br>
 *  - If all the players passed their turns, they are asked if they want to end the game<br>
 *  - If letters are placed on special squares, they get the special points<br><br>
 *
 *  To change this to an applet/application:<br>
 *  				Applet								Application<br>
 *  			   ========						   	   =============<br>
 *  			extends JApplet 					extends JFrame<br>
 * 				comment main method					uncomment main method<br>
 * 				uncomment start method				comment start method<br>
 * 				comment dispose method				uncomment dispose method<br>
 * 				uncomment stop method				comment stop method<br>
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class Scrabble extends JApplet implements ActionListener
{
	// The GUI
	private JTextArea historyArea, helpArea, chatArea;
	private JButton submitButton, changeButton, challengeButton, sendButton;
	private JTextField chatField;
	private JScrollPane chatScrollPane;
	private JLabel lettersLabel;

	// References to instances of the classes I need
	private BoardDisplay boardPanel;
	private LettersPanel lettersPanel;
	private ScorePanel scorePanel;
	private BoundedEnv[] lettersEnv;
	private GameEngine game;
	private BoundedEnv boardEnv;
	private NetworkScrabble network;

	// Some variables that hold game information
	private boolean networkPlay; 		// True if the game is played on more than one computer
	private int turn; 					// Whose turn it is
	private int numPlayers;				// The number of players
	private boolean changing; 			// True if the current player is changing his letters
	private boolean playing; 			// True when a game is in place
	public final static int SIZE = getBestSize(); 	// The size of the lettes (in pixels)

	/**
 	 * Start the game
 	 */
	/*public static void main(String args[])
	{
        JFrame frame = new Scrabble();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height - 30));
        frame.setTitle("Scrabble! By Dean Attali, March 2006");
        frame.pack();
        frame.setVisible(true);
	}*/

	public void start()
	{
        resize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height - 45));
		showStatus("Scrabble! By Dean Attali, March 2006");
	}

	/**
 	 * Close the game and inform the other player if necessary
 	 */
	/*public void dispose()
	{
		System.out.println("BYE BYE!");
		if(networkPlay)
		{
			network.send("close");
		}
		super.dispose();
		System.exit(0);
	}*/

	public void stop()
	{
		System.out.println("BYE BYE!");
		if(networkPlay)
		{
			network.send("close");
		}
		super.stop();
	}

	/**
	 * Find the best size of the letters, to look best on all screen resolutions
	 * @return The size of the letters should be, in pixels
	 */
	public static int getBestSize()
	{
		return Math.min((Toolkit.getDefaultToolkit().getScreenSize().height - 130) / 16, (Toolkit.getDefaultToolkit().getScreenSize().width - 420) / 15);
	}

	/**
	 * Set up the game
	 */
	public Scrabble()
	{
		Debug.turnOff();
		Debug.println("Starting up the game");

		// Create all the classes I need to start
		lettersPanel=new LettersPanel(this);
		boardPanel=new BoardDisplay(this);
		scorePanel=new ScorePanel(this);
		game=new GameEngine(this);

		// Set up the GUI
		getContentPane().setLayout(null);
		getContentPane().add(boardPanel);
		getContentPane().add(lettersPanel);
		getContentPane().add(scorePanel);
		boardPanel.setBounds(185, 10, boardPanel.getPreferredSize().width, boardPanel.getPreferredSize().height);
		lettersPanel.setBounds(((boardPanel.getX()*2+boardPanel.getWidth()) / 2) - (lettersPanel.getPreferredSize().width / 2),boardPanel.getHeight()+boardPanel.getY()+10,lettersPanel.getPreferredSize().width,lettersPanel.getPreferredSize().height);
		scorePanel.setBounds(boardPanel.getX()-scorePanel.getPreferredSize().width-15, 70, scorePanel.getPreferredSize().width, scorePanel.getPreferredSize().height);

		JLabel historyLabel=new JLabel("Game History");
		historyLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
		getContentPane().add(historyLabel);
		historyLabel.setBounds(boardPanel.getX()+boardPanel.getWidth()+55, 10, 200, 35);

		chatArea = new JTextArea();
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
		chatArea.setEditable(false);
		chatScrollPane = new JScrollPane(chatArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(chatScrollPane);

		sendButton = addButton("Send");
		getContentPane().add(sendButton);
		sendButton.setVisible(false);
		sendButton.setEnabled(true);

		chatField = new JTextField();
		getContentPane().add(chatField);
		chatField.setVisible(false);

		historyArea =new JTextArea();
		historyArea.setLineWrap(true);
		historyArea.setWrapStyleWord(true);
		historyArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
		historyArea.setEditable(false);
		historyArea.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
		JScrollPane historyScrollPane = new JScrollPane(historyArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(historyScrollPane);
		historyScrollPane.setBounds(historyLabel.getX()-8, historyLabel.getY()+40, Toolkit.getDefaultToolkit().getScreenSize().width - historyLabel.getX() - 10, Toolkit.getDefaultToolkit().getScreenSize().height - 250 - historyLabel.getY());

		helpArea= new JTextArea();
		helpArea.setLineWrap(true);
		helpArea.setWrapStyleWord(true);
		helpArea.setBackground(new Color(238, 238, 238));
		helpArea.setEditable(false);
		helpArea.setFont(new Font("SansSerif", Font.BOLD, 20));
		getContentPane().add(helpArea);
		helpArea.setBounds(historyScrollPane.getX(), historyScrollPane.getY()+historyScrollPane.getHeight()+10, historyScrollPane.getWidth(), 110);

		lettersLabel = new JLabel("Letters Left: ");
		lettersLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
		getContentPane().add(lettersLabel);
		lettersLabel.setBounds(scorePanel.getX()+5, 20, 200, 35);
		lettersLabel.setVisible(false);

		submitButton = addButton("Submit");
        changeButton = addButton("Change Letters");
        challengeButton = addButton("Challenge Play");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 20));
        submitButton.setBounds(lettersPanel.getX()-110, lettersPanel.getY()+5, 100, lettersPanel.getHeight()-10);
		changeButton.setBounds(lettersPanel.getX()+lettersPanel.getWidth()+10, submitButton.getY()-3, 130, 20);
		challengeButton.setBounds(changeButton.getX(), changeButton.getY()+changeButton.getHeight()+6, changeButton.getWidth(), changeButton.getHeight());

		setJMenuBar(createMenu()); // Set up the menu bar
	}

	/**
	 * Create a JButton
	 * @param name	The text on the button
	 * @return		The JButton
	 */
	private JButton addButton(String name)
	{
        JButton button = new JButton(name);
        button.setBackground(new Color(230,240,250));
        button.setEnabled(false);
        button.addActionListener(this);
        getContentPane().add(button);
        return button;
	}

	/**
	 * Create the menu bar
	 * @return	The JMenuBar
	 */
	private JMenuBar createMenu()
	{
		JMenuBar menubar=new JMenuBar(); // The menu bar that will be created

		// Declare the menus and their items
		JMenu mainMenu, newMenu;
		JMenuItem localItem, networkItem, quitItem, aboutMenu;

		// Create all the menus
		mainMenu=new JMenu("Menu");
		newMenu=new JMenu("New Game");
		aboutMenu=new JMenuItem("About");
		localItem = new JMenuItem("Local Game", (int)'L');
		networkItem = new JMenuItem("Network Game", (int)'N');
		quitItem=new JMenuItem("Quit", (int)'Q');

		// Build the menus
		menubar.add(mainMenu);
		newMenu.add(localItem);
		newMenu.add(networkItem);
		mainMenu.add(newMenu);
		mainMenu.add(quitItem);
		mainMenu.addSeparator();
		mainMenu.add(aboutMenu);

		aboutMenu.addActionListener(this);
		localItem.addActionListener(this);
		networkItem.addActionListener(this);
		quitItem.addActionListener(this);

		return menubar;
	}

	/**
	 * Starts a new game of Scrabble and initializes all variables
	 * @param num		The number of players
	 * @param _network	Wheter it is a network game or not
	 * @param host		If it's a network game, whether this player is the host or not
	 */
	public void newGame(int num, boolean _network, boolean host)
	{
		if(!_network || host) 	turn=0;
		else 					turn = 1;
		numPlayers = num;
		scorePanel.newGame();
		networkPlay = _network;
		playing=true;
		changing=false;
		lettersEnv = new BoundedEnv[numPlayers];
		boardPanel.newGame();
		lettersPanel.newGame();
		game.setLettersEnv(lettersEnv);
		changeButton.setText("Change Letters");
		if(networkPlay && !host) 	submitButton.setEnabled(false);
		else 						submitButton.setEnabled(true);
		changeButton.setEnabled(false);
		challengeButton.setEnabled(false);
		lettersLabel.setText("Letters Left: "+LettersBag.bagSize());
		lettersLabel.setVisible(true);
		historyArea.setText("");
		scorePanel.setBounds(scorePanel.getX(), scorePanel.getY(), scorePanel.getPreferredSize().width, scorePanel.getPreferredSize().height);
		submitButton.requestFocus();
		addText("New Game");
		chatArea.setText("");
		if(networkPlay)
		{
			chatScrollPane.setBounds(scorePanel.getX(), scorePanel.getY()+scorePanel.getHeight()+20, scorePanel.getWidth(), Toolkit.getDefaultToolkit().getScreenSize().height - 400);
			chatScrollPane.setVisible(true);
			chatField.setBounds(chatScrollPane.getX(), chatScrollPane.getY()+chatScrollPane.getHeight()+10, chatScrollPane.getWidth()-75, 20);
			chatField.setVisible(true);
			sendButton.setBounds(chatField.getX()+chatField.getWidth()+5, chatField.getY(), 70, 20);
			sendButton.setVisible(true);
			chatField.setText("");
		}
		else
		{
			sendButton.setVisible(false);
			chatField.setVisible(false);
			chatScrollPane.setVisible(false);
		}
		if(networkPlay && !host) 	setHelp("Waiting for other player to play...");
		else 						setHelp("");
	}

	/**
	 * Starts a new local game
	 * @param num	The number of players
	 */
	public void newGame(int num)
	{
		newGame(num, false, false);
	}

	/*
	 * When a button is clicked
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		String str = e.getActionCommand();

		// Quit the game
		if(str.equals("Quit"))
		{
			try
			{
				System.exit(0);
			}
			catch(SecurityException ex){JOptionPane.showMessageDialog(null, "Cannot quit the game", "Scrabble", JOptionPane.ERROR_MESSAGE);}
		}
		// About
		else if(str.equals("About")){
			JOptionPane.showMessageDialog(null, "This Scrabble was made by Dean Attali on March 2006.\nComments - dean_attali@hotmail.com :)", "Scrabble", JOptionPane.INFORMATION_MESSAGE);
		System.out.println("F");}
		// New local game
		else if(str.equals("Local Game"))
		{
			if(networkPlay)
			{
				network.send("close");
			}
			new NewGameFrame(this);
		}
		// Network game
		else if(str.equals("Network Game"))
		{
			if(networkPlay)
			{
				network.send("close");
			}
			network = new NetworkScrabble(this);
		}
		// Change letters
		else if(str.equals("Change Letters"))
		{
			changing=true;
			boardEnv.removeAllNew(); // Bring back all the letters played to the letters panel
			lettersPanel.clearChangingLetters();
			lettersEnv[turn].clearSelected();
			lettersPanel.repaint();
			changeButton.setText("Cancel Change");
		}
		// Cancel changing letters
		else if(str.equals("Cancel Change"))
		{
			changing=false;
			lettersPanel.repaint();
			changeButton.setText("Change Letters");
		}
		// Submit the played letters
		else if(str.equals("Submit"))
		{
			if(game.checkValidTurn())
			{
				game.newTurn();
				changeButton.setText("Change Letters");
			}
		}
		// Challenge the previous play
		else if(str.equals("Challenge Play"))
		{
			challengeButton.setEnabled(false);
			game.challengePlay();
		}
		// Chat
		else if(str.equals("Send"))
		{
			if(network.isHost() && chatField.getText() != null && chatField.getText().trim().length() > 0)
			{
				addChat("SERVER: "+chatField.getText());
				network.send("chat "+"SERVER: "+chatField.getText());
				chatField.setText("");
			}
			else if(!network.isHost() && chatField.getText() != null && chatField.getText().trim().length() > 0)
			{
				addChat("CLIENT: "+chatField.getText());
				network.send("chat "+"CLIENT: "+chatField.getText());
				chatField.setText("");
			}
		}
	}

	/**
	 * Prepares for a new turn
	 */
	public void newTurn()
	{
		if(!networkPlay) lettersPanel.randomizeLetters(turn);
		else
		{
			// Local player finished his turn
			if(turn == 0)
			{
				lettersPanel.randomizeLetters(turn);
				submitButton.setEnabled(false);
				network.send("done");
			}
			// Remote player finished his turn
			else submitButton.setEnabled(true);
		}
		turn = getNextTurn();
		lettersEnv[turn].clearSelected();
		lettersPanel.repaint();

		// If there are no more letters, end the game
		for(int i=0;i<numPlayers;i++)
		{
			if(LettersBag.bagSize() == 0 && lettersEnv[i].numObjects() == 0)
			{
				game.endGame();
				return;
			}
		}

		boardEnv.clearSelected();
		boardPanel.repaint();
		changing=false;
		boardEnv.clearNonPermanents();
		scorePanel.repaint();
		lettersLabel.setText("Letters Left: "+LettersBag.bagSize());
		submitButton.requestFocus();
		if(networkPlay && turn == 1) setHelp("Waiting for other player to play...");
		else setHelp("");
		addText("\nPlayer "+(1+turn)+"'s turn");
	}

	/**
	 * @return Returns the turn.
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * @return Returns the last player's turn
	 */
	public int getLastTurn()
	{
		return ( (turn - 1) >= 0 ) ? (turn - 1) : numPlayers - 1 ;
	}

	/**
	 * @return Returns the next player's turn
	 */
	public int getNextTurn()
	{
		return ( (turn + 1) < numPlayers ) ? (turn + 1) : 0 ;
	}

	/**
	 * Append a string to the history text area
	 * @param str	The string to append
	 */
	public void addText(String str)
	{
		Debug.println(str);
		historyArea.append(str+"\n");
		historyArea.setCaretPosition(historyArea.getText().length());
	}

	/**
	 * Set the text of the help text area
	 * @param str	The text to set
	 */
	public void setHelp(String str)
	{
		helpArea.setText(str);
	}

	/**
	 * Append a string to the chat box
	 * @param str	The string to append
	 */
	public void addChat(String str)
	{
		chatArea.append(str+"\n");
		chatArea.setCaretPosition(chatArea.getText().length());
	}

	/**
	 * @return Returns the board enviroment
	 */
	public BoundedEnv getBoardEnv() {
		return boardEnv;
	}

	/**
	 * @param boardEnv The board environment to set
	 */
	public void setBoardEnv(BoundedEnv boardEnv) {
		this.boardEnv = boardEnv;
	}

	/**
	 * @return Returns whether the player is changing letters
	 */
	public boolean isChanging() {
		return changing;
	}

	/**
	 * @param changing - Whether to set the player to changing
	 */
	public void setChanging(boolean changing) {
		this.changing = changing;
	}

	/**
	 * @param 	num The player of which the letter environment is wanted
	 * @return	Returns the letters environment
	 */
	public BoundedEnv getLettersEnv(int num) {
		return lettersEnv[num];
	}

	/**
	 * @param lettersEnv 	The letters environment to set
	 * @param num			The player to whom the lettersEnv belongs
	 */
	public void setLettersEnv(BoundedEnv lettersEnv, int num) {
		this.lettersEnv[num] = lettersEnv;
	}

	/**
	 * @return Returns whether the is played on a network
	 */
	public boolean isNetworkPlay() {
		return networkPlay;
	}

	/**
	 * @param networkPlay Whether or not it's a network game
	 */
	public void setNetworkPlay(boolean networkPlay) {
		this.networkPlay = networkPlay;
	}

	/**
	 * @return Returns whether there is a game in place
	 */
	public boolean isPlaying() {
		return playing;
	}

	/**
	 * @param playing Whether there is a game in place
	 */
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	/**
	 * @return Returns the board panel.
	 */
	public BoardDisplay getBoardPanel() {
		return boardPanel;
	}

	/**
	 * @return Returns the challengeButton.
	 */
	public JButton getChallengeButton() {
		return challengeButton;
	}

	/**
	 * @return Returns the changeButton.
	 */
	public JButton getChangeButton() {
		return changeButton;
	}

	/**
	 * @return Returns the lettersPanel.
	 */
	public LettersPanel getLettersPanel() {
		return lettersPanel;
	}

	/**
	 * @return Returns the scorePanel.
	 */
	public ScorePanel getScorePanel() {
		return scorePanel;
	}

	/**
	 * @return Returns the submitButton.
	 */
	public JButton getSubmitButton() {
		return submitButton;
	}

	/**
	 * @return Returns the number of players.
	 */
	public int getNumPlayers() {
		return numPlayers;
	}

	/**
	 * @return Returns the game engine.
	 */
	public GameEngine getGame() {
		return game;
	}

	/**
	 * @return Returns the network class.
	 */
	public NetworkScrabble getNetwork() {
		return network;
	}

	/**
	 * @return Returns the lettersLabel.
	 */
	public JLabel getLettersLabel() {
		return lettersLabel;
	}
}