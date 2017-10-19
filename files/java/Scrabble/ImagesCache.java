import java.util.HashMap;
import javax.swing.ImageIcon;
import java.net.URL;

/**
 * Dean Attali<br>
 * March 10, 2006<br><br>
 *
 * This class is a cache of all the images (letters).<br>
 * Instead of loading the images all the time, this only loads
 * an image once using a HashMap
 *
 * @author Dean Attali
 * @version 1 March 2006
 */

public class ImagesCache
{
	// A hashmap that relates sprites with their names
	private static HashMap<String, ImageIcon> sprites=new HashMap<String, ImageIcon>();

	/**
	 * Returns the specified image
	 * @param name	the name of the image
	 * @return 		the sprite, as an ImageIcon
	 **/
	public static ImageIcon getSprite(String name)
	{
		ImageIcon img = sprites.get(name);
		if (img == null)
		{
			Debug.println("Image "+name+ " loaded");
			try{
				URL url = new URL("https://deanattali.com/files/java/Scrabble/" + name);
			    img = new ImageIcon(url);
			}catch(Exception ex){}
			sprites.put(name,img);
		}

		return img;
	}
}