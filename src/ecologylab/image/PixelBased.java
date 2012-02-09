package ecologylab.image;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.PackedColorModel;
import java.io.IOException;
import java.net.URL;

import ecologylab.concurrent.BasicSite;
import ecologylab.concurrent.Downloadable;
import ecologylab.generic.Colors;
import ecologylab.generic.ConsoleUtils;
import ecologylab.generic.MathTools;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_wrap;

/**
 * Infrastructure to display, keep track of, and manipulate pixel based media.
 * 
 * Works with {@link Rendering Rendering} to implement pipelined
 * image processing transformations on the bits.
 */
@simpl_inherit
public class PixelBased
extends ElementState
implements Downloadable, Colors
{
	
	// /////////////////////////// image transform state ////////////////////////
	public static final int									NO_ALPHA_RADIUS	= -1;

	protected static final DirectColorModel	ARGB_MODEL			= new DirectColorModel(32, 0x00ff0000, 0x0000ff00, 0xff, 0xff000000);

	protected static final PackedColorModel	RGB_MODEL				= new DirectColorModel(24, 0xff0000, 0xff00, 0xff, 0);

	protected static final int[]						ARGB_MASKS			= { 0xff0000, 0xff00, 0xff, 0xff000000, };

	protected static final int[]						RGB_MASKS				= { 0xff0000, 0xff00, 0xff, };

	static final int[]											RGB_BANDS				= { 0, 1, 2 };

	/**
	 * Net location of the whatever might get downloaded. Change from URL to ParsedURL.
	 */
	ParsedURL																purl;

	boolean																	recycled;

	/**
	 * Don't run grab() more than once.
	 */
	boolean																	grabbed;																										;

	/**
	 * like unprocessedRendering, but never part of a chain. image buffer for the completely
	 * unaltered, the virgin. Usually unscaled, but may be scaled down due to maxDimension,
	 */
	@simpl_composite
	@simpl_classes({ AlphaGradientRendering.class, Rendering.class, DesaturatedRendering.class, BlurredRendering.class })
	@simpl_wrap
	Rendering																basisRendering;

	/**
	 * No image processing on this rendering, but if there is scaling to do, this one is scaled.
	 */
//	@simpl_classes({ AlphaGradientRendering.class, Rendering.class, DesaturatedRendering.class, BlurredRendering.class })
//	@simpl_wrap
	protected Rendering											unprocessedRendering;

	@simpl_composite
	AlphaGradientRendering									alphaGradientRendering;

	@simpl_composite
	BlurredRendering												blurredRendering;

	@simpl_composite
	DesaturatedRendering										desaturatedRendering;

	/**
	 * The current Rendering.
	 */
	@simpl_composite
	@simpl_classes({ AlphaGradientRendering.class, Rendering.class, DesaturatedRendering.class, BlurredRendering.class })
	@simpl_wrap
	Rendering																currentRendering;

	public final Object											renderingsLock	= new Object();

	/**
	 * First Rendering in the pipeline (chain) that is dynamic, that is, that changes after 1st
	 * rendered.
	 */
	Rendering																firstDynamic;

	private long														ormId;

	/**
	 * Size of the image, once we know it. Position, as well, if its on screen.
	 */
	// protected Dimension dimension = new Dimension();
	@simpl_scalar
	protected int														width;

	@simpl_scalar
	protected int														height;

	// ////////////////////// for debugging ////////////////////////////////
	@simpl_scalar
	boolean																	scaled;

	public static int												constructedCount, recycledCount;

	public Rendering getFirstDynamic()
	{
		return firstDynamic;
	}

	/////////////////////// constructors //////////////////////////
	public PixelBased()
	{
		
	}
	
	public PixelBased(BufferedImage bufferedImage)
	{
		this(bufferedImage, null);
	}
	public PixelBased(BufferedImage bufferedImage, Dimension maxDimension)
	{
		if (maxDimension == null)
			maxDimension			= new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight());
		
		Rendering rendering	= new Rendering(this, bufferedImage, null, null);

		rendering = scaleRenderingUnderMaxDimension(rendering, rendering.width, rendering.height, maxDimension);

		initializeRenderings(rendering);
 
		constructedCount++;
	}
	
	public void initializeRenderings(Rendering rendering)
	{
		width							= rendering.width;
		height 						= rendering.height;
		basisRendering		= rendering;
		currentRendering	= rendering;
	}


	protected Rendering scaleRenderingUnderMaxDimension(Rendering rendering,
			int width, int height, Dimension maxDimension) 
	{
		if( maxDimension!= null )
		{
			int maxWidth	= maxDimension.width;
			int maxHeight	= maxDimension.height;
			if (width > maxWidth || height > maxHeight)
			{
				if (width / (float) height < maxWidth / (float) maxHeight) 
				{
					width 	= (width*maxHeight + height/2) / height;
					height 	= maxHeight;
				} 
				else 
				{
					height 	= (height*maxWidth + width/2) / width;
					width 	= maxWidth;
				}
				Rendering origRendering	= rendering;
				if (width > 0 && height > 0)
				{ 
					rendering		= rendering.getScaledRendering(width, height);
				}
				origRendering.recycle();
				origRendering = null;	
			}
		}
		return rendering;
	}
//////////////////////from regular Image -> int[] pixels /////////////////////
	/**
	 * If possible, grab the image into memory. This is required for all
	 * image processing operations.
	 * This method creates the unprocessed <code>Rendering</code,
	 * if necessary. This will be true, for example, if this is based on an image stored with
	 * indexed color models.
	 *
	 * @return	true if the image can be grabbed; false if its an animated GIF
	 *		that we're not grabbing, or if the grab fails mysteriously.
	 */
	public boolean acquirePixelsIfNecessary()
	{
		// no need to fixPixels() more than once!
		if (grabbed)
			return true;
		grabbed	= true;

		basisRendering.fixPixels();

		return true;
	}

	//////////////////////// image processing ////////////////////////////////
	/**
	 * Scale the image to a new size, if possible and necessary;
	 *
	 * @param newWidth	new width for the image.
	 * @param newHeight	new height for the image.
	 * @param bufferedImage TODO
	 * @return	true if the operation succeeds (even if no new dimensions).
	 * @return	false if the image is timeBased, or bad.
	 */
	public boolean scaleInitially(int newWidth, int newHeight, BufferedImage bufferedImage)
	{
//		debug("scale() " + dimension.width +","+ dimension.height+" -> " +
//		newWidth +","+ newHeight);
		if (recycled || (basisRendering == null))
			return false;

		if ((bufferedImage.getWidth() != newWidth) || (newHeight != bufferedImage.getHeight()))
		{
			if (unprocessedRendering == null)
			{
				synchronized (renderingsLock)
				{
					width									= newWidth;
					height								= newHeight;
					scaled								= true;
					unprocessedRendering	= basisRendering.getScaledRendering(newWidth, newHeight);
//					basisRendering				= basisRendering.getScaledRendering(newWidth, newHeight);
//					unprocessedRendering 	= new Rendering(basisRendering);
				}
			}
			else
			{
				//ConsoleUtils.obtrusiveConsoleOutput("Resize in scaleInitially()! " + this + " " + dimension+
				//		  " ->"+newWidth+","+newHeight);
				if ((this.alphaGradientRendering != null) && (this.unprocessedRendering.nextRendering != alphaGradientRendering))
				{
					ConsoleUtils.obtrusiveConsoleOutput("GASP! alphaGradient is not in the pipeline chain!");
				}
				this.resize(newWidth, newHeight);
				if ((this.alphaGradientRendering != null) && (alphaGradientRendering.width != width))
					ConsoleUtils.obtrusiveConsoleOutput("EARLY CRAZY! this="+width+" alphaGrad="+this.alphaGradientRendering.width);

			}
		}
		else
		{
			// create new Rendering using the same pixels, DataBufferInt, BufferedImage
			// this is efficient!
			//ConsoleUtils.obtrusiveConsoleOutput("No Scaling! " + this + " " + dimension);
			if (unprocessedRendering == null)
				unprocessedRendering	= new Rendering(basisRendering);
			//unprocessedRendering		= unscaledRendering.getScaledRendering(newWidth, newHeight);
		}

		this.setCurrentRendering(unprocessedRendering);
		return true;
	}
	public void dontScaleInitially()
	{
		if (!recycled && (basisRendering != null))
		{
			// create new Rendering using the same pixels, DataBufferInt, BufferedImage
			// this is efficient!
			if (unprocessedRendering == null)
				unprocessedRendering	= new Rendering(basisRendering);
			this.setCurrentRendering(unprocessedRendering);
		}
	}
	public boolean resize(int newWidth, int newHeight)
	{
		//       debug("scale() " + extent.width +","+ extent.height+" -> " +
		//	     newWidth +","+ newHeight);
		if (recycled)
			return false;

		if ((newWidth != width) || (newHeight != height))
		{
			if (unprocessedRendering == null)
			{
				unprocessedRendering = new Rendering(basisRendering);
				this.setCurrentRendering(unprocessedRendering);
			}
			
			synchronized (renderingsLock)
			{
				width				= newWidth;
				height			= newHeight;
				scaled			= true;
				unprocessedRendering.resize(newWidth, newHeight, basisRendering);
//				if (alphaGradientRendering != null)
//				{
//					unprocessedRendering.computeNext();
//				}
			}
		}
		return true;
	}
	
	public void setupRandomAlphaGradient(float radiusFactor)
	{
		// now (after probability thresholding), push it up!
		radiusFactor	= MathTools.bias(radiusFactor, .7f);
		int shorter	= (width < height) ? width : height;
		int alphaRadius	= (int) ((shorter / 2) * radiusFactor);
		int alphaMinimum	= ((int) (MathTools.random(.4f) * (float) R)) & R;
		
		alphaGradient(alphaRadius, alphaMinimum);
	}
	/**
	 * Builds an alpha gradient, or feathered mask.
	 * 
	 * Acts on buffer in the <code>pixels</code> slot.
	 * 
	 * @param	radius		area -- pixels of border to mask
	 * @param	minAlpha	lowest alpha setting in mask -- represented in
	 *				red, instead of alpha space, to avoid
	 *				signed arithmetic problems; will shift up later
	 */
	public boolean alphaGradient(int radius, int minAlpha)
	{
		if (recycled)
			return false;
		
		if (unprocessedRendering == null)
			unprocessedRendering = new Rendering(basisRendering);
		
		boolean active	= (radius != NO_ALPHA_RADIUS);

		if (alphaGradientRendering == null)
			alphaGradientRendering = new AlphaGradientRendering(unprocessedRendering, active);
		
		if (active)
		{
			alphaGradientRendering.compute(radius, minAlpha);
			Rendering last		= alphaGradientRendering.lastActive();
			if (last == alphaGradientRendering)
				//alphaGradientState.hookup();
				this.setCurrentRendering(alphaGradientRendering);
			else
				//	     last.compute();
				alphaGradientRendering.computeNext();
		}
		if (alphaGradientRendering.width != width)
			ConsoleUtils.obtrusiveConsoleOutput("CRAZY! this="+width+" alphaGrad="+this.alphaGradientRendering.width);
		return true;
	}
	
	public boolean alphaGradient(int radius, int minAlpha, int minAlphaInt)
	{
		alphaGradientRendering.setMinAlphaInt(minAlphaInt);
		return alphaGradient(radius, minAlpha);
	}
	
	/**
	 * @return In alpha gradient blending, the distance from the perimeter,
	 * inside which opacity is complete.
	 */
	public double alphaRadius()
	{
		return (alphaGradientRendering != null) ? alphaGradientRendering.radius : 0;
	}
	/**
	 * @return the minimum value opacity mask used for alpha gradient blending.
	 * [0, 255]
	 */
	public int minAlpha()
	{
		return (alphaGradientRendering != null) ? alphaGradientRendering.minAlpha : 0;
	}
	public boolean hasAlphaGradient()
	{
		return (alphaGradientRendering != null) && alphaGradientRendering.isActive;
	}
	public void blur2D(int blurWidth, int blurHeight, boolean immediate)
	{
		if (recycled || (unprocessedRendering == null))
			return;
		synchronized (renderingsLock)
		{
			if (blurredRendering == null)
			{
				if (desaturatedRendering == null)
					blurredRendering = new BlurredRendering(lastStatic(), true);
				else
					blurredRendering = new BlurredRendering(lastStatic(), desaturatedRendering, 
							true);
				firstDynamic = blurredRendering;
			}

			blurredRendering.compute(blurWidth, blurHeight, false);
			blurredRendering.goActive(immediate);

			/*	  blurState.goActive();
			  blurState.compute(blurWidth, blurHeight, immediate);
			  if (immediate && blurState.isLastActive())
			  blurState.hookup();
			 */
		}
	}
	public void noAlphaGradient()
	{
		this.setCurrentRendering(unprocessedRendering);
		unprocessedRendering.recycleRenderingChain(false);
		unprocessedRendering.nextRendering = null;
		alphaGradientRendering 	= null;
		desaturatedRendering 		= null;
		//goInactive(alphaGradientRendering);
	}
	public boolean restoreAlphaGradient()
	{
		boolean result = alphaGradientRendering != null;
		if (result)
		{
			alphaGradientRendering.fixPreviousRendering(unprocessedRendering);	// kludge pipeline if necessary
			goActive(alphaGradientRendering, true);
		}
		return result;
	}
	public void noBlur2D()
	{
		goInactive(blurredRendering);
	}
	public void blur2D(float degree, boolean immediate)
	{
		// for more perceptibly linear response, push degree toward 0
		//      degree		= MoreMath.bias(degree, .4f); 
		int blurWidth	= (int) ((float) width  * .25f * degree);
		int blurHeight	= (int) ((float) height * .25f * degree);
		blur2D(blurWidth, blurHeight, immediate);
	}
	/**
	 * @param degree	degree of desaturation
	 */
	public void desaturate(float	degree, boolean immediate)
	{
		if (show(1))
			debug("desaturate("+degree+") " + immediate);
		if (recycled || (unprocessedRendering == null))
			return;
		if (degree < .07f)
			return;

		Rendering blurredRendering	= this.blurredRendering;
		synchronized (renderingsLock)
		{
			if (desaturatedRendering == null)
			{
				if (blurredRendering == null)
				{
					desaturatedRendering
					= new DesaturatedRendering(lastStatic(), true);
					//		  = new DesaturateState(this, alphaGradientState, true);
					firstDynamic= desaturatedRendering;
				}
				else
				{
					desaturatedRendering
					= new DesaturatedRendering(blurredRendering, true);
				}
			}
			if ((blurredRendering != null) && (blurredRendering.previousRendering == null))
				throw new RuntimeException(this + " changed to NULL previousRendering?????);by new DesaturatedRendering");

			desaturatedRendering.compute(degree);
			//desaturatedRendering.goActive(immediate);
			setCurrentRendering(desaturatedRendering);
		} 
	}
	public void noDesaturate()
	{
		goInactive(desaturatedRendering);
	}


	/////////////////////// Rendering management //////////////////////////
	void goInactive(Rendering rendering)
	{
		if (!recycled && (rendering != null))
			rendering.goInactive();
	}
	void goActive(Rendering rendering, boolean immediate)
	{
		if (!recycled && (rendering != null))
			rendering.goActive(immediate);
	}
	boolean	useNoProc;
	public void setUseNoProc(boolean value)
	{
		useNoProc	= value;
	}

	/////////////////////// Images and MemoryImageSource //////////////////////////

	public void setCurrentRendering(Rendering rendering)
	{
		if (!recycled)
			this.currentRendering	= rendering;
	}
////////////////////////sundry services ////////////////////////////////
	public Cursor createCustomCursor(Point point, String name)
	{
		return createCustomCursor(basisRendering.bufferedImage, point, name);
	}

	public static Cursor createCustomCursor(BufferedImage bufferedImage, Point point, String name)
	{
		Toolkit kit		= Toolkit.getDefaultToolkit();
		return kit.createCustomCursor(bufferedImage, point, name);
	}

////////////////////////rendering ////////////////////////////////

	Rendering lastStatic()
	{
		return (alphaGradientRendering == null) ? unprocessedRendering : alphaGradientRendering;
	}
	/**
	 * rendering
	 */
	public void paint(Graphics g, int x, int y)
	{
		if (!recycled)
		{
			Rendering rendering = useNoProc ? this.unprocessedRendering : this.currentRendering;
			if (rendering != null)
				rendering.paint(g, x, y);
			//else
			//debug("ERROR: trying to render, but no current rendering");
		}
	}
	public void paint(Graphics2D g2, int x, int y, AffineTransform a)
	{
		if (!recycled)
		{
			Rendering rendering = useNoProc ? this.unprocessedRendering : this.currentRendering;
			if (rendering != null)
				rendering.paint(g2, x, y, a);
			//else
			//debug("ERROR: trying to render, but no current rendering");
		}
	}

////////////////////////utilities ////////////////////////////////
	public String toString()
	{ 
		String addr = "["+ ((purl==null) ? "no purl - " + this.getClassSimpleName() : purl.toString())+"]";
		String dim  = "[" + width+"x"+height + "] ";
		return getClassSimpleName(this) +  addr + dim;
	}
	public String errorMessage()
	{
		String purlString = (purl == null) ? "null" : purl.toString();
		return "** " + getClassSimpleName() + " can't access content: " + purlString;
	}

	public static String hex(int h)
	{
		return Integer.toHexString(h) + " ";
	}
	
	/**
	 * Encourage resource relamation -- flush <code>Image</code> and 
	 * release <code>Rendering</code> buffers..
	 */
	public void recycle()
	{
		synchronized (renderingsLock)
		{
			if (!recycled)
			{
				//debug("recycle()");
				recycled			= true;

				purl				= null;

				if (unprocessedRendering != null)
				{
					unprocessedRendering.recycleRenderingChain(true);
					unprocessedRendering.recycle(); // also calls chain of Renderings
					unprocessedRendering		= null;
				}

				if (currentRendering != null)
				{
					currentRendering.recycle();
					currentRendering	= null;
				}
				
				if (firstDynamic != null)
				{
					firstDynamic.recycle();
					firstDynamic		= null;
				}
				
				if (basisRendering != null)
				{
					basisRendering.recycle();
					basisRendering	= null;
				}

				if (this.alphaGradientRendering != null)
				{
					alphaGradientRendering.recycle();
					alphaGradientRendering	= null;
				}
				if (this.desaturatedRendering != null)
				{
					desaturatedRendering.recycle();
					desaturatedRendering	= null;
				}
				if (this.blurredRendering != null)
				{
					blurredRendering.recycle();
					blurredRendering	= null;
				}
				recycledCount++;
			}
		}
	}

	/**
	 * Debugging info about the status of the image.
	 */   
	public String downScaled()
	{
		String scale= scaled ? " scaled" : "";
		return scale;
	}

	/**
	 * A shorter string for displaing in the modeline for debugging, and
   in popup messages.
	 */
	public static String shortURL(URL u)
	{
		String s;
		if (u == null)
			s	= "Img null";
		else
		{
			String file	= u.getFile();
			s	= u.getHost() + "/.../" + 
			file.substring(file.lastIndexOf('/') + 1);
		}
		return s;
	}
	public String shortURL()
	{
		return shortURL(purl.url());
	}

	/* return ParsedURL */
	public ParsedURL location()
	{
		return purl;
	}

	/**
	 * Change alpha values throughout the pixels in the currentRendering.
	 * Multiply existing alpha by the value passed in.
	 * This means that transparent areas stay transparent (newAlpha * 0),
	 * while opaque areas get this level of transparency (newAlpha * 0xff).
	 * @param scaleFactor TODO
	 */
	public void scaleAlpha(float scaleFactor)
	{
		currentRendering.scaleAlpha(scaleFactor);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public boolean timedOut()
	{
		return false;
	}
	public boolean shouldDownloadBiggerVersion() {
		/* if originalDimension exists, we scaled the picture down when first downloading it.
		 so if the user wants to make it bigger than the basisRendering, 
		 we should re-download it and not scale it down */
		return (/* originalDimension != null && */
				width > basisRendering.width ||
				height > basisRendering.height);
	}
	
	public BufferedImage bufferedImage()
	{
		return (currentRendering != null) ? currentRendering.bufferedImage : null;
	}

	public BufferedImage basisBufferedImage()
	{
		return basisRendering.bufferedImage;
	}

	/**
	 * Do nothing, but implement Downloadable.
	 */
	public void handleIoError(Throwable e)
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Do nothing, but implement Downloadable.
	 */
	public boolean isRecycled()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Do nothing, but implement Downloadable.
	 */
	public void performDownload() throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	public BasicSite getSite()
	{
		return null;
	}
	
  /**
   * 
   * @return	What to tell the user about what is being downloaded.
   */
  public String message()
  {
  	return "image " + purl;
  }

	public void updateRenderings(BufferedImage bufferedImage)
	{
		if (basisRendering != null)
		{
			basisRendering.bufferedImage = bufferedImage;
			acquirePixelsIfNecessary();	// may have side effects on the bufferedImage we use
			bufferedImage	= basisRendering.bufferedImage;
//			if (unprocessedRendering != null)
//				unprocessedRendering.
			scaleInitially(width, height, bufferedImage);
			restoreAlphaGradient();
		}
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public long getOrmId()
	{
		return ormId;
	}

	public void setOrmId(long ormId)
	{
		this.ormId = ormId;
	}

	public Rendering getBasisRendering()
	{
		return basisRendering;
	}

	public void setBasisRendering(Rendering basisRendering)
	{
		this.basisRendering = basisRendering;
	}

	public Rendering getUnprocessedRendering()
	{
		return unprocessedRendering;
	}

	public void setUnprocessedRendering(Rendering unprocessedRendering)
	{
		this.unprocessedRendering = unprocessedRendering;
	}

	public AlphaGradientRendering getAlphaGradientRendering()
	{		
		if (alphaGradientRendering == null)
			alphaGradientRendering = new AlphaGradientRendering(unprocessedRendering, true);
		
		return alphaGradientRendering;
	}

	public void setAlphaGradientRendering(AlphaGradientRendering alphaGradientRendering)
	{
		this.alphaGradientRendering = alphaGradientRendering;
	}

	public BlurredRendering getBlurredRendering()
	{
		return blurredRendering;
	}

	public void setBlurredRendering(BlurredRendering blurredRendering)
	{
		this.blurredRendering = blurredRendering;
	}

	public DesaturatedRendering getDesaturatedRendering()
	{
		return desaturatedRendering;
	}

	public void setDesaturatedRendering(DesaturatedRendering desaturatedRendering)
	{
		this.desaturatedRendering = desaturatedRendering;
	}

	public boolean isScaled()
	{
		return scaled;
	}

	public void setScaled(boolean scaled)
	{
		this.scaled = scaled;
	}

	public Rendering getCurrentRendering()
	{
		return currentRendering;
	}
	

	@Override
	public boolean isImage()
	{
		return true;
	}

}
