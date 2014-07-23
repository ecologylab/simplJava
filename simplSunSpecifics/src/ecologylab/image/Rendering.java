/*
 * Copyright 2002 by Texas A&M.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package ecologylab.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;

import ecologylab.generic.Colors;
import ecologylab.generic.ImageTools;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_classes;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_wrap;

/**
 * The basic unit of an image processing rendering pipeline;
 * <code>Rendering</code> encapsalates a particular graphical state for a
 * {@link PixelBased PixelBased} -- either initial state, or a filtered state --
 * and includes references to the next and previous states in the pipeline.
 * Thus, these objects form a
 * chain of such states that builds a pipeline of image processing operations.
 */
@simpl_inherit
public class Rendering
extends ElementState<PixelBased>
implements Colors
{
	/**
	 * Global count of all pending <code>ImageState</code>s that need to have their pixels recomputed.
	 */
	static int				renderingToDo;

	/**
	 * Global statistic for debugging memory leaks.
	 */
	public static int	flushCount;

	@simpl_scalar
	protected boolean	isActive;

	/**
	 * true if this <code>ImageState</code> needs to have its pixels recomputed.
	 */
	private boolean		isPending;

	/**
	 * has a non-empty compute op, that gets called periodically, rather than just once for init.
	 */
	@simpl_scalar
	boolean						isDynamic;

	/**
	 * Previous state in the Rendering pipeline.
	 */
	@simpl_composite
	@simpl_classes({ AlphaGradientRendering.class, Rendering.class, DesaturatedRendering.class, BlurredRendering.class })
	@simpl_wrap
	Rendering					previousRendering;

	/**
	 * Next state in the Rendering pipeline.
	 */
	@simpl_composite
	@simpl_classes({ AlphaGradientRendering.class, Rendering.class, DesaturatedRendering.class, BlurredRendering.class })
	@simpl_wrap
	Rendering					nextRendering;

	@simpl_composite
	PixelBased				pixelBased;

	@simpl_scalar
	int								width;

	@simpl_scalar
	int								height;

	/**
	 * where pixels resulting from this operation live
	 */
	int[]							pixels;

	/**
	 * DataBuffer that refers to the pixels.
	 */
	DataBufferInt			dataBuffer;

	/**
	 * BufferedImage that refers to the DataBuffer.
	 */
	BufferedImage			bufferedImage;

	private long			ormId;

	public Rendering()
	{
		
	}
	
	Rendering(PixelBased pixelBased, BufferedImage bufferedImage, DataBufferInt dataBuffer, int[] pixels)
	{
		this.pixelBased		= pixelBased;
		this.bufferedImage	= bufferedImage;
		if (dataBuffer == null)
		{
			if (bufferedImage.getSampleModel() instanceof SinglePixelPackedSampleModel)
			{
				Raster raster	= bufferedImage.getRaster();
				DataBuffer db	= raster.getDataBuffer();
				if (db instanceof DataBufferInt)
				{
//					debug("EUREKA: Derived dataBuffer and pixels from BufferedImage!");
					dataBuffer	= (DataBufferInt) db;
					pixels		= dataBuffer.getData();
				}
			}
		}
		this.dataBuffer		= dataBuffer;
		this.pixels			= pixels;
		this.width			= bufferedImage.getWidth();
		this.height			= bufferedImage.getHeight();
		this.isActive		= true;
	}
	public Rendering(Rendering mommy)
	{
		this.pixelBased		= mommy.pixelBased;
		this.bufferedImage	= mommy.bufferedImage;
		this.dataBuffer		= mommy.dataBuffer;
		this.pixels			= mommy.pixels;
		this.width			= mommy.width;
		this.height			= mommy.height;
		this.isActive		= true;
	}
	public Rendering(Rendering previousRendering, boolean active)
	{
		this.pixelBased	= previousRendering.pixelBased;
		width						= pixelBased.width;
		height					= pixelBased.height;
		isActive				= active;
		
		previousRendering.nextRendering	= this;
		this.previousRendering	= previousRendering;
	}
	public Rendering(Rendering previousRendering, Rendering nextRendering,
			boolean active)
	{
		this(previousRendering, active);
		if (nextRendering != null)
		{
			nextRendering.previousRendering	= this;
			this.nextRendering	= nextRendering;
		}	
	}
	
	/**
	 * If we didnt get a DataBufferInt and pixels when we were constructed,
	 * copy to get some now.
	 * <p/>
	 * If we did, do nothing.
	 */
	void fixPixels()
	{
		if ((dataBuffer == null) || (pixels == null))
		{
			BufferedImage oldImage	= this.bufferedImage;				
			// create pixels and DataBufferInt, but not WritableRaster and BufferedImage
			this.setupImageComponents(oldImage.getWidth(), oldImage.getHeight(), false);
			// create new WritableRaster and BufferedImage without flushing the old BufferedImage
			// cause we need to copy from it
			BufferedImage newImage	= createNewBufferedImage(dataBuffer);			
			this.bufferedImage		= newImage;
			
			debug("fixPixels() copying!! " + pixelBased().location());
			ImageTools.copyImage(oldImage, newImage);
			
			// now that the copy is done, we can do the flush()!
			flush(oldImage);
			oldImage = null;
		}
	}
	/**
	 * @param bImage
	 */
	private void flush(BufferedImage bImage) 
	{
		bImage.flush();
		new WeakReference<BufferedImage>(bImage, null);
		flushCount++;
	}
	/**
	 * Resize what is expected to be a chain.
	 * 
	 * Use only on noProcStaate -- first in chain.
	 * Sets up parameters, and clears pixels so it will
	 * later get recomputed. in forward chained Rendering.
	 * Then performs a scaling on @param unscaledRendering pixels, and sets
	 * pixels here to this new, actually scaled pixel array.
	 *
	 * For chained states. Sets up parameters, and clears pixels so it will
	 * later get recomputed. in this and all states..
	 */
	void resize(int newWidth, int newHeight, Rendering unscaledRendering)
	{
		synchronized (pixelBased.renderingsLock)
		{
			// assumption -- !((newWidth == width) && (newHeight == height))
			
			this.resizePipelineImageComponents(newWidth, newHeight);
			
			ImageTools.scaleAndCopyImage(newWidth, newHeight, 
										 unscaledRendering.bufferedImage, bufferedImage);
			
			if (nextRendering != null)
				nextRendering.compute(this);

			clearPending();
		}
	}
	/**
	 * To initially create a scaled version of this Rendering.
	 * 
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	Rendering getScaledRendering(int newWidth, int newHeight)
	{
		synchronized (pixelBased.renderingsLock)
		{
			int[] scaledPixels	= new int[newWidth * newHeight];
			int size						= newWidth * newHeight;
			DataBufferInt scaledDataBuffer	= new DataBufferInt(scaledPixels, size);
			BufferedImage scaledBImage			= createNewBufferedImage(scaledDataBuffer, newWidth, newHeight);
		
			// if you want to clone everything, this is reasonably fast
			// even if there's no scaling
			ImageTools.scaleAndCopyImage(newWidth, newHeight, bufferedImage, scaledBImage);
			
			Rendering result		= new Rendering(pixelBased, scaledBImage, scaledDataBuffer, scaledPixels);
			result.setPreviousRendering(this);
//			recycle();

			return result;
		}
	}
	protected BufferedImage createNewBufferedImage(DataBufferInt scaledDataBuffer, int width, int height)
	{
		return createNewBufferedImage( scaledDataBuffer, this.bufferedImage, width, height);
	}
	protected BufferedImage createNewBufferedImage(DataBufferInt scaledDataBuffer)
	{
		return createNewBufferedImage( scaledDataBuffer, this.bufferedImage, width, height);
	}
	/**
	 * Use the DataBuffer passed in, the SampleModel from the current WritableRaster,
	 * and the ColorModel from the current BufferedImage to create a new BufferedImage.
	 * 
	 * @param scaledDataBuffer
	 * @return
	 */
	protected BufferedImage createNewBufferedImage(DataBufferInt scaledDataBuffer, BufferedImage referenceImage, int width, int height)
	{
		SampleModel sm					= getSampleModel(referenceImage, width, height);
		WritableRaster	wr			= Raster.createWritableRaster(sm, scaledDataBuffer, null);
		ColorModel	cm					= getColorModel(referenceImage);
		return new BufferedImage(cm, wr, false, null);
	}
	protected ColorModel getColorModel(BufferedImage referenceImage)
	{
		switch(imageType(referenceImage))
		{
		case BufferedImage.TYPE_INT_RGB: 
			return PixelBased.RGB_MODEL;
		
		case BufferedImage.TYPE_INT_ARGB:
		default:
			return PixelBased.ARGB_MODEL;
		}
	}
	protected SampleModel getSampleModel(BufferedImage referenceImage, int width, int height)
	{
		int[] bitMasks;
		switch(imageType(referenceImage))
		{
		case BufferedImage.TYPE_INT_RGB: 
			bitMasks = PixelBased.RGB_MASKS;
			break;
		case BufferedImage.TYPE_INT_ARGB:
		default:
			bitMasks = PixelBased.ARGB_MASKS;
		}
		return new SinglePixelPackedSampleModel(DataBuffer.TYPE_INT, width, height, bitMasks);
	}
	private int imageType(BufferedImage referenceImage)
	{
		return (referenceImage != null) ? referenceImage.getType() : -1;
	}
	
	/*
	protected ColorModel getColorModel(BufferedImage referenceImage)
	{
		return referenceImage.getColorModel();
	}
	protected SampleModel getSampleModel(BufferedImage referenceImage)
	{
		return referenceImage.getSampleModel();
	}
	*/
	public AffineTransformOp createScaleOp(int newWidth, int newHeight)
	{
		return createScaleOp(newWidth, newHeight, width, height);
	}
	public static AffineTransformOp createScaleOp(int newWidth, int newHeight, int oldWidth, int oldHeight)
	{
		float widthFactor	= ((float) newWidth) / ((float) oldWidth);
		float heightFactor	= ((float) newHeight) / ((float) oldHeight);
		
		AffineTransform scaleTransform	= AffineTransform.getScaleInstance(widthFactor, heightFactor);
		AffineTransformOp scaleOp		= new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
		return scaleOp;
	}
	protected void resizePipelineImageComponents(int width, int height)
	{
		resizeImageComponents(width, height, true);
		Rendering next		= nextRendering;
		if (next != null)
			next.resizePipelineImageComponents(width, height);
	}
	protected void setupPipelineImageComponents(int width, int height)
	{
		setupImageComponents(width, height, true);
		Rendering next		= nextRendering;
		if (next != null)
			next.setupPipelineImageComponents(width, height);
	}
	protected void setupImageComponents(int width, int height)
	{
		setupImageComponents(width, height, true);
	}
	protected void resizeImageComponents(int width, int height, boolean createBufferedImage)
	{
		setupImageComponents(width, height, createBufferedImage);
	}
	/**
	 * Create an array of pixels, a DataBufferInt, and a BufferedImage at this size.
	 * Set our instance variables to refer to these new ones.
	 * <p/>
	 * You probably want to save a copy of your reference to any existing BufferedImage
	 * before calling this, so you can flush() it (after calling this method) when youre ready to.
	 * @param width
	 * @param height
	 * @param createBufferedImage	-- If true, create a new BufferedImage, and refer to it. flush() the old one.
	 */
	protected void setupImageComponents(int width, int height, boolean createBufferedImage)
	{
		this.width	= width;
		this.height	= height;
		int size	= width * height;
		pixels		= new int[size];
		dataBuffer	= new DataBufferInt(pixels, size);
		if (createBufferedImage)
		{
			BufferedImage oldImage		= this.bufferedImage;
			if (oldImage != null)
			{
				flush(oldImage);
				oldImage	= null;
				this.bufferedImage = null;
			}
			
			BufferedImage newImage		= createNewBufferedImage(dataBuffer, width, height);
			bufferedImage							= newImage;
		}
	}
	public void fill(int pixel)
	{
		if (pixels == null)
			pixels	= new int[width*height];
		
		fill(pixels, pixel);
		print("\t");
		for (int i=(pixels.length-10); i< pixels.length; i++)
			print(Integer.toHexString(pixels[i]) + " ");
		println("");
	}
	public void fill(int[] outPixels, int pixel)
	{
		for (int i=0; i<outPixels.length; i++)
			outPixels[i]	= ALPHA + pixel;
	}
	public void compute(Rendering predecessor)
	{
		compute(true, predecessor);
	}
	boolean doCompute(boolean recomputedPredecessor, Rendering inputRendering)
	{
		boolean result	= false;
		if (isPending || recomputedPredecessor)
		{
			if (pixels == null)
			{
				setupImageComponents(width, height);
			}
			
			compute(inputRendering, this);
			clearPending();
			result			= true;
		}
		return result;
	}
	public void computeNext()
	{
		for (Rendering next = nextRendering; (next != null); next = next.nextRendering)
			next.isPending		= true;
		
		if (nextRendering != null)
			nextRendering.compute(true);
	}
	public void compute(boolean recomputedPredecessor)
	{
//		debug("compute("+recomputedPredecessor);
		Rendering prevousRendering	= previousActiveRendering();
		int[] prevPixels	= prevousRendering.pixels();
		if (prevPixels == null)
		{
			Rendering prevPrev	= prevousRendering.previousRendering;
			
			debug("compoute(???EEECH previous="+prevousRendering+" previous.pixels="+
					prevousRendering.pixels + " previous.pixels()=" + prevPixels+" "+
					" prevPrev=" + prevPrev + " " +
					((prevPrev == null) ? "" : prevPrev.pixels.toString()));
		}
		compute(recomputedPredecessor, prevousRendering);
	}
	public void compute(boolean recomputedPredecessor, Rendering inputRendering)
	{
//		debug(3, "compute("+recomputedPredecessor+", "+inPixels + 
//		" firstDynamic=" + pixelBased.firstDynamic);
		boolean computed	= false;
		if ((isDynamic || recomputedPredecessor) && (isActive || isPending))
		{
			computed	= doCompute(recomputedPredecessor, inputRendering);
		}
		if (nextRendering != null)
		{
			nextRendering.compute((computed || recomputedPredecessor), 
					(computed ? this : inputRendering));
		}
		else		   // last in chain
		{
			if (computed)
			{
				//TODO i think this line is unnecessary
				pixelBased.setCurrentRendering(this);
			}
		}
	}
	public void compute(Rendering inputRendering, Rendering outputRendering)
	{
	}

	boolean isRendered()
	{
		return pixelBased.currentRendering == this;
	}
	/**
	 * @return	the <code>ImageState</code> that will be rendered -- iff
	 * it <code>isActive</code>.
	 */
	Rendering lastInChain()
	{
		return (nextRendering == null) ? this : nextRendering.lastInChain();
	}
	/**
	 * @return	the last active <code>ImageState</code> -- the one
	 * that should be rendered.
	 */
	Rendering lastActive()
	{
		return ((nextRendering != null) && nextRendering.isActive) ?
				nextRendering.lastInChain() : this;
	}
	boolean isLastActive()
	{
		return lastActive() == this;
	}
	void goActive(boolean immediate)
	{
		boolean wasActive		= isActive;
		isActive			= true;
		if (!wasActive || immediate)
		{
			setPending();
			if (immediate)
			{
				compute(true);
				
				if (isLastActive())
					pixelBased.setCurrentRendering(this);
			}
		}
	}
	void goInactive(boolean wasActive)
	{
		isActive			= false;
		if (wasActive && (pixelBased != null))
		{
//			debug("pixelBased.renderedImageState="+pixelBased.renderedImageState);
			if (pixelBased.currentRendering == this)
			{
				//previousActiveState().hookup();
				pixelBased.setCurrentRendering(this.previousActiveRendering());
			}
			else if (nextRendering != null)
			{
				nextRendering.recursiveSetPending();
				nextRendering.compute(true);
			}
			
			if (isDynamic)
			{
				synchronized (pixelBased.renderingsLock)
				{
					if (this == pixelBased.firstDynamic)
						pixelBased.firstDynamic	=
							(previousRendering.isDynamic && previousRendering.isActive)
							? previousRendering : null;
				}
			}
		}
	}
	void goInactive()
	{
		goInactive(isActive);
	}
	int[] pixels()
	{
		return isActive ? pixels : previousActiveRendering().pixels();
	}
	
	Rendering previousActiveRendering()
	{
		if (previousRendering == null)
			debug("EEECH! previousState = null; nextState="+nextRendering);
//		return (previousState == null) ? pixelBased.noProcState :
		return previousRendering.isActive ?
				previousRendering : previousRendering.previousActiveRendering();
		
	}
	void remove()
	{
		if (nextRendering != null)
			nextRendering.previousRendering	= previousRendering;
		if (previousRendering != null)
			previousRendering.nextRendering	= null;
	}
	void recursiveSetPending()
	{
		setPending();
		if (nextRendering != null)
			nextRendering.setPending();
	}
	synchronized void setPending()
	{
		if (!isPending)
		{
			isPending	= true;
			renderingToDo++;
		}
	}
	synchronized void clearPending()
	{
		if (isPending)
		{
			isPending	= false;
			renderingToDo--;
		}
	}
	
	/**
	 * rendering
	 */
	public void paint(Graphics g, int x, int y)
	{
		if (bufferedImage != null)
		{
			synchronized (pixelBased.renderingsLock)
			{
				//debug("painting at "+x+","+y);
				g.drawImage(bufferedImage, x, y, null);
			}
		}
		else
			debug("ERROR: paint() bufferedImage == null!");
	}
	public void paint(Graphics2D g2, int x, int y, AffineTransform a)
	{
		if (bufferedImage != null)
		{
			synchronized (pixelBased.renderingsLock)
			{
				g2.translate(x,y);
				g2.drawImage(bufferedImage, a, null);
				g2.translate(-x,-y);
			}
		}
		else
			debug("ERROR: paint() bufferedImage == null!");
	}

	public static int renderingToDo()
	{
		return renderingToDo;
	}
	public String toString()
	{
		return (super.toString() +"["+pixelBased+"]");
	}
	/**
	 * Free resources associated with just this.
	 * NB: *does not* cycle through the rendering pipeline.
	 */
	public void recycle()
	{
		clearPending();
		
		if (bufferedImage != null)
		{
			flush(bufferedImage);
			bufferedImage			= null;
		}
		this.dataBuffer				= null;
		this.pixels					= null;
		this.pixelBased				= null;
		
		previousRendering = null;
		nextRendering = null;
	}
	
	void recycleRenderingChain(boolean recycleThis)
	{
		Rendering temp = null;
		for (Rendering prev = lastInChain(); (prev != null); prev = temp)
		{
			temp 						= prev.previousRendering;
			boolean recycle = recycleThis || this != prev;
			if (recycle)
				prev.recycle();
		}
	}
	
	/**
	 * Change alpha values throughout the pixels.
	 * Multiply existing alpha by the value passed in.
	 * This means that transparent areas stay transparent (newAlpha * 0),
	 * while opaque areas get this level of transparency (newAlpha * 0xff).
	 * 
	 * @param scaleFactor	Alpha scale factor: target level of alpha for previously opaque areas.
	 */
	public void scaleAlpha(float scaleFactor)
	{
		final int length	= this.pixels.length;
		final int maxAlpha	= ((int) (scaleFactor * 255.0f)) << 24;
		for (int i=0; i<length; i++)
		{
			int pixel	= pixels[i];
			int alpha	= (pixel & ALPHA);
			if (alpha != 0)
			{	// if not previously fully transparent
				if (alpha == ALPHA)
				{	// if previously opaque
					alpha	= maxAlpha;
				}
				else
				{	// some intermediate alpha value to scale -- 
					// can store in a lookup table if optimizing further matters
					alpha >>= 24;
					alpha  &= 0xff;
					alpha	= (int) (scaleFactor * ((float) alpha));
					alpha <<= 24;
				}
				pixel		= alpha | (pixel & RGB);
				pixels[i]	= pixel;
				
			}
		}
	}

	public void setIsActive(boolean isActive)
	{
		this.isActive = isActive;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public PixelBased getPixelBased()
	{
		return pixelBased;
	}

	public void setPixelBased(PixelBased pixelBased)
	{
		this.pixelBased = pixelBased;
	}

	public long getOrmId()
	{
		return ormId;
	}

	public void setOrmId(long ormId)
	{
		this.ormId = ormId;
	}

	public boolean getIsDynamic()
	{
		return isDynamic;
	}

	public void setIsDynamic(boolean isDynamic)
	{
		this.isDynamic = isDynamic;
	}

	public Rendering getPreviousRendering()
	{
		return previousRendering;
	}

	public void setPreviousRendering(Rendering previousRendering)
	{
		this.previousRendering = previousRendering;
	}

	public boolean fixPreviousRendering(Rendering newPreviousRendering)
	{
		boolean result = !this.previousRendering.hasPixels();
		if (result)
		{
			this.previousRendering = newPreviousRendering;
			newPreviousRendering.nextRendering	= this;
		}
		return result;
	}

	public Rendering getNextRendering()
	{
		return nextRendering;
	}

	public void setNextRendering(Rendering nextRendering)
	{
		this.nextRendering = nextRendering;
	}

	public boolean getIsActive()
	{
		return isActive;
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	public PixelBased pixelBased()
	{
		PixelBased result	= pixelBased;
		if (result == null)
		{
			result					= parent();
			this.pixelBased	= result;
		}
		return result;
	}
	
	boolean hasPixels()
	{
		return pixels != null;
	}
}
