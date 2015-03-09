import javax.swing.*;
import java.awt.*;

/**
 * Dean Attali<br>
 * March 10, 2006<br><br>
 * 
 * This class is a modified version of the BoundedEnv from the
 * Marine Biologist Simulation
 * 
 * @author Dean Attali
 * @version 1 March 2006
 */

public class BoundedEnv extends SquareEnvironment
{
	private Scrabble parent; 				// A reference to the main game class
    private Letter[][] theGrid;  			// Grid representing the environment
    private int objectCount;        		// Number of objects in current environment
    private JPanel parentPanel;				// The panel that is using this environment
    private int hGap, vGap;					// The vertical and horizontal gaps to draw
    private Location selected;				// The location of the selected object
    private int nonPermCount;				// The number of non permanent letters on the board
    private Location[] lastPlayed; 			// The pieces played the previous turn
    private final int SIZE = Scrabble.SIZE; // The dimensions of every letter

    /**
     * Constructs an empty BoundedEnv object
     * @param rows 		Number of rows
     * @param cols 		Number of columns
     * @param hGap		The horizontal gap
     * @param vGap		The vertical hap
     * @param panelp	The panel that created this object
     * @param p			The main game class
     */
    public BoundedEnv(int rows, int cols, int hGap, int vGap, JPanel panelp, Scrabble p)
    {
        theGrid = new Letter[rows][cols];
        objectCount = 0;
        this.hGap=hGap;
        this.vGap=vGap;
        parentPanel=panelp;
        parent = p;
        clearSelected();
        Debug.println("Creating new environment with "+rows+" rows and "+cols+" columns");
	}

    /**
     * Constructs an empty BoundedEnv object with no gaps at the sides
     * @param rows
     * @param cols
     * @param panelp
     * @param p
     */
    public BoundedEnv(int rows, int cols, JPanel panelp, Scrabble p)
    {
		this(rows, cols, 0, 0, panelp, p);
    }

    /** 
     * Returns the number of rows in the environment.
     * @return   The number of rows
     */
    public int numRows()
    {
        return theGrid.length;
    }

    /** 
     *  @return   The number of columns
     **/
    public int numCols()
    {
        return theGrid[0].length;
    }
    
    /**
     * @return The location of the selected letter
     */
    public Location getSelected()
    {
		return selected;
	}

	/**
	 * @param loc	The location of the selected letter
	 */
	public void setSelected(Location loc)
	{
		selected=loc;
	}

	/**
	 * Clears the selected letter
	 */
	public void clearSelected()
	{
		selected=new Location(-1, -1);
	}
	
	/**
	 * @return The number of non-permanent letters in the environment
	 */
	public int numNonPerm()
	{
		return nonPermCount;
	}

    /** 
     * Verifies whether a location is valid in this environment.
     * @param  loc    location to check
     * @return <code>true</code> if <code>loc</code> is valid;
     *         <code>false</code> otherwise
     */
    public boolean isValid(Location loc)
    {
        if ( loc == null )
            return false;

        return (0 <= loc.row() && loc.row() < numRows()) &&
               (0 <= loc.col() && loc.col() < numCols());
    }

    /** 
     * Returns the number of objects in this environment.
     * @return   the number of objects
     */
    public int numObjects()
    {
        return objectCount;
    }

    /** 
     * Returns all the objects in this environment.
     * @return    an array of all the environment objects
     */
    public Letter[] allObjects()
    {
        Letter[] theObjects = new Letter[numObjects()];
        int tempObjectCount = 0;

        // Look at all grid locations.
        for ( int r = 0; r < numRows(); r++ )
        {
            for ( int c = 0; c < numCols(); c++ )
            {
                // If there's an object at this location, put it in the array.
                Letter obj = theGrid[r][c];
                if ( obj != null )
                {
                    theObjects[tempObjectCount] = obj;
                    tempObjectCount++;
                }
            }
        }

        return theObjects;
    }

    /** 
     * Determines whether a specific location in this environment is
     * empty.
     * @param loc  the location to test
     * @return     <code>true</code> if <code>loc</code> is a
     *             valid location in the context of this environment
     *             and is empty; <code>false</code> otherwise
     */
    public boolean isEmpty(Location loc)
    {
        return isValid(loc) && objectAt(loc) == null;
    }

    /** 
     * Returns the object at a specific location in this environment.
     * @param loc    the location in which to look
     * @return       the object at location <code>loc</code>;
     *               <code>null</code> if <code>loc</code> is not
     *               in the environment or is empty
     */
    public Letter objectAt(Location loc)
    {
        if ( ! isValid(loc) )
            return null;

        return theGrid[loc.row()][loc.col()];
    }

    /** 
     * Creates a single string representing all the objects in this
     * environment (not necessarily in any particular order).
     * @return    a string indicating all the objects in this environment
     */
    public String toString()
    {
        Letter[] theObjects = allObjects();
        String s = "Environment contains " + numObjects() + " objects: ";
        for ( int index = 0; index < theObjects.length; index++ )
            s += theObjects[index].toString() + " ";
        return s;
    }

    /** 
     * Adds a new object to this environment at the location it specifies.
     * (Precondition: <code>obj.location()</code> is a valid empty location.)
     * @param obj the new object to be added
     * @throws    IllegalArgumentException if the precondition is not met
     */
    public void add(Letter obj)
    {
        // Check precondition.  Location should be empty.
        Location loc = obj.location();
        if ( ! isEmpty(loc) )
            throw new IllegalArgumentException("Location " + loc +
                                    " is not a valid empty location");

        // Add object to the environment.
        theGrid[loc.row()][loc.col()] = obj;
        objectCount++;
        nonPermCount++;
    }

