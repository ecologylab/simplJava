package ecologylab.image;

public class AreaAveragingScaleFilter
{
   int srcWidth, srcHeight;
   int destWidth, destHeight;
   int[] outputRow;
   
   private float reds[], greens[], blues[], alphas[];
   private int savedy;
   private int savedyrem;

   public AreaAveragingScaleFilter(int srcWidth, int srcHeight,
								   int destWidth, int destHeight,
								   int inputPixels[], int outputPixels[])
   {
      this.srcWidth	= srcWidth;
      this.srcHeight	= srcHeight;
      this.destWidth	= destWidth;
      this.destHeight	= destHeight;
      
      outputRow		= new int[destWidth];

      proc(inputPixels, outputPixels);
   }
   private void makeAccumBuffers() 
   {
      reds = new float[destWidth];
      greens = new float[destWidth];
      blues = new float[destWidth];
      alphas = new float[destWidth];
   }

   private void calcRow()
   {
      float mult = ((float) srcWidth) * srcHeight;

      for (int x = 0; x < destWidth; x++) 
	  {
		 int a = Math.round(alphas[x] / mult);
		 int r = Math.round(reds[x] / mult);
		 int g = Math.round(greens[x] / mult);
		 int b = Math.round(blues[x] / mult);
		 if (a < 0) {a = 0;} else if (a > 255) {a = 255;}
		 if (r < 0) {r = 0;} else if (r > 255) {r = 255;}
		 if (g < 0) {g = 0;} else if (g > 255) {g = 255;}
		 if (b < 0) {b = 0;} else if (b > 255) {b = 255;}
		 outputRow[x] = (a << 24 | r << 16 | g << 8 | b);
      }
   }

   private void accumPixels(int x, int y, int w, int h,
							int[] inputPixels, int[] outputPixels,
							int off, int scansize)
   {
      if (reds == null) 
	  {
		 makeAccumBuffers();
      }
      int sy = y;
      int syrem = destHeight;
      int dy, dyrem;
      if (sy == 0) 
	  {
		 dy = 0;
		 dyrem = 0;
      }
	  else 
	  {
		 dy = savedy;
		 dyrem = savedyrem;
      }
      while (sy < y + h) 
	  {
		 int amty;
		 if (dyrem == 0) 
		 {
			for (int i = 0; i < destWidth; i++) 
			{
			   alphas[i] = reds[i] = greens[i] = blues[i] = 0f;
			}
			dyrem = srcHeight;
		 }
		 if (syrem < dyrem) 
		 {
			amty = syrem;
		 }
		 else 
		 {
			amty = dyrem;
		 }
		 int sx = 0;
		 int dx = 0;
		 int sxrem = 0;
		 int dxrem = srcWidth;
		 float a = 0f, r = 0f, g = 0f, b = 0f;
		 while (sx < w) 
		 {
			if (sxrem == 0) 
			{
			   sxrem = destWidth;
			   int rgb;
			   rgb = ((int[]) inputPixels)[off + sx];
			   //		    rgb = model.getRGB(rgb);
			   a = rgb >>> 24;
			   r = (rgb >> 16) & 0xff;
			   g = (rgb >>  8) & 0xff;
			   b = rgb & 0xff;
			}
			int amtx;
			if (sxrem < dxrem) 
			{
			   amtx = sxrem;
			}
			else 
			{
			   amtx = dxrem;
			}
			float mult = ((float) amtx) * amty;
			alphas[dx] += mult * a;
			reds[dx] += mult * r;
			greens[dx] += mult * g;
			blues[dx] += mult * b;
			if ((sxrem -= amtx) == 0) 
			{
			   sx++;
			}
			if ((dxrem -= amtx) == 0) 
			{
			   dx++;
			   dxrem = srcWidth;
			}
		 }
		 if ((dyrem -= amty) == 0)
		 {
			calcRow();
			do
			{
			   setOutput(outputPixels, dy);
			   dy++;
			} while ((syrem -= amty) >= amty && amty == srcHeight);
		 } 
		 else 
		 {
			syrem -= amty;
		 }
		 if (syrem == 0) 
		 {
			syrem = destHeight;
			sy++;
			off += scansize;
		 }
      }
      savedyrem = dyrem;
      savedy = dy;
   }

   /**
    * Combine the components for the delivered int pixels into the
    * accumulation arrays and send on any averaged data for rows of
    * pixels that are complete.  If the correct hints were not
    * specified in the setHints call then relay the work to our
    * superclass which is capable of scaling pixels regardless of
    * the delivery hints.
    * <p>
    * Note: This method is intended to be called by the 
    * <code>ImageProducer</code> of the <code>Image</code> 
    * whose pixels are being filtered.  Developers using
    * this class to filter pixels from an image should avoid calling
    * this method directly since that operation could interfere
    * with the filtering operation.
    */
   public void proc(int inputPixels[], int outputPixels[])
   {
      accumPixels(0, 0, srcWidth, srcHeight,
				  inputPixels, outputPixels,
				  0, srcWidth);
   }
   void setOutput(int outputPixels[], int rowNum)
   {
      System.arraycopy(outputRow, 0, outputPixels, rowNum*destWidth,destWidth);
   }
}
