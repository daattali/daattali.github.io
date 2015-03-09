import javax.swing.*;
import java.awt.*;

/**
 * Dean Attali<br>
 * March 11, 2006<br><br>
 *
 * This panel shows the 7 letter tiles that the player can use
 * to create words, and stores some information about them
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class LettersPanel extends JPanel
{
	private Scrabble parent; 				// A reference to the main game class
	private final int NUM_LETTERS = 7; 		// The number of letters the player can use
	private final int SIZE = Scrabble.SIZE; // The dimensions of every letter
	private boolean changingLetter[]; 		// True if a letter is to be changed

	/**
	 * Create the panel and set variables
	 * @param p	The main class
	 */
	public LettersPanel(Scrabble p)
	{
		Debug.println("Setting up the letters panel");
		parent = p;
		setBorder(BorderFactory.createEtchedBorder(0));
		setPreferredSize(new Dimension( NUM_LETTERS * SIZE + 10, SIZE+10 ));
		addMouseListener(new ScrabbleMouseListener(this, parent));
	}

	/**
	 * Resets the panel to prepare for a new game
	 */
	public void newGame()
	{
		Debug.println("Creating new letters panel");
		for(int i=0; i < parent.getNumPlayers(); i++)
		{
			parent.setLettersEnv(new BoundedEnv(1, NUM_LETTERS, 5, 5, this, parent), i);
		}
		LettersBag.createNewBag();
		if(!parent.isNetworkPlay()){
			for(int i=0; i< parent.getNumPlayers(); i++)
			{
				randomizeLetters(i);
			}
		}
		else
		{
			randomizeLetters(0);
		}
		repaint();
	}

	/**
	 * Fills the 7 letter spots with random letters
	 * @param turn	The player whose letters should be randomized
	 */
	public void randomizeLetters(int turn)
	{
		for(int i=0;i<NUM_LETTERS;i++)
		{
			// Make sure that there are letters in the bag
			if(LettersBag.bagSize()>0 && parent.getLettersEnv(turn).objectAt(new Location(0, i)) == null)
			{
				Letter temp = new Letter(parent.getLettersEnv(turn), new Location(0, i), LettersBag.nextLetter());
				if(parent.isNetworkPlay())
				{
					parent.getNetwork().send("draw "+temp.letter());
				}
			}
		}
	}

	/**
	 * Draw the panel
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g2d);
		
		if(!parent.isPlaying()) return;
		
		// If it's a network game, always show this player's letters
		if(parent.isNetworkPlay())
		{
			parent.getLettersEnv(0).drawEnv(g2d);
			if(parent.isChanging() && parent.getTurn() == 0) drawChanging(g2d);
		}
		else
		{
			parent.getLettersEnv(parent.getTurn()).drawEnv(g2d);
			if(parent.isChanging()) drawChanging(g2d);
		}
	}
	
	/**
	 * If the player wants to change letters, outline them
	 * @param g2d	The graphics on which to draw
	 */
	public void drawChanging(Graphics2D g2d)
	{
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(2));

		for(int i=0; i<changingLetter.length; i++)
		{
			if(changingLetter[i])
			{
				g2d.drawRect(SIZE*i+5, 5, SIZE, SIZE);
			}
		}

		g2d.setStroke(new BasicStroke(1));		
	}
	
	/**
	 * Reset the changing status to false
	 */
	public void clearChangingLetters()
	{
		changingLetter=new boolean[7];
	}
	
	/**
	 * Select/Deselect a letter
	 * @param num The index (column) of the letter
	 */
	public void changeChangingLetter(int num)
	{
		changingLetter[num] = !changingLetter[num];
	}
	
	/**
	 * @param num The index of the letter
	 * @return Returns if this letter is being changed
	 */
	public boolean isChanging(int num)
	{
		return changingLetter[num];
	}
	
}