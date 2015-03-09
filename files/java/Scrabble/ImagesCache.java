import java.util.HashMap;
import javax.swing.ImageIcon;

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
			img = new ImageIcon(name);
			sprites.put(name,img);
		}

		return img;
	}
}