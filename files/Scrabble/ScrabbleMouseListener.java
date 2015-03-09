import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;

/**
 * Dean Attali<br>
 * March 11, 2006<br><br>
 *
 * The mouse listener for the board and the letters panel
 *
 * @author Dean Attali
 * @version 1 March 2006
 */
public class ScrabbleMouseListener extends MouseInputAdapter
{
	private JPanel parentPanel;		// The panel that uses this mouse listener
	private Scrabble parent; 		// A reference to the main game class

	/**
	 * Creates a new mouse listener
	 * @param panelp	The panel that uses this mouse listener
	 * @param p			The main game class
	 */
	public ScrabbleMouseListener(JPanel panelp, Scrabble p)
	{
		parentPanel = panelp;
		parent = p;
	}

	/**
	 * When the mouse is released, inform the parent panel
	 */
	public void mouseReleased(MouseEvent e)
	{
		// If the left button was released inside the panel
		if (SwingUtilities.isLeftMouseButton(e) &&
			e.getX()>0 && e.getY()>0 && e.getX()<parentPanel.getWidth() && e.getY()<parentPanel.getHeight())
		{
			if(parentPanel instanceof BoardDisplay) // If the mouse was released inside the board
			{
				parent.getGame().mouseInBoard(e.getX(), e.getY());
			}

			if(parentPanel instanceof LettersPanel) // If the mouse was released inside the letter panel
			{
				parent.getGame().mouseInLetters(e.getX(), e.getY());
			}
		}
	}
	
	/**
	 * When the mouse is moved inside the board panel, notify it
	 */
	public void mouseMoved(MouseEvent e)
	{
		if(parentPanel instanceof BoardDisplay)
		{
			parent.getGame().mouseMoved(e.getX(), e.getY());
		}		
	}
	
	/**
	 * When the mouse exits the board panel, notify it
	 */
	public void mouseExited(MouseEvent e)
	{
		if(parentPanel instanceof BoardDisplay)
		{
			parent.getGame().mouseExited();
		}
	}
}