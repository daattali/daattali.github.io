import java.util.ArrayList;
import java.util.Collections;

/**
 * Dean Attali<br>
 * March 11, 2006<br><br>
 *
 * This class represents a bag of all the unused letters
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class LettersBag
{
	private static ArrayList<Character> letters; 	// The letters
	private static int[] frequency;					// How many of each letter there are in total
	
	/**
	 * Create a new bag of letters
	 */
	public static void createNewBag()
	{
		Debug.println("Creating the bag of letters");
		letters=new ArrayList<Character>();
		
		// Add the letters
		for(int i=0; i<26; i++)
		{
			for(int j=0; j<frequency[i]; j++)
			{
				letters.add((char)(i+65));
			}
		}
		
		// The blank tiles
		for(int i = 0; i < frequency[26]; i++)
		{
			letters.add(new Character((char)'?'));
		}
		
		Collections.shuffle(letters); // Shuffle, since the letters were places in order
	}
	
	/**
	 * Sets the bag to the default (100 letters)
	 */
	public static void setDefaultFrequency()
	{
		frequency = new int[]{9,2,2,4,12,2,3,2,9,1,1,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1,2};
	}
	
	/**
	 * Change the number of letters
	 * @param freq	An array with 27 elements of the number of each letter
	 */
	public static void setFrequency(int[] freq)
	{
		frequency = freq;
	}
	
	/**
	 * @return Returns the frequency of every letter
	 */
	public static int[] getFrequency()
	{
		return frequency;
	}

	/**
	 * @return Returns a random letter from the bag
	 */
	public static char nextLetter()
	{
		char c=(letters.get(0)).charValue();
		letters.remove(0);

		Debug.println("The letter "+c+" was drawn out of the bag; "+bagSize()+" letters left");
		return c;
	}
	
	/**
	 * Adds a letter to the bag
	 * @param c	The letter
	 */
	public static void addLetter(char c)
	{
		letters.add(new Character(c));
		Debug.println("Putting the letter "+c+" back into the bag; "+bagSize()+" letters left");
		Collections.shuffle(letters);
	}
	
	/**
	 * Removes a letter from the bag
	 * @param toRemove	The letter
	 */
	public static void removeLetter(char toRemove)
	{
		for(int i=0; i<letters.size(); i++)
		{
			char c = ((Character)letters.get(i)).charValue();
			if(c == toRemove)
			{
				letters.remove(i);
				Debug.println("The letter "+c+" was drawn out of the bag; "+bagSize()+" letters left");
				break;
			}
		}
	}

	/**
	 * @return Returns the number of letters in the bag
	 */
	public static int bagSize()
	{
		return letters.size();
	}
}