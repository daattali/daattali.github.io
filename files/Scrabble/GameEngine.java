import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

/**
 * Dean Attali<br>
 * March 12, 2006<br><br>
 *
 * The class is the responsible for handling the gameplay
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class GameEngine
{
	// References to instances of the classes I need
	private Scrabble parent;
	private BoardDisplay boardPanel;
	private LettersPanel lettersPanel;
	private ScorePanel scorePanel;
	private BoundedEnv boardEnv;
	private BoundedEnv[] lettersEnv;

	private final int SIZE = Scrabble.SIZE; // The dimensions of every letter
	private ArrayList<String> words;		// The list of words formed
	private int[][] grid;					// Each grid's score value
	private int turnsPassed;				// The number of consecutive turns passed
	private boolean turnLost;				// True when a player challenged a play and loses his turn

	/**
	 * Set the references to the instances
	 * @param p	The Scrabble class
	 */
	public GameEngine(Scrabble p)
	{
		parent = p;
		boardPanel = parent.getBoardPanel();
		lettersPanel = parent.getLettersPanel();
		scorePanel = parent.getScorePanel();
		boardEnv = parent.getBoardEnv();
		grid = boardPanel.getGrid();
	}

	/**
	 * When the mouse is clicked inside the board
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	public void mouseInBoard(int x, int y)
	{
		// Ignore the click if there is no game in play,
		// the player lost his turn, or if he's changing his letters
		if(!parent.isPlaying() || parent.isChanging() ||
			(parent.isNetworkPlay() && parent.getTurn() == 1))
				return;

		Location loc=new Location(y/SIZE, x/SIZE); // The location where the mouse was released

		// If a letter that was already selected was clicked, deselect it
		if(loc.equals(boardEnv.getSelected()))
		{
			boardEnv.clearSelected();
		}

		// If there is already a letter here
		else if(boardEnv.objectAt(loc) != null)
		{
			if(!boardEnv.objectAt(loc).isPerm())// If it's non-permanent, select it
			{
				boardEnv.setSelected(loc);
			} else { 							// If it's a permanent letter, deselect everything
				boardEnv.clearSelected();
			}

		}

		// If there is no letter here
		else if(boardEnv.isEmpty(loc))
		{
			// If there is a letter to play from the letters panel, play it
			if(lettersEnv[parent.getTurn()].getSelected().isValid() && !lettersEnv[parent.getTurn()].isEmpty(lettersEnv[parent.getTurn()].getSelected()))
			{
				playLetter(loc);
			}

			// If another letter from the board is already selected, move it here
			else if(boardEnv.getSelected().isValid() && !boardEnv.isEmpty(boardEnv.getSelected()))
			{
				moveLetter(loc);
			}

			// If there is no letter to put here, deselect everything
			else
			{
				boardEnv.clearSelected();
			}
		}
		else boardEnv.clearSelected();

		// Redraw the board and the letters
		lettersEnv[parent.getTurn()].clearSelected();
		lettersPanel.repaint();
		boardPanel.repaint();
	}

	/**
	 * When the mouse is clicked in the letters panel
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	public void mouseInLetters(int x, int y)
	{
		// Ignore the click if there is no game in play or if the player lost his turn
		if(!parent.isPlaying() || (parent.isNetworkPlay() && parent.getTurn() == 1)) return;

		Location loc=new Location(y/SIZE, (x-5)/SIZE); // The location where the mouse was released

		// If the player is changing letters, select/deselect it
		if(parent.isChanging() && lettersEnv[parent.getTurn()].objectAt(loc) != null)
		{
			lettersPanel.changeChangingLetter(loc.col());
		}

		// If this letter was already selected, deselect it
		else if(loc.equals(lettersEnv[parent.getTurn()].getSelected()))
		{
			lettersEnv[parent.getTurn()].clearSelected();
		}

		// If there is a letter here, select it
		else if(lettersEnv[parent.getTurn()].objectAt(loc) != null)
		{
			lettersEnv[parent.getTurn()].setSelected(loc);
		}

		// If there is no letter here, deselect everything
		else if(lettersEnv[parent.getTurn()].objectAt(loc) == null)
		{
			lettersEnv[parent.getTurn()].clearSelected();

			// If a letter on the board was selected, move it back here
			if(boardEnv.getSelected().isValid() && !boardEnv.isEmpty(boardEnv.getSelected()))
			{
				removeLetter();
			}
		}

		// Redraw the board and the letters
		lettersPanel.repaint();
		boardEnv.clearSelected();
		boardPanel.repaint();
		parent.setHelp("");
	}

	/**
	 * When the mouse exits the board panel
	 */
	public void mouseExited()
	{
		// If the player can play, clear the help area
		if(parent.isPlaying() && !(parent.isNetworkPlay() && parent.getTurn() == 1))
		{
			parent.setHelp("");
		}
	}

	/**
	 * When the mouse is moved in the board panel
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 */
	public void mouseMoved(int x, int y)
	{
		// Ignore if there is no play
		if(!parent.isPlaying() || (parent.isNetworkPlay() && parent.getTurn() == 1)) return;

		Location loc=new Location(y/SIZE, x/SIZE); // The location where the mouse is
		parent.setHelp("");

		// If this square is empty and colored, show (in the help area) what it does
		if(boardEnv.isEmpty(loc))
		{
			switch(grid[loc.row()][loc.col()])
			{
			case 1: parent.setHelp("Light blue squares double the letter's value"); break;
			case 2: parent.setHelp("Dark blue squares triple the letter's value"); break;
			case 3: parent.setHelp("Light pink squares double the word's value"); break;
			case 4: parent.setHelp("Dark pink squares triple the word's value"); break;
			}
		}
	}

	/**
	 * Place a letter from the letters panel in the board
	 * @param loc	The location of the letter in the board
	 */
	private void playLetter(Location loc)
	{
		Letter selectedLetter=(lettersEnv[parent.getTurn()].objectAt(lettersEnv[parent.getTurn()].getSelected()));

		// If it's a blank tile
		if(selectedLetter.value() == 0)
		{
			new LetterSelectFrame().getLetter(loc, selectedLetter, this);
		}
		// Not a blank tile
		else
		{
			Debug.println("Placing "+selectedLetter.letter()+" at "+loc.toString());
			new Letter(boardEnv, loc, selectedLetter.letter());
			lettersEnv[parent.getTurn()].remove(selectedLetter);
		}
	}

	/**
	 * When a letter is chosen for the blank from the LetterSelectFrame
	 * @param num				// The letter that was selected
	 * @param loc				// The location of the letter
	 * @param selectedLetter	// The selected letter
	 */
	public void blankLetterSelected(int num, Location loc, Letter selectedLetter)
	{
		if(num == -1)
		{
			return; // If no letter was selected, don't do anything
		}

		Debug.println("Placing a blank tile (" + (char)(num+65) + ") at "+loc.toString());
		new Letter(boardEnv, loc, (char)(num+65), true);
		lettersEnv[parent.getTurn()].remove(selectedLetter);
		lettersPanel.repaint();
		boardPanel.repaint();
	}

	/**
	 * Remove a letter from the board back to the letters panel
	 */
	private void removeLetter()
	{
		Letter selectedLetter=boardEnv.objectAt(boardEnv.getSelected());
		Debug.println("Removing "+selectedLetter.letter()+" from "+boardEnv.getSelected().toString());

		// If it was a blank letter
		if(selectedLetter.isBlank())
		{
			new Letter(lettersEnv[parent.getTurn()], lettersEnv[parent.getTurn()].getNextEmpty(), '?', true);
		}
		// If it wasn't a blank
		else
		{
			new Letter(lettersEnv[parent.getTurn()], lettersEnv[parent.getTurn()].getNextEmpty(), selectedLetter.letter());
		}

		boardEnv.remove(selectedLetter);
	}

	/**
	 * Move a letter within the board
	 * @param loc The new location in the board
	 */
	private void moveLetter(Location loc)
	{
		Letter selectedLetter = boardEnv.objectAt(boardEnv.getSelected());
		Debug.println("Moving "+selectedLetter.letter() + " from "+boardEnv.getSelected().toString()+" to "+loc.toString());
		new Letter(boardEnv, loc, selectedLetter.letter(), selectedLetter.isBlank());
		boardEnv.remove(boardEnv.objectAt(boardEnv.getSelected()));
	}

	/**
	 * Check if the player's turn is valid
	 * @return Returns whether the play is valid or not
	 */
	public boolean checkValidTurn()
	{
		// If it's the first play of the game, check that:
		//		At least 2 letters were played
		//		A letter was played on the center tile
		//		All the letters were played in a line
		if(boardEnv.numObjects() == boardEnv.numNonPerm())
		{
			if(boardEnv.numNonPerm() < 2)
			{
				parent.setHelp("Invalid play\nAt least 2 letters have to be played");
				return false;
			}
			else if(boardEnv.isEmpty(new Location(boardEnv.numRows()/2, boardEnv.numCols()/2)))
			{
				parent.setHelp("Invalid play\nA letter has to be placed in the center");
				return false;
			}
			else if(!inLine())
			{
				return false;
			}
			else
			{
				return true;
			}
		}

		// Check that all the letter are in a line and at least one new letter touches an old one
		if(inLine() && touchesOld())
		{
			return true;
		}
		return false;
	}

	/**
	 * Check if all the letters were played in a line
	 * @return Returns true if the letters were placed in a single line, false otherwise
	 */
	private boolean inLine()
	{
		// If less than two letters were played, it has to be a line
		if(boardEnv.numNonPerm()<2)
		{
			return true;
		}

		// Get the first two letters in the board and where the second is compared to the first
		Location firstLetterLoc = boardEnv.getNewObjects()[0];
		Location secondLetterLoc = boardEnv.getNewObjects()[1];
		Direction dirToCheck = boardEnv.getDirection(firstLetterLoc, secondLetterLoc);

		int nonPermInLine = 1; // The number of non-permanent letters found in a line
		nonPermInLine = recurseLetters(firstLetterLoc, dirToCheck, nonPermInLine);  // Check all the letters, going at the correct direction

		// If all the played letters are found, keep checking
		if(nonPermInLine == boardEnv.numNonPerm())
		{
			return true;
		}
		parent.setHelp("Invalid play\nThe letters must be placed in a line");
		return false;
	}

	/**
	 * Recurse up/down/left/right from a location and count how many letters there are
	 * @param loc				// The previous location
	 * @param dir				// The direction to check
	 * @param nonPermInLine		// The number of new letters found
	 * @return	Returns the number of new letters found in the line
	 */
	private int recurseLetters(Location loc, Direction dir, int nonPermInLine)
	{
		// Get the new location
		Location newLoc = boardEnv.getNeighbor(loc, dir);

		// If it's empty or outside the board, stop
		if(boardEnv.isEmpty(newLoc) || !boardEnv.isValid(newLoc))
		{
			return nonPermInLine;
		}
		// If there is a non-permanent letter here, increment nonPermInLine
		if(!boardEnv.objectAt(newLoc).isPerm())
		{
			nonPermInLine++;
		}

		return recurseLetters(newLoc, dir, nonPermInLine); // Keep recursing in this direction
	}

	/**
	 * @return Returns true if at least one new letter is touching at least one old letter
	 */
	private boolean touchesOld()
	{
		// If no letters were played, it's fine
		if(boardEnv.numNonPerm()==0)
		{
			return true;
		}

		// Get all the new letters
		Location[] newObj = boardEnv.getNewObjects();

		// Check all his neighbors, and if one of them is permanent, return true
		for (int i=0; i<newObj.length; i++)
		{
			ArrayList<Location> neighbors = boardEnv.neighborsOf(newObj[i]);
			for(int j=0; j<neighbors.size(); j++)
			{
				Location loc = neighbors.get(j);
				if(!boardEnv.isEmpty(loc) && boardEnv.objectAt(loc).isPerm())
				{
					return true;
				}
			}
		}
		parent.setHelp("Invalid play\nThe letters must touch existing words");
		return false;
	}

	/**
	 * Prepare for a new turn
	 */
	public void newTurn()
	{
		boardEnv.writeNewObjects(); // Show the players what letters were played

		// If the player is changing letters
		if(parent.isChanging())
		{
			int numChanging=0; // The number of letters to be changed
			turnsPassed = 0; // He didn't pass his turn

			for(int i=0;i<7;i++)
			{
				// If the letter is to be changed, add it to the letters bag and remove from the panel
				if(lettersPanel.isChanging(i))
				{
					numChanging++;
					Letter tempLetter = lettersEnv[parent.getTurn()].objectAt(new Location(0, i));
					LettersBag.addLetter(tempLetter.letter());
					lettersEnv[parent.getTurn()].remove(tempLetter);
					if(parent.isNetworkPlay())
					{
						parent.getNetwork().send("change "+tempLetter.letter());
					}
				}
			}
			if(numChanging>0)
			{
				parent.addText("Changed "+numChanging+" letters");
				if(parent.isNetworkPlay()) parent.getNetwork().send("changing "+numChanging);
			}
		}

		recordScore(); // Count how many points are earned

		// If the player did not use any letters, he passed his turn
		if(!parent.isChanging() && words!=null && words.size() == 0 && !turnLost)
		{
			turnsPassed++;

			// If all the players passed, ask if to end the game
			if(turnsPassed >= parent.getNumPlayers())
			{
				// In a netowrk game, don't ask both sides, only ask the current player
				if(!parent.isNetworkPlay() || parent.getTurn() == 0)
				{
					if(JOptionPane.showConfirmDialog(null,
							"All players have passed their turns.\nDo you want to end the game?",
							"End Game?",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE)
						== JOptionPane.YES_OPTION)
					{
						if(parent.isNetworkPlay())
						{
							parent.getNetwork().send("close");
						}
						endGame();
						return;
					}
				}
			}
		}
		// If the player formed words or lost his turn, he did'nt pass
		else if(!parent.isChanging() || turnLost)
		{
			turnsPassed = 0;
			turnLost = false;
		}

		parent.newTurn(); // Inform the main class about a new turn

		// If no words were formed, disable challenging
		if(words.size() == 0 || (parent.isNetworkPlay() && parent.getTurn() == 1))
		{
			parent.getChallengeButton().setEnabled(false);
		}
		else
		{
			parent.getChallengeButton().setEnabled(true);
		}

		// If there are no more letters or the player is being challenged, disable changing letters
		if(LettersBag.bagSize() == 0 || (parent.isNetworkPlay() && parent.getTurn() == 1))
		{
			parent.getChangeButton().setEnabled(false);
		}
		else
		{
			parent.getChangeButton().setEnabled(true);
		}

	}

	/**
	 * Check how many points a player got in this turn
	 */
	private void recordScore()
	{
		words=new ArrayList<String>(); // Clear the words from the last turn
		int totScore = 0; // The player's total score for this turn

		// If no letters were played, no points are awarded
		if(boardEnv.numNonPerm() == 0);

		// If one letter was played, check words in both directions (vertical and horizontal)
		else if(boardEnv.numNonPerm() == 1)
		{
			Direction dirToCheck = Direction.SOUTH;
			Location letterLoc = boardEnv.getNewObjects()[0];
			for(int i=0;i<2;i++)
			{
				totScore += checkWordScore(letterLoc, dirToCheck);
				dirToCheck = dirToCheck.toLeft();
			}
		}

		// If more than one letter was played
		// First check the main word formed by the new letters in the direction they were played
		// Then check any words that they formed perpendicular to the way they were played
		else
		{
			// Get the direction in which the letters were played (East = horizontal, South = vertical)
			Location firstLetterLoc = boardEnv.getNewObjects()[0];
			Location secondLetterLoc = boardEnv.getNewObjects()[1];
			Direction dirToCheck = boardEnv.getDirection(firstLetterLoc, secondLetterLoc);

			totScore += checkWordScore(firstLetterLoc, dirToCheck); // Check the main word

			// Check what way the words will be (perpendicular to the main word)
			if(dirToCheck.equals(Direction.SOUTH))
			{
				dirToCheck = Direction.EAST;
			}
			else if(dirToCheck.equals(Direction.EAST))
			{
				dirToCheck = Direction.SOUTH;
			}

			// For every letter, check if it's part of another word
			Location[] newObj = boardEnv.getNewObjects();
			for(int i=0;i<newObj.length;i++)
			{
				totScore += checkWordScore(newObj[i], dirToCheck);
			}
		}

		// If all the tiles were played, add 50 points
		if(boardEnv.numNonPerm()==7)
		{
			totScore += 50;
			parent.addText("50 Points bonus!");
		}
		parent.addText("Total of "+totScore+" points");
		scorePanel.setScore(parent.getTurn(), totScore); // Add the points to the total score

	}

	/**
	 * Check the score of a word
	 * @param letterLoc		// The location to check
	 * @param dirToCheck	// The direction to check
	 * @return	Returns the number of points gained by this word
	 */
	private int checkWordScore(Location letterLoc, Direction dirToCheck)
	{
		Location startLoc = wordStartLoc(letterLoc, dirToCheck.reverse());
		int wordScore = checkWord(startLoc, dirToCheck, 0, 1, new StringBuilder());
		if(wordScore > 0)
		{
			parent.addText((String)words.get(words.size()-1) + " - "+wordScore+" points");
		}
		return wordScore;
	}

	/**
	 * Find the location where a word starts
	 * @param loc	// A location of any letter in the word
	 * @param dir	// The direction to check
	 * @return	Returns the location where the word starts
	 */
	private Location wordStartLoc(Location loc, Direction dir)
	{
		// Get the new location
		Location newLoc = boardEnv.getNeighbor(loc, dir);

		// If it's empty or outside the board, stop
		if(boardEnv.isEmpty(newLoc) || !boardEnv.isValid(newLoc))
		{
			return loc;
		}

		return wordStartLoc(newLoc, dir); // Keep recursing in this direction
	}

	/**
	 * Checks how many points a word is worth
	 * @param loc			// The location to check
	 * @param dir			// The direction to check
	 * @param score			// The total score of the word
	 * @param multiplyWord	// The number by which to multiply the whole word (red squares)
	 * @param sb			// A StringBuilder of the word that was made
	 * @return	Returns the score of this word
	 */
	private int checkWord(Location loc, Direction dir, int score, int multiplyWord, StringBuilder sb)
	{
		int multiplyLetter = 1; // How much to multiply the letter's value by
		Letter curLetter = boardEnv.objectAt(loc); // The current letter
		sb.append(curLetter.letter()); // Add the letter to the word

		// If the letter is new, check any special squares
		if( ! curLetter.isPerm())
		{
			if ( grid[loc.row()][loc.col()] == 1)
			{
				multiplyLetter = 2;
			}
			else if ( grid[loc.row()][loc.col()] == 2)
			{
				multiplyLetter = 3;
			}
			else if ( grid[loc.row()][loc.col()] == 3)
			{
				multiplyWord *= 2;
			}
			else if ( grid[loc.row()][loc.col()] == 4)
			{
				multiplyWord *= 3;
			}
		}

		// Add the current letter to the score
		score += multiplyLetter * curLetter.value();

		// Get the next location
		Location nextLoc = boardEnv.getNeighbor(loc, dir);

		// If it's empty or outside the board, return the word's total score
		if(boardEnv.isEmpty(nextLoc) || !boardEnv.isValid(nextLoc))
		{
			// If there is no word (length = 1), no points are awarded
			if(sb.length() == 1)
			{
				return 0;
			}

			words.add(sb.toString()); // Add the word to the words list
			return score * multiplyWord;
		}

		return checkWord(nextLoc, dir, score, multiplyWord, sb);
	}

	/**
	 * The player has requested to challenge the previous play
	 * Check if all the words are in the dictionary and act accordingly
	 */
	public void challengePlay()
	{
		parent.addText("Play challenged!");

		for(int i=0; i<words.size(); i++)
		{
			if( ! ScrabbleDict.checkWord((String)words.get(i)))
			{
				parent.addText("Challenge Accepted ("+words.get(i)+")\nPlayer "+(parent.getLastTurn()+1)+" lost his previous turn");
				scorePanel.playChallenged();
				boardEnv.clearLastPlayed();
				boardPanel.repaint();
				if(parent.isNetworkPlay() && parent.getTurn() == 0) parent.getNetwork().send("challenge true "+words.get(i));
				return;
			}
		}
		if(parent.isNetworkPlay() && parent.getTurn() == 0) parent.getNetwork().send("challenge false");
		parent.addText("Challenge Declined\nPlayer "+(parent.getTurn()+1)+" lost his turn");
		turnLost = true;
		newTurn();
	}

	/**
	 * Process incoming data, when playing over the internet
	 * @param str	The string that was sent
	 */
	public void process(String str)
	{
		// Draw a letter out of the bag
		if(str.startsWith("draw"))
		{
			new Letter(parent.getLettersEnv(1), parent.getLettersEnv(1).getNextEmpty(), str.charAt(5));
			LettersBag.removeLetter(str.charAt(5));
			parent.getLettersLabel().setText("Letters Left: "+LettersBag.bagSize());
		}
		// Tell the remote user what letters were played and put them on the board
		else if(str.startsWith("play"))
		{
			StringTokenizer st=new StringTokenizer(str);
			st.nextToken();
			boolean blank = st.nextToken().equals("true");
			char let = st.nextToken().charAt(0);
			for(int i=0;i<7;i++)
			{
				Letter selectedLetter=(lettersEnv[1].objectAt(new Location(0, i)));
				if(blank && selectedLetter != null && selectedLetter.letter() == '?')
				{
					new Letter(boardEnv, new Location(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())), let, blank);
					lettersEnv[1].remove(selectedLetter);
					break;
				}
				else if(!blank && selectedLetter != null && selectedLetter.letter() == let)
				{
					new Letter(boardEnv, new Location(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken())), let, blank);
					lettersEnv[1].remove(selectedLetter);
					break;
				}
			}
		}
		// Remote player is done, my turn now
		else if(str.startsWith("done"))
		{
			newTurn();
		}
		else if(str.startsWith("change"))
		{
			for(int i=0; i<7; i++)
			{
				Letter tempLetter = lettersEnv[parent.getTurn()].objectAt(new Location(0, i));
				if(tempLetter != null && tempLetter.letter() == str.charAt(7))
				{
					LettersBag.addLetter(tempLetter.letter());
					lettersEnv[parent.getTurn()].remove(tempLetter);
				}
			}
		}
		else if(str.startsWith("changing"))
		{
			parent.addText("Changed "+str.charAt(9)+" letters");
		}
		else if(str.startsWith("challenge"))
		{
			StringTokenizer st=new StringTokenizer(str);
			st.nextToken();
			if(st.nextToken().equals("false"))
			{
				parent.addText("Play challenged!");
				parent.addText("Challenge Declined\nPlayer "+(parent.getTurn()+1)+" lost his turn");
				turnLost = true;
			}
			else
			{
				parent.addText("Play challenged!");
				parent.addText("Challenge Accepted ("+st.nextToken()+")\nPlayer "+(parent.getLastTurn()+1)+" lost his previous turn");
				scorePanel.playChallenged();
				boardEnv.clearLastPlayed();
				boardPanel.repaint();
			}
		}
		// Chat
		else if(str.startsWith("chat"))
		{
			parent.addChat(str.substring(5));
		}
	}

	/**
	 * End the game
	 */
	public void endGame()
	{
		if(parent.isPlaying()) scorePanel.determineWinner();
		parent.setPlaying(false);
		parent.setChanging(false);
		parent.getSubmitButton().setEnabled(false);
		parent.getChangeButton().setEnabled(false);
		parent.getChallengeButton().setEnabled(false);
		lettersPanel.repaint();
	}

	/**
	 * Set the letter environments
	 * @param envs	The letter environments
	 */
	public void setLettersEnv(BoundedEnv[] envs)
	{
		lettersEnv = envs;
	}

}