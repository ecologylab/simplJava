/*
 * Written by Eunyee Koh. 
 */
package ecologylab.image;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/*
 * Create this constructor for Buffered Images, which do not need to download. 
 * So, download method do nothing. 
 * 
 * @author eunyee
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BufferedPhoto
extends PixelBased
{
   boolean		aborted;

   public BufferedPhoto(BufferedImage bufferedImage, Dimension maxDimension)
   {
	  super(bufferedImage, maxDimension);
   }
   public boolean download()
   {
   		return true;
//      debug("download() does nothing");
   }
   
   protected void rescaleImage()
   {
	   
   }
}

