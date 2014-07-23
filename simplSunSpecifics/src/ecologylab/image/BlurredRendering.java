package ecologylab.image;

/**
 * Image buffer state that applies a blur filter to the pixels chained 
 * in.
 */
public class BlurredRendering extends Rendering
{
   int		blurWidth;
   int		blurHeight;
   
   int[] sumR, sumG, sumB;	   // single row or column buffers

/**
 * Constructor for BlurredRendering.
 * @param previousRendering	The previous state in the rendering chain.
 * @param active		This state may be turned off.
 */
   public BlurredRendering(Rendering previousRendering, boolean active)
   {
      super(previousRendering, active);
      isDynamic	= true;
   }
   public BlurredRendering(Rendering previousRendering, Rendering nextRendering, boolean active)
   {
      super(previousRendering, nextRendering, active);
      isDynamic	= true;
   }

   public void compute(int bWidth, int bHeight, boolean immediate)
   {
	  //      debug("compute("+(immediate ? "immediate":"later"));
      boolean	goingInactive	= false;
      if ((bWidth == 0) || (bHeight == 0))
      {
		 if ((blurWidth == 0) || (blurHeight == 0))
		 {
			debug(".compute() does nothing. blurRadius=0");
			return;
		 }
		 else
		 {
			goingInactive	= true;
		 }
      }
      else if ((bWidth == blurWidth) && (bHeight == blurHeight))
		 return;
      
      blurWidth		= bWidth;
      blurHeight	= bHeight;
      setPending();
      
      isActive		= !goingInactive;
	  
	  //      debug("compute(isActive="+isActive);
      if (immediate)
      {
		 if (goingInactive)
			goInactive(true);
		 else
			compute(true);
      }
   }
   public void compute(Rendering inputRendering, Rendering outputRendering)
   {
	  int[] inPixels	= inputRendering.pixels;
	  int[] outPixels	= outputRendering.pixels;
	  
	  //      int n		= Math.max(width,height);
      int n		= (width > height) ? width : height;
      // ??? kludges to workaround bizarre alloc problems !!! why??????
      if ((sumR == null) || (sumR.length != n))
		 sumR		= new int[n];
      if ((sumG == null) || (sumG.length != n))
		 sumG		= new int[n];
      if ((sumB == null) || (sumB.length != n))
		 sumB		= new int[n];

      for (int j=0; j<2; j++)
      {
		 // blur vertical up and down each line
		 for (int i=0; i< width; i++)
			try
			{
			   blur1D(i, false, blurHeight, height, inPixels, outPixels);
			} catch (ArrayIndexOutOfBoundsException e)
			{
			   pixelBased.debug("??????BlurException! i="+i+ " blurHeight="+
								blurHeight+
								" "+width+"x"+height+ " sumR.length="+sumR.length +
								" sumG.length="+sumG.length + 
								" sumB.length="+sumB.length +
								" inPixels.length="+inPixels.length + 
								" outPixels.length="+outPixels.length);
			   e.printStackTrace();
			   return;		   // !!! punt blur !!!
			}
		 // blur horizontal across each raster
		 for (int i=0; i< height; i++)
			blur1D(i, true, blurWidth, width, inPixels, outPixels);
      }
   }
   public void blur1D(int rasterNum, boolean horizontal, 
		      int blurRadius, int n,
		      int[] inPix, int[] outPix)
   {
      int origin	= horizontal ? rasterNum * width : rasterNum;
      int delta		= horizontal ? 1 : width;
      
	  //      Debug.println((horizontal ? "horizontal" : "vertical") + " rasterNum="+
	  //		    rasterNum + " n="+n+" origin="+origin+" delta="+delta);

      // sum up all the values:
      sumR[0] = (inPix[origin] & R) >> 16;
      sumG[0] = (inPix[origin] & G) >> 8;
      sumB[0] = inPix[origin] & B;
      int index		= origin;
      for (int i = 1 ; i < n ; i++)
      {
		 if (index < 0 || index >= inPix.length)
		 {
		 	System.err.println("\nIndex = " + index );
		 }
		 int thisPixel	= inPix[index];
		 sumR[i] = sumR[i-1] + ((thisPixel & R) >> 16);
		 sumG[i] = sumG[i-1] + ((thisPixel & G) >> 8);
		 sumB[i] = sumB[i-1] + (thisPixel & B);
		 index += delta;
      }

      // take differences to create blurring by a box filter
      // with some particular blur radius.
      index		= origin;
      n--;			   // use this value for iHi and loop bound <=
      for (int i = 0 ; i <= n ; i++)
      {
		 //	 int iLo	= Math.max(i - blurRadius, 0);
		 //	 int iHi	= Math.min(i + blurRadius, n);
		 int iLo	= i - blurRadius;
		 if (iLo < 0)
			iLo		= 0;
		 int iHi	= i + blurRadius;
		 if (iHi > n)
			iHi		= n;
		 int divisor	= iHi - iLo;
		 if (divisor > 1)
		 {
			int thisPixel	= inPix[index];
			int r		= (sumR[iHi] - sumR[iLo]) / divisor;
			int g		= (sumG[iHi] - sumG[iLo]) / divisor;
			int b		= (sumB[iHi] - sumB[iLo]) / divisor;
			outPix[index] = (ALPHA & inPix[index]) + (r << 16) + (g << 8) + b;
		 }
		 index	       += delta;
      }
   }
}
