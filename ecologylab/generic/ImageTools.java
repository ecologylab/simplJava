package ecologylab.generic;


import java.io.*;
import java.awt.image.*;
import java.awt.*;
import javax.imageio.*;
import javax.imageio.stream.*;
import java.util.*;
import javax.imageio.plugins.jpeg.*;

/**
 * A set of lovely convenience methods for writing image files.
 */
public class ImageTools 
extends Debug
{
/**
 * Take the RenderedImage passed in, compress it,
 * and writes it to a file created from outfileName.
 * 
 * @param compressionQuality ranges between 0 and 1, 0-lowest, 1-highest.
 */
   public static void writeJpegFile(RenderedImage rendImage, 
				    String outfileName, 
				    float compressionQuality)
   {
      writeJpegFile(rendImage, new File(outfileName), compressionQuality);
   }
/**
 * Take the RenderedImage passed in, compress it,
 * and writes it to outfile.
 * 
 * @param compressionQuality ranges between 0 and 1, 0-lowest, 1-highest.
 */
    public static void writeJpegFile(RenderedImage rendImage, 
				     File outfile, float compressionQuality)
    {
        try 
        {
    
            // Find a jpeg writer
            ImageWriter writer = null;
            Iterator iter = ImageIO.getImageWritersByFormatName("jpg");
            if (iter.hasNext()) {
                writer = (ImageWriter)iter.next();
            }
    
            // Prepare output file
            ImageOutputStream ios = ImageIO.createImageOutputStream(outfile);
            writer.setOutput(ios);
    		
    		ImageTools imageTools	=	new ImageTools();
            // Set the compression quality
            ImageWriteParam iwparam = imageTools.new MyImageWriteParam();
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) ;
            iwparam.setCompressionQuality(compressionQuality);
    
            // Write the image
            writer.write(null, new IIOImage(rendImage, null, null), iwparam);
    
            // Cleanup
            ios.flush();
            writer.dispose();
            ios.close();
        } 
        catch (Exception e) 
        {
        	e.printStackTrace();
        }
    }
    
   /**
    * Scale the image, then write a jpeg.
    */
/**
 * Take the RenderedImage passed in, compress it,
 * and writes it to a file created from outfileName.
 * 
 * @param compressionQuality ranges between 0 and 1, 0-lowest, 1-highest.
 */
	public static void writeJpegFile(BufferedImage image, 
					 String fileName, 
					 float compressionQuality,
					 int width, int height)
	{
//		final int THUMBNAIL_WIDTH 		= 245;
//		final int THUMBNAIL_HEIGHT		= 350;
	    
//	    int width 	= THUMBNAIL_WIDTH;
//	    int height	= THUMBNAIL_HEIGHT;
	    
	    float thumbRatio	= (float)width / (float)height;
	    int imageWidth	= image.getWidth(null);
	    int imageHeight	= image.getHeight(null);
	    float imageRatio	= (float)imageWidth / (float)imageHeight;
	    
	    if (thumbRatio < imageRatio) 
	    {
	      height = (int)(width / imageRatio);
	    } 
	    else 
	    {
	      width = (int)(height * imageRatio);
	    }
	    //println("writeThumbnail("+width+","+height+
		//    " type="+image.getType());

	    // draw original image to thumbnail image object and
	    // scale it to the new size on-the-fly
	    BufferedImage scaledImage = new BufferedImage(width, 
	      height, image.getType());
	      
	    Graphics2D graphics2D = scaledImage.createGraphics();
	    
	    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	      RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	      
	    graphics2D.drawImage(image, 0, 0, width, height, null);
	    
	    writeJpegFile(scaledImage, fileName, compressionQuality);
  	}

    
    
    // This class overrides the setCompressionQuality() method to workaround
    // a problem in compressing JPEG images using the javax.imageio package.
    class MyImageWriteParam extends JPEGImageWriteParam 
    {
        public MyImageWriteParam() {
            super(Locale.getDefault());
        }
    
        // This method accepts quality levels between 0 (lowest) and 1 (highest) and simply converts
        // it to a range between 0 and 256; this is not a correct conversion algorithm.
        // However, a proper alternative is a lot more complicated.
        // This should do until the bug is fixed.
        public void setCompressionQuality(float quality) 
        {
            if (quality < 0.0F || quality > 1.0F) {
                throw new IllegalArgumentException("Quality out-of-bounds!");
            }
            this.compressionQuality = 256 - (quality * 256);
        }
    }


}
