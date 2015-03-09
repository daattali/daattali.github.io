import java.io.*;
import java.util.ArrayList;

/**
 * Dean Attali<br>
 * March 10, 2006<br><br>
 *
 * This dictionary class reads words from a dictionary file and
 * stores them in an array.<br>
 * It can be used to check if a word exists or not from any class
 * without being instantiated
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class ScrabbleDict
{
	private static ArrayList words=getWords(); 	// The array of the words

	/**
	 * @return Returns an ArrayList with all the words from the dictionary
	 */
	private static ArrayList getWords()
	{
		Debug.println("Reading the dictionary...");
		ArrayList<String> temp=new ArrayList<String>();

		try
		{
			BufferedReader reader=new BufferedReader(new FileReader("dict.dat"));
			temp=new ArrayList<String>();

			String s;

			while((s=reader.readLine())!=null)
			{
				if(s.length()>1) // Don't add single-lettered words
					temp.add(s);
			}
		} catch(IOException ex){ Debug.println(ex.getMessage()); }

		Debug.println("Dictionary was set with "+temp.size()+" words");
		return temp;
	}

	/**Determines whether a word is valid
	 * @param str 		the word to check
     * @return 			<code>true</code> if <code>word</code> is in the dictionary
     *          		<code>false</code> otherwise
     **/
	public static boolean checkWord(String str)
	{
		boolean real=words.contains(str.toLowerCase());
		Debug.println("Checking if "+str.toLowerCase()+" is a real word: "+real);
		return real;
	}
}