import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Dean Attali<br>
 * March 13, 2006<br><br>
 *
 * A panel that is responsible for the players' scores and displays them
 *
 * @author Dean Attali
 * @version 1 March 2006
 */


public class ScorePanel extends JPanel
{
	private Scrabble parent; 		// A reference to the main game class
	private int[] playerScore; 		// Each player's score
	private int lastScore; 			// The score of the previous turn
	
	/**
	 * Set up the score panel
	 * @param p	The main game class
	 */
	public ScorePanel(Scrabble p)
	{
		parent = p;
		setBorder(BorderFactory.createEtchedBorder(1));
		setPreferredSize(new Dimension(160, 60));
	}

	/**
	 * Returns a player's score
	 * @param p The player
	 * @return Returns the player's score
	 */
	public int getScore(int p)
	{
		return playerScore[p];
	}
	
	/**
	 * @return Returns the last turn's score
	 */
	public int getLastScore()
	{
		return lastScore;
	}
	
	/**
	 * Set's a player's score
	 * @param p		The player
	 * @param score	The points to add to this player's score
	 */
	public void setScore(int p, int score)
	{
		lastScore = score;
		playerScore[p] += score;
		repaint();
	}

	/**
	 * If the previous play was challenged, take back the previous player's score
	 */
	public void playChallenged()
	{
		setScore(parent.getLastTurn(), -lastScore);
	}

	/**
	 * Reset the players' scores for a new game
	 */
	public void newGame()
	{
		playerScore=new int[parent.getNumPlayers()];
		setPreferredSize(new Dimension(160, 60 + parent.getNumPlayers() * 35));
		repaint();
	}

	/**
	 * Draws the score panel and the players' scores
	 */
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d=(Graphics2D)g;
		super.paintComponent(g2d);
		
		// Title
		g2d.setColor(Color.black);
		g2d.setFont(new Font("SansSerif", Font.BOLD, 40));
		g2d.drawString("Score", 20, 45);
		
		// Every player's score
		g2d.setColor(Color.gray.darker());
		g2d.setFont(new Font("SansSerif", Font.BOLD, 18));
		for(int i=0; i < parent.getNumPlayers();i ++)
		{
			g2d.drawString("Player "+(i+1)+": "+(playerScore[i]), 40, 80+i*35);
		}
		
		// If a game is in place, draw the arrow showing whose turn it is
		if(parent.isPlaying())
		{
			g2d.setColor(Color.orange);
			int xArrow[]=new int[8], yArrow[]=new int[8];
	
			// Determine the x values of the arrow
			xArrow[0]=12;
			xArrow[1]=xArrow[0]+10;
			xArrow[2]=xArrow[1];
			xArrow[3]=xArrow[2]+10;
			xArrow[4]=xArrow[3]-10;
			xArrow[5]=xArrow[4];
			xArrow[6]=xArrow[5]-10;
			xArrow[7]=xArrow[6];
	
			// Determine the y values of the arrow
			yArrow[0]=68 + parent.getTurn() * 35;
			yArrow[1]=yArrow[0];
			yArrow[2]=yArrow[1]-4;
			yArrow[3]=yArrow[2]+8;
			yArrow[4]=yArrow[3]+8;
			yArrow[5]=yArrow[4]-4;
			yArrow[6]=yArrow[5];
			yArrow[7]=yArrow[6]-8;
	
			g2d.fillPolygon(xArrow, yArrow, 8); // Draw the arrow
			g2d.setColor(Color.black); // Set the colour to black
			g2d.drawPolygon(xArrow, yArrow, 8); // Outline the arrow
		}
	}
	
	/**
	 * When the game ends, determine who has the most points
	 */
	public void determineWinner()
	{
		int numPlayers = parent.getNumPlayers();
		int originalScore[] = new int[numPlayers];
		int unplayed[]=new int[numPlayers];
		
		for(int i=0; i < numPlayers; i++)
		{
			originalScore[i] = getScore(i); // Get the original score
			
			// Get the value of the unplayed letters
			for(int j=0;j<7;j++)
			{
				if(!parent.getLettersEnv(i).isEmpty(new Location(0, j)))
					unplayed[i] += parent.getLettersEnv(i).objectAt(new Location(0, j)).value();
			}
		}
		
		for(int i=0; i < numPlayers; i++)
		{
			// If there were unplayed letters, deduct them from the score
			if(unplayed[i] > 0)
			{
				setScore(i, -unplayed[i]);
			}
			// If all the letters were played, add the other players' unplayed to this score
			else
			{
				for(int j=0; j<numPlayers; j++)
				{
					setScore(i, unplayed[j]);
				}
			}			
		}

		// Store the scores into an array list
		ArrayList<Integer> scores = new ArrayList<Integer>();
		for(int i=0; i<numPlayers; i++)
		{
			scores.add(playerScore[i]);
		}
		
		// Sort the scores from highest to lowest
		Collections.sort(scores);
		Collections.reverse(scores);
		
		// If someone has the highest score
		if( ((Integer)scores.get(0)).intValue() > ((Integer)scores.get(1)).intValue())
		{
			for(int i=0; i<numPlayers; i++)
			{
				if(playerScore[i] == ((Integer)scores.get(0)).intValue())
				{
					parent.setHelp("Game Over.\nPlayer " + (i+1) + " wins!");
				}
			}
		}
		// If there is a tie
		else
		{
			int highest = recurseForWinner(0, 0, ((Integer)scores.get(0)).intValue(), originalScore);
			ArrayList<Integer> playersWithHighest=new ArrayList<Integer>();

			for(int i=0; i<numPlayers; i++)
			{
				if(originalScore[i] == highest && playerScore[i] == ((Integer)scores.get(0)).intValue())
					playersWithHighest.add(i);
			}
			if(playersWithHighest.size() == 1)
				parent.setHelp("Game Over.\nPlayer " + (((Integer)playersWithHighest.get(0)).intValue()+1) + " wins!");
			else
			{
				StringBuffer tiedPlayers = new StringBuffer("");
				for(int i=0;i<playersWithHighest.size() - 1;i++)
					tiedPlayers.append((((Integer)playersWithHighest.get(i)).intValue()+1)+" and ");
				tiedPlayers.append((((Integer)playersWithHighest.get(playersWithHighest.size()-1)).intValue()+1));
				parent.setHelp("Game Over.\nIt's a tie between players " + tiedPlayers + "!");
			}
		}
	}

	/**
	 * In case of a tie, finds the winner before the unplayed letters were added
	 * @param num 				The player number
	 * @param highest			The player with the highest original score
	 * @param highscore			The final score that was tied by 2 or more players
	 * @param originalscore		The original score of all the players
	 * @return		Returns the highest original score
	 */
	private int recurseForWinner(int num, int highest, int highscore, int[] originalscore)
	{

		if(num == originalscore.length) return originalscore[highest];

		// If this player's end score is one of the ones that are tied and its original score
		// is the highest so far, store this player
		if(playerScore[num] == highscore && originalscore[num] >= originalscore[highest])
		{
			return recurseForWinner(++num, num-1, highscore, originalscore);
		}
		else return recurseForWinner(++num, highest, highscore, originalscore);
	}
}