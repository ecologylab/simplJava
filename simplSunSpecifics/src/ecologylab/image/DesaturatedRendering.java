package ecologylab.image;

import ecologylab.generic.ImageTools;

/**
 * Rendering that applies a desaturating filter to the pixels chained in.
 */
public class DesaturatedRendering extends Rendering
{
	static final float	MIN_DESAT	= .07f;
	
	float								degree;

	/**
	 * Constructor for DesaturatedRendering.
	 * 
	 * @param suppliedPixels
	 * @param previousRendering
	 * @param active
	 */
	public DesaturatedRendering(Rendering previousRendering, boolean active)
	{
		super(previousRendering, active);
		
//		setupImageComponents(width, height);
//		ImageTools.copyImage(previousRendering.bufferedImage, bufferedImage);
		isDynamic = true;
	}

	public void compute(float degreeArg, boolean immediate)
	{
		boolean goingInactive = false;
		if (degreeArg < MIN_DESAT)
		{
			degreeArg = 0;
			if (degree < MIN_DESAT)
			{
				debug(".desaturate does nothing. degree=" + degreeArg);
				return;
			}
			else
			{
				goingInactive = true;
				debug("compute(goingInactive isActive=" + isActive + " degree= " + degree + " -> "
						+ degreeArg);
			}
		}
		else if (degreeArg == degree)
			return;

		isActive = !goingInactive;
		// debug("compute(isActive="+isActive+" degree= "+degree+" -> "+degreeArg);
		degree = degreeArg;
		setPending();

		if (immediate)
		{
			if (goingInactive)
				goInactive(true);
			else
				compute(true);
		}
	}
	
	public void compute(float degree)
	{
		this.degree = degree;
		doCompute(true, previousRendering);
		isActive = true;
	}

	public void compute(Rendering inputRendering, Rendering outputRendering)
	{
		ImageTools.copyImage(inputRendering.bufferedImage, outputRendering.bufferedImage);
		
		int[] outPixels = outputRendering.pixels();

		int pixelIndex = 0;
		float oneMinusDegree = 1 - degree;
		for (int j = 0; j < height; j++)
		{
			for (int i = 0; i < width; i++)
			{
				int thisPixel = outPixels[pixelIndex];
				int r = (thisPixel & R) >> 16;
				int g = (thisPixel & G) >> 8;
				int b = thisPixel & B;
				boolean alreadyGray = (r == g) && (r == b) && (g == b);
				if (!alreadyGray)
				{
					// int max = Math.max(Math.max(r,g), b);
					int max = (r > g) ? r : g;
					max = (b > max) ? b : max;
					// float gray = degree * max;
					int gray = (int) (degree * max);
					// fold in a weighted amount of grey
					r = (int) (gray + oneMinusDegree * r);
					g = (int) (gray + oneMinusDegree * g);
					b = (int) (gray + oneMinusDegree * b);
					int alpha = thisPixel & ALPHA;
					outPixels[pixelIndex] = alpha + (r << 16) + (g << 8) + b;
				}
				pixelIndex++;
			}
		}
	}
}
