import java.io.*;
import java.util.StringTokenizer;

/**
 * Dean Attali<br>
 * March 10, 2006<br><br>
 *
 * This class reads the board file and gets the parameters of
 * the board (rows and columns) and stores all of the information
 * in an array that is accessible to all the classes
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class ReadFile
{
	private static int[] params = setParams(); 	// The number of rows and columns
	private static String[] line = readLines(); // A string array of all the lines

	/**
	 * Reads the first line in the file and returns the number of rows and columns
	 * @return An array with two integers: rows and columns
	 */
	private static int[] setParams()
	{
		Debug.println("Reading real.dat to get board information");
		BufferedReader reader;
		int rows=0, cols=0;

		try
		{
			reader=new BufferedReader(new FileReader("grid.dat"));
			StringTokenizer st=new StringTokenizer(reader.readLine());
			rows=Integer.parseInt(st.nextToken());
			cols=Integer.parseInt(st.nextToken());
		}catch(IOException ex){ System.out.println(ex.getMessage()); }

		return new int[] {rows, cols};
	}

	/**
	 * Read the whole file and store the lines in a String array
	 * @return A string array that stores all the lines in the file
	 */
	private static String[] readLines()
	{
		BufferedReader reader;
		String[] temp=null;

		try
		{
			reader=new BufferedReader(new FileReader("grid.dat"));
			reader.readLine();
			temp=new String[params[0]];

			for(int i=0;i<temp.length;i++)
			{
				temp[i]=reader.readLine();
			}
		}catch(IOException ex){ Debug.println(ex.getMessage()); }

		return temp;
	}

	/**Returns the rows and columns according to the file
	 * @return		an array containing the number of rows (params[0]) and number of columns (params[1])
	 */
	public static int[] getParams()
	{
		return params;
	}

	/**Returns a specific line from the file
	 * @param num		the line number in the file (starting from the second line)
	 * @return			a String representation of the line
	 */
	public static String readLine(int num)
	{
		return line[num];
	}
}