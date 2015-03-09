import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dean Attali<br>
 * March 17, 2006<br><br>
 *
 * A frame that shows up when the player wants to play a local game
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class NewGameFrame extends JFrame implements ActionListener
{
	private Scrabble parent;							// A reference to the main class
	private JSpinner playersSpinner, letterSpinner[];	// The spinners of the number of letters/players
	private int[] frequency;							// The frequency of every letter

	/**
	 * Sets up a new frame with all the GUI
	 * @param p	The main class
	 */
	public NewGameFrame(Scrabble p)
	{
		super("New Game");

		parent = p;
		getContentPane().setLayout(null);
		setResizable(false);

		JLabel newGameLabel = new JLabel("New Game");
		newGameLabel.setFont(new Font("SansSerif", Font.BOLD, 50));
		getContentPane().add(newGameLabel);
		newGameLabel.setBounds(60, 10, 300, 45);

		JLabel playersLabel = new JLabel("Number of Players:");
		playersLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		getContentPane().add(playersLabel);
		playersLabel.setBounds(90, 290, 200, 30);

		playersSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
		getContentPane().add(playersSpinner);
		playersSpinner.setBounds(240, 297, 35, 20);
        playersSpinner.setEditor(new JSpinner.NumberEditor(playersSpinner, "#"));
		((JSpinner.DefaultEditor)(playersSpinner.getEditor())).getTextField().setHorizontalAlignment(JTextField.CENTER);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEtchedBorder(0));
		getContentPane().add(panel);
		panel.setBounds(30, 90, 305, 200);

		JLabel lettersLabel = new JLabel("Letter Frequencies");
		lettersLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
		getContentPane().add(lettersLabel);
		lettersLabel.setBounds(panel.getX()+85, panel.getY()-30, 150, 30);

		letterSpinner = new JSpinner[27];
		JLabel[] letterLabel = new JLabel[letterSpinner.length];

		for(int i=0; i < letterSpinner.length - 1; i++)
		{
			letterLabel[i]=new JLabel(String.valueOf((char)(i+65)));
			letterLabel[i].setFont(new Font("SansSerif", Font.BOLD, 16));
			panel.add(letterLabel[i]);
			letterLabel[i].setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

			letterSpinner[i] = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
			panel.add(letterSpinner[i]);
			letterSpinner[i].setEditor(new JSpinner.NumberEditor(letterSpinner[i], "#"));
			((JSpinner.DefaultEditor)(letterSpinner[i].getEditor())).getTextField().setHorizontalAlignment(JTextField.CENTER);
		}

		letterLabel[26]=new JLabel("Blanks");
		letterLabel[26].setFont(new Font("SansSerif", Font.BOLD, 16));
		panel.add(letterLabel[26]);
		letterLabel[26].setBorder(BorderFactory.createEmptyBorder(0,10,0,0));

		letterSpinner[26] = new JSpinner(new SpinnerNumberModel(0, 0, 10, 1));
		panel.add(letterSpinner[26]);
		letterSpinner[26].setEditor(new JSpinner.NumberEditor(letterSpinner[26], "#"));
		((JSpinner.DefaultEditor)(letterSpinner[26].getEditor())).getTextField().setHorizontalAlignment(JTextField.CENTER);

		JButton startButton = new JButton("Start");
		startButton.setFont(new Font("SansSerif", Font.BOLD, 18));
		startButton.addActionListener(this);
		getContentPane().add(startButton);
		startButton.setBounds(135, 325, 100, 30);

		JButton defaultButton = new JButton("Default");
		defaultButton.setFont(new Font("SansSerif", Font.BOLD, 18));
		defaultButton.addActionListener(this);
		getContentPane().add(defaultButton);
		defaultButton.setBounds(25, 325, 100, 30);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("SansSerif", Font.BOLD, 18));
		cancelButton.addActionListener(this);
		getContentPane().add(cancelButton);
		cancelButton.setBounds(245, 325, 100, 30);

		setDefault();

		setPreferredSize(new Dimension(370, 430));
		setVisible(true);
		setLocation(100,100);
		pack();
	}

	/**
	 * Sets the letters spinners to their default values (100 letters in total)
	 * and the number of players to two
	 */
	private void setDefault()
	{
		LettersBag.setDefaultFrequency();
		frequency = LettersBag.getFrequency();
		for(int i=0;i<frequency.length;i++)
		{
			letterSpinner[i].setValue(new Integer(frequency[i]));
		}
		playersSpinner.setValue(new Integer(2));
	}

    public void actionPerformed(ActionEvent e)
    {
		String str = e.getActionCommand();

		// Close the frame
		if(str.equals("Cancel"))
		{
			dispose();
		}
		// Start a new game
		else if(str.equals("Start"))
		{
			for(int i=0;i<letterSpinner.length;i++)
			{
				frequency[i] = ((Integer)(letterSpinner[i].getValue())).intValue();
			}
			LettersBag.setFrequency(frequency);
			parent.newGame(((Integer)(playersSpinner.getValue())).intValue());
			dispose();
		}
		// Set everything to its default value
		else if(str.equals("Default"))
		{
			setDefault();
		}
	}
}