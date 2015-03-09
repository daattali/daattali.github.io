import javax.swing.*;
import java.awt.*;
import java.util.StringTokenizer;

/**
 * Dean Attali<br>
 * March 10, 2006<br><br>
 *
 * The BoardDisplay class is responsible for drawing the board
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class BoardDisplay extends JPanel
{
	private Scrabble parent; 				// A reference to the main game class
	private BoundedEnv env;					// A reference to the board's environment	
	private final int SIZE = Scrabble.SIZE; // The dimensions of every letter
	private final int COLS, ROWS; 			// The number of rows and columns in the board
	
	// The grid's int and color representations
	private int[][] grid;
	private final Color[] gridCol=new Color[] { Color.lightGray, Color.cyan, new Color(11, 140, 223), Color.pink, Color.magenta};

	/**
	 * Set up the board and initialize values
	 * @param p	The Scrabble object that instantiated this object
	 */
	public BoardDisplay(Scrabble p)
	{
		Debug.println("Setting up the board");
		parent = p;
		env=new BoundedEnv((ReadFile.getParams())[0], (ReadFile.getParams())[1], this, parent);
		parent.setBoardEnv(env);
		COLS=env.numCols();
		ROWS=env.numRows();
		ScrabbleMouseListener sml = new ScrabbleMouseListener(this, parent);
		addMouseListener(sml);
		addMouseMotionListener(sml);
		setGrid();
		setPreferredSize(new Dimension(COLS*SIZE, ROWS*SIZE));
	}

    /**
     * Reset the board to prepare for a new game
     */
    public void newGame()
    {
		env.removeAll(); 	// Reset the grid
		setGrid(); 			// Create the grid
		repaint(); 			// Draw the grid
	}

	/**
	 * Get the colors of every square in the grid 
	 */
	private void setGrid()
	{
		Debug.println("Getting the board colors");
		StringTokenizer st;
		grid=new int[ROWS][COLS];

		for(int r=0;r<ROWS;r++)
		{
			st=new StringTokenizer(ReadFile.readLine(r));

			for(int c=0;c<COLS;c++)
			{
				grid[r][c]=Integer.parseInt(st.nextToken());
			}
		}
	}

	/* 
	 * Draw the board
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
    {
		Graphics2D g2d = (Graphics2D)g;
        super.paintComponent(g2d);
        drawSquares(g2d); 	// Draw the squares
        drawGrid(g2d); 		// Draw the grid
		env.drawEnv(g2d); 	// Draw the letters
	}
	
	/**
	 * Draws the grid
	 * @param g2d	The graphics on which to draw
	 */
	private void drawGrid(Graphics2D g2d)
	{	
		g2d.setColor(Color.white);
		for(int c=0;c<COLS-1;c++)
		{
			for(int r=0;r<ROWS-1;r++)
			{
				g2d.drawLine((c+1)*SIZE-1, 0, (c+1)*SIZE-1, ROWS*SIZE);
				g2d.drawLine(0, (r+1)*SIZE, COLS*SIZE, (r+1)*SIZE);
			}
		}	
	}

	/**
	 * Draws the squares in different colors
	 * @param g2d	The graphics on which to draw
	 */
	private void drawSquares(Graphics2D g2d)
	{
		// Draw the background
		g2d.setColor(gridCol[0]);
		g2d.fillRect(0, 0, COLS*SIZE, ROWS*SIZE);		
		
		// Color the special squares
		for(int r=0;r<ROWS;r++)
		{
			for(int c=0;c<COLS;c++)
			{
				if(grid[r][c]!=0)
				{
					g2d.setColor(gridCol[grid[r][c]]);
					g2d.fillRect(c*SIZE, r*SIZE, SIZE, SIZE);
				}
			}
		}		
	}
	
	/**
	 * @return Returns the grid
	 */
	public int[][] getGrid()
	{
		return grid;
	}
}