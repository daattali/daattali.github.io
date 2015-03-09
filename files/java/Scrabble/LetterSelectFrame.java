import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Dean Attali<br>
 * March 15, 2006<br><br>
 *
 * The frame that shows up when a blank tile is places to choose a letter for it
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class LetterSelectFrame extends JFrame implements ActionListener, WindowFocusListener
{
	private JComboBox comboBox;		// The combo box that lists all the letter
	private Location loc;			// The location of the letter on the board
	private Letter blankLetter;		// A reference to the blank letter
	private GameEngine game;		// The game engine
	private int selected; 			// An integer representation of the chosen letter

	/**
	 * Sets up the frame and the GUI
	 */
	public LetterSelectFrame()
	{
		super("Scrabble");
		setPreferredSize(new Dimension(180, 150));
		setResizable(false);
		setLayout(new FlowLayout());
		setVisible(false);

		String[] letters = new String[26];
		for(int i=0;i<26;i++)
		{
			letters[i]=String.valueOf((char)(65+i));
		}
		comboBox=new JComboBox(letters);

		JLabel label1 = new JLabel("Choose the letter to");
		JLabel label2 = new JLabel("use for this blank tile");

		label1.setFont(new Font("SansSerif", Font.BOLD, 16));
		label2.setFont(new Font("SansSerif", Font.BOLD, 16));

		JButton chooseButton = new JButton("Choose");
		chooseButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		chooseButton.addActionListener(this);

		getContentPane().add(label1);
		getContentPane().add(label2);
		getContentPane().add(comboBox);
		getContentPane().add(chooseButton);

		addWindowFocusListener(this);
		pack();
	}

	/**
	 * When a blank letter is played, this method is called to wait for a letter selection
	 * @param loc			The location of the letter on the board
	 * @param blankLetter	The letter
	 * @param g				The game engine
	 */
	public void getLetter(Location loc, Letter blankLetter, GameEngine g)
	{
		setVisible(true);
		toFront();
		this.loc=loc;
		this.blankLetter = blankLetter;
		game = g;
		comboBox.setSelectedIndex(0);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width-getSize().width)/2,(dim.height-getSize().height)/2);
	}
	
	// When the Choose button is pressed, inform the game engine what letter is selected
	public void actionPerformed(ActionEvent e)
	{
		selected = comboBox.getSelectedIndex();
		game.blankLetterSelected(selected, loc, blankLetter);
		setVisible(false);
	}

	// If the frame loses focus, close it
	public void windowLostFocus(WindowEvent e)
	{
		game.blankLetterSelected(-1, loc, blankLetter);
		setVisible(false);
	}

	public void windowGainedFocus(WindowEvent e) {}
}