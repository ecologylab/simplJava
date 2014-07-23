package ecologylab.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;

import ecologylab.generic.Colors;
import ecologylab.generic.ImageTools;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * @author andruid
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
@simpl_inherit
public class AlphaGradientRendering extends Rendering
{
	@simpl_scalar
  float radius;
	
	@simpl_scalar
  int minAlpha;
	
	int minAlphaInt;
   
	public AlphaGradientRendering()
	{
		
	}
	
/**
 * Constructor for AlphaGradientRendering.
 * @param previousRendering
 * @param active
 */
   public AlphaGradientRendering(Rendering previousRendering, boolean active)
   {
	   this(previousRendering, active, 0, ((int) (((float)0 / 255.0f) * (float) Colors.R)) & Colors.R );
   }
   
   public AlphaGradientRendering(Rendering previousRendering, boolean active, int radius, int minAlpha)
   {
  	 super(previousRendering, active);
	   this.radius 		= radius;
	   this.minAlpha 	= minAlpha;
	   minAlphaInt 		= 0;
   }

   public void compute(int radius, int minAlpha)
   {
      this.radius	= radius;
      this.minAlpha	= minAlpha;
      doCompute(true, previousRendering);
      isActive		= true;
   }
   public void compute0(int[] inPixels, int[] outPixels)
   {
      fill(outPixels, G + B);
   }
   public void compute(Rendering inputRendering, Rendering outputRendering)
   {
  	 // cant use straight copy, because JPG images are read with 3 bands.
  	 // if they dont get scaled, the source pixels may not have alpha set!
  	 //System.arraycopy(inPixels, 0, outPixels, 0, inPixels.length);

  	 // let the graphics context do the copy, so it can smartly fix alpha
  	 ImageTools.copyImage(inputRendering.bufferedImage, outputRendering.bufferedImage);

  	 int[] outPixels	= outputRendering.pixels;

  	 // work in red space, to avoid signed arithmetic problems;
  	 // shift up later

  	 // gradientMagnitude is the full delta of alpha, across the
  	 // gradient area. 
  	 int gradientMagnitude	= R - minAlpha;
  	 int pixelIndex		= 0;
  	 int beyondRight	= (int)(width - radius); // x >= places in gradient
  	 int beyondBottom	= (int)(height - radius);// y >= places in gradient
  	 //	 debug("radius="+radius+ ", minAlpha="+Integer.toHexString(minAlpha) +
  	 //	       ", gradientMagnitude="+hex(gradientMagnitude) +
  	 //	       "\t"+width+"x"+height+"\n");
  	 for (int j=0; j<height; j++)
  	 {
  		 int yFactor	= height;
  		 if (j < radius)
  			 yFactor	= j;
  		 else if (j >= beyondBottom)
  			 yFactor	= height - j - 1;
  		 for (int i=0; i<width; i++)
  		 {
  			 int xFactor	= width;
  			 // gradientFactor is 0 in the outermost ring:
  			 //	most faded out -- gradient=minAlpha --
  			 // moving toward radius in the innermost ring
  			 int gradientFactor	= yFactor;
  			 if (i < radius)
  				 xFactor	= i;
  			 else if (i >= beyondRight)
  				 xFactor	= width - i - 1;
  			 if (xFactor < gradientFactor)
  				 gradientFactor	= xFactor;

  			 if (gradientFactor < radius)
  			 {
  				 double gradientChange = ((float) gradientFactor / radius) * gradientMagnitude ;
  				 int thisGradient = // shift to compensate for sign avoidance
  					 ((int) gradientChange + minAlpha)<< 8;

  				 // !!! already blt-ed them all over
  				 //	       int thisPixel	= outPixels[pixelIndex];
  				 //int srcPixel		= inPixels[pixelIndex];
  				 // note: input pixels have been copied to output pixels already!
  				 int srcPixel		= outPixels[pixelIndex];
  				 // includes original translucence -- if there was --
  				 // overriding our gradient
  				 //int srcAlpha		= ALPHA; // all bits on
  				 //if (srcHasAlpha) // baby got alpha from copyImage
  				 //srcAlpha		&= srcPixel; // only include src's alpha if the src has alpha; otherwise keep 'em on
  				 int newPixel	= 
  					 (thisGradient & ALPHA & srcPixel) | (srcPixel & RGB);
  				 outPixels[pixelIndex]	= newPixel;
  			 }
  			 pixelIndex++;
  		 }			   // for i < width
  	 }			   // for j < height
      
   }
	protected ColorModel getColorModel(BufferedImage referenceImage)
	{
		return PixelBased.ARGB_MODEL;
	}
	protected SampleModel getSampleModel(BufferedImage referenceImage)
	{
		return new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, width, height, PixelBased.ARGB_MASKS);
	}
	public void paint(Graphics g, int x, int y)
	{
		super.paint(g, x, y);
	}

	// for ORM layer:
	
	public float getRadius()
	{
		return radius;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}

	public int getMinAlpha()
	{
		return minAlpha;
	}

	public void setMinAlpha(int minAlpha)
	{
		this.minAlpha = minAlpha;
	}
	@Override
	protected void resizeImageComponents(int width, int height, boolean createBufferedImage)
	{
		double oldRadius	= radius;
		double ratio	= Math.sqrt((double) (width * height) / (double) (this.width * this.height));
//		debug("resize ratio: " + oldRadius +"\t-> " + radius);
		radius	*= ratio;
		super.resizeImageComponents(width, height, createBufferedImage);
	}
	
	public int getMinAlphaInt() {
		return minAlphaInt;
	}

	public void setMinAlphaInt(int minAlphaInt) {
		this.minAlphaInt = minAlphaInt;
	}
}
