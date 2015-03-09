import java.util.ArrayList;
import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * Dean Attali<br>
 * March 11, 2006<br><br>
 * 
 * This class is a modified version of the Fish from the
 * Marine Biologist Simulation
 * 
 * @author Dean Attali
 * @version 1 March 2006
 */

public class Letter implements Locatable
{
    private BoundedEnv theEnv, oldEnv;	// The current and previous environments of this letter
    private Location myLoc, oldLoc; 	// The letter's current and previous locations
    private ImageIcon image;			// The letter's image
    private char letter;				// The letter
    private int value;					// The value of this letter
    private boolean permanent;			// Whether the letter can be moved or not
    private boolean blank;				// Whether the letter is a blank or not

    /** Constructs a non-blank letter at the specified location in a given environment.
     *  @param env    	Environment in which the letter is created
     *  @param loc    	Location of the letter in the environment
     *  @param letter	The letter of this
     */
    public Letter(BoundedEnv env, Location loc, char letter)
    {
    	this(env, loc, letter, false);
    }
    
    /** Constructs a letter at the specified location in a given environment.
     *  @param env    	Environment in which the letter is created
     *  @param loc    	Location of the letter in the environment
     *  @param letter	The letter of this
     *  @param blank	Whether this letter is a blank
     */
    public Letter(BoundedEnv env, Location loc, char letter, boolean blank)
    {
        theEnv = env;
        oldEnv = env;
        myLoc = loc;
        oldLoc = loc;
        this.letter=letter;
    
        // Determine if it's a blank tile with no letter
        if(String.valueOf(letter).equals("?"))
        {
        	image = ImagesCache.getSprite("pics/BLANK.gif");
        	value = 0;
        	this.blank = true;
        }
        // If it's a blank tile that has a letter 
        else if(blank)
        {
        	this.blank = true;
        	value = 0;
        	image = ImagesCache.getSprite("pics/BLANK"+letter+".gif");
        }
        else
        {
            image=ImagesCache.getSprite("pics/"+letter+".gif");
            determineValue();        	
        }
           
        permanent=false;
        theEnv.add(this);
    }

	/**
	 * @return Returns the letter
	 */
	public char letter()
	{
		return letter;
	}

	/**
	 * @return Returns the image
	 */
	public Image image()
	{
		return image.getImage();
	}

	/**
	 * @return Returns the letter's value
	 */
	public int value()
	{
		return value;
	}
	
	/**
	 * @return Returns whether the letter is a blank tile or not
	 */
	public boolean isBlank()
	{
		return blank;
	}
	
    /**
     * @return Returns if the letter is permanent in the board or not
     */
    public boolean isPerm()
    {
		return permanent;
	}

	/**
	 * Sets this letter to be permanent
	 */
	public void setPerm()
	{
		permanent=true;
	}

	/**
	 * @return Returns the letter's previous location
	 */
	public Location oldLoc()
	{
		return oldLoc;
	}

    /**
     * Returns this letter's location.
     * @return        The location of the letter in the board
     */
    public Location location()
    {
        return myLoc;
    }

    /**
     * Determine the value of this letter
     */
    private void determineValue()
    {
		switch(letter)
		{
			case 'A' : case 'E' : case 'I' : case 'L' : case 'N' : case 'O' : case 'R' : case 'S' : case 'T' : case 'U': value=1; break;
			case 'D' : case 'G': value=2; break;
			case 'B' : case 'C' : case 'M' : case 'P': value=3; break;
			case 'F' : case 'H' : case 'V' : case 'W' : case 'Y': value=4; break;
			case 'K': value=5; break;
			case 'J' : case 'X': value=8; break;
			case 'Q' : case 'Z': value=10; break;
		}
	}
}