    /** 
     * Removes the object from this environment.
     * (Precondition: <code>obj</code> is in this environment.)
     * @param obj     the object to be removed
     * @throws    IllegalArgumentException if the precondition is not met
     */
    public void remove(Letter obj)
    {
        theGrid[obj.oldLoc().row()][obj.oldLoc().col()] = null;
        objectCount--;
        nonPermCount--;
    }
    
    /**
     * Remove all the objects from the environment
     */
    public void removeAll()
    {
		Debug.println("Removing all objects from the environment");
		Letter[] theObjects = allObjects();
		for(int i = 0; i<theObjects.length; i++)
			remove(theObjects[i]);
		nonPermCount=0;
	}

	/**
	 * Draw the environment
	 * @param g2d	The graphics on which to draw
	 */
	public void drawEnv(Graphics2D g2d)
	{	
        Letter alocatable[] = allObjects();
        for(int i = 0; i < alocatable.length; i++)
        {
			Letter temp=(alocatable[i]);
			g2d.drawImage(temp.image(), temp.location().col()*SIZE+hGap, temp.location().row()*SIZE+vGap, SIZE, SIZE, parentPanel);
		}
		drawSelected(g2d);
	}

	/**
	 * Draw the selected letter
	 * @param g2d The graphics on which to draw
	 */
	private void drawSelected(Graphics2D g2d)
	{
        g2d.setColor(Color.red);
        g2d.setStroke(new BasicStroke(2));

		if(objectAt(getSelected()) != null)
			g2d.drawRect(SIZE*getSelected().col()+hGap, SIZE*getSelected().row()+vGap, SIZE, SIZE);

		g2d.setStroke(new BasicStroke(1));
	}
	
	/**
	 * @return The next empty location in the environment
	 */
	public Location getNextEmpty()
	{
		for(int r=0;r<numRows();r++)
		{
			for(int c=0;c<numCols();c++){
				if (isEmpty(new Location(r, c))){
					return new Location(r, c);
				}
			}
		}
		return new Location(-1, -1);
	}

	/**
	 * @return An array of the locations of the new objects
	 */
	public Location[] getNewObjects()
	{
		Letter[] objects = allObjects();
		Location[] newObj=new Location[nonPermCount];
		int iCount=0;
		
		for(int i=0;iCount<nonPermCount && i<objects.length;i++)
		{
			if(!objects[i].isPerm()){
				newObj[iCount]=objects[i].location();
				iCount++;
			}
		}
		return newObj;
	}
	
	/**
	 * Changes all the non-permanent letters to permanent 
	 */
	public void clearNonPermanents()
	{
		Location[] nonPerms = getNewObjects();
		lastPlayed=new Location[nonPermCount];
		for(int i=0; i<nonPermCount; i++)
		{
			objectAt(nonPerms[i]).setPerm();
			lastPlayed[i]=new Location(nonPerms[i].row(), nonPerms[i].col());
		}
		nonPermCount=0;
	}

	/**
	 * If a play was challenged, delete the letters played on that turn
	 */
	public void clearLastPlayed()
	{
		for(int i=0;i<lastPlayed.length;i++)
		{
			Letter curLetter=objectAt(lastPlayed[i]);
			parent.getLettersEnv(parent.getLastTurn()).remove(parent.getLettersEnv(parent.getLastTurn()).objectAt(new Location(0, i)));
			if(curLetter.isBlank())
			{
				new Letter(parent.getLettersEnv(parent.getLastTurn()), parent.getLettersEnv(parent.getLastTurn()).getNextEmpty(), '?', true);
			}
			else
			{
				new Letter(parent.getLettersEnv(parent.getLastTurn()), parent.getLettersEnv(parent.getLastTurn()).getNextEmpty(), curLetter.letter());
			}
			remove(curLetter);				
		}
		nonPermCount=0;
	}
	
	/**
	 * Show the player where the new objects were placed
	 */
	public void writeNewObjects()
	{
		Location[] nonPerms = getNewObjects();
		for(int i=0; i<nonPermCount; i++)
		{
			parent.addText("Placed "+objectAt(nonPerms[i]).letter()+" at "+nonPerms[i].toString());
			// If it's a network game, send info in the format of
			//		play blank/non-blank letter row column 
			if(parent.isNetworkPlay() && parent.getTurn() == 0) 
			{
				parent.getNetwork().send("play "+objectAt(nonPerms[i]).isBlank()+" "+objectAt(nonPerms[i]).letter()+" "+nonPerms[i].row()+" "+nonPerms[i].col());
			}
		}		
	}
	
	/**
	 * Remove all the new objects
	 */
	public void removeAllNew()
	{
		Location[] nonPerms = getNewObjects();
		for(int i=0; i<nonPerms.length; i++)
		{
			Letter temp = objectAt(nonPerms[i]);
			new Letter(parent.getLettersEnv(parent.getTurn()), parent.getLettersEnv(parent.getTurn()).getNextEmpty(), temp.letter());
			parent.getBoardEnv().remove(temp);
			parent.getBoardPanel().repaint();
			parent.getLettersPanel().repaint();
		}
	}
}
