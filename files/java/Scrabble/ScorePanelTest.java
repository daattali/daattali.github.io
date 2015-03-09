//March 17

//SMALL DEBUGING PROGRAM, to check if the determineWinner method is correct
// It has to first compare the final scores, and if a few people have the same
// final score, then the one with the higher original score wins, or maybe they're tied
// This situation barely happens so I needed to check the results manually

import java.util.ArrayList;
import java.util.Collections;

class ScorePanelTest
{
	public static void main(String argp[]){
		new ScorePanelTest();
	}
	private int[] playerScore; 		// Each player's score

	/** Set up the score panel **/
	public ScorePanelTest()
	{
		determineWinner();
	}

	/** Sets the player's score **/
	public void setScore(int p, int score)
	{
		playerScore[p] += score;
	}


	/** When the game ends, determine who has the most points **/
	public void determineWinner()
	{
		int numPlayers = 10;           //        1  2  3  4  5  6  7  8  9  10
		int originalScore[] = new int[]         {5, 8, 1, 7, 4, 8, 7, 5, 8, 8};          //ORIGINAL score
		playerScore=new int[]                   {4, 6, 2, 7, 7, 2, 7, 3, 5, 6};            //FINAL score

		System.out.println("1\t2\t3\t4\t5\t6\t7\t8\t9\t10");
		System.out.println("\nORIGINAL SCORE");

		for(int i=0;i<numPlayers;i++)
		{
			System.out.print(originalScore[i]+"\t");
		}
		System.out.println("\nFINAL SCORE");
		for(int i=0;i<numPlayers;i++)
		{
			System.out.print(playerScore[i]+"\t");
		}
		System.out.println("\n");

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
						System.out.println("Game Over.\nPlayer " + (i+1) + " wins!");
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
				System.out.println("Game Over.\nPlayer " + (((Integer)playersWithHighest.get(0)).intValue()+1) + " wins!");
			else
			{
				StringBuffer tiedPlayers = new StringBuffer("");
				for(int i=0;i<playersWithHighest.size() - 1;i++)
					tiedPlayers.append((((Integer)playersWithHighest.get(i)).intValue()+1)+" and ");
				tiedPlayers.append((((Integer)playersWithHighest.get(playersWithHighest.size()-1)).intValue()+1));
				System.out.println("Game Over.\nIt's a tie between players " + tiedPlayers + "!");
			}
		}
		System.out.println("\n\n\n\n");
		System.out.println("first you look at the final scores to see if there are any winners\nif there are a few people with the same final score, you compare their original scores\n\n\n");
	}

	/** In case of a tie, finds the winner before the unplayed letters were added **/
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