package ecologylab.textformat;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.types.element.IMappable;

/**
 * Set of variables that control the font style. May contain a name.
 * A Map State of named stles is stored in AWTBridge.
 * @author alexgrau
 */
@simpl_inherit
public class NamedStyle extends ElementState
implements IMappable<String>
{
	public static final int	STROKE_RECTANGLE_FIT	= 0;
	public static final int	STROKE_SMOOTH_FIT			= 1;
	public static final int	STROKE_RECTANGLE			= 2;
	public static final int	STROKE_NONE						= 3;

	/** Name for this given style */
	@simpl_scalar
	protected String				name;

	/** Boolean indicating whether to underline the entire chunk or not */
	@simpl_scalar
	protected boolean				underline;

	/** Integer indicating the size of the chunk font */
	@simpl_scalar
	protected int						fontSize;

	/** Integer indicating the alignment */
	@simpl_scalar
	protected int						alignment;

	/** Integer indicating the faceIndex of the chunk */
	@simpl_scalar
	protected int						faceIndex;

	/** Integer indicating the fontStyle of the chunk. Follows Font constants. */
	@simpl_scalar
	protected int						fontStyle;

	/** Integer that indicates the stroke style for this chunk */
	@simpl_scalar
	protected int						strokeStyle						= Pref.lookupInt("stroke_style", STROKE_RECTANGLE_FIT);

	private long						ormId;

	/**
	 * Set of variables that control the font style. May contain a name.
	 * A Map State of named stles is stored in AWTBridge.
	 */
	public NamedStyle(){}

	
	public NamedStyle(int fontSize)
	{
		this.fontSize			= fontSize;
	}
	
	public NamedStyle(int fontSize, int strokeStyle)
	{
		this.fontSize			= fontSize;
		this.strokeStyle	= strokeStyle;
	}

	/**
	 * Set of variables that control the font style. May contain a name.
	 * A Map State of named stles is stored in AWTBridge.
	 */
	public NamedStyle(String name, boolean underline, int fontSize, int alignment, int faceIndex, int fontStyle)
	{
		this.name = name;
		this.underline = underline;
		this.fontSize = fontSize;
		this.alignment = alignment;
		this.faceIndex = faceIndex;
		this.fontStyle = fontStyle;
	}
	
	/**
	 * Set of variables that control the font style. May contain a name.
	 * A Map State of named stles is stored in AWTBridge.
	 */
	public NamedStyle(String name, boolean underline, int fontSize, int alignment, int faceIndex, int fontStyle, int strokeStyle)
	{
		this( name, underline, fontSize, alignment, faceIndex, fontStyle);
		this.strokeStyle = strokeStyle;
	}


	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean getUnderline() 
	{
		return underline;
	}

	public void setUnderline(boolean underline) 
	{
		this.underline = underline;
	}

	public int getFontSize() 
	{
		return fontSize;
	}

	public void setFontSize(int fontSize) 
	{
		this.fontSize = fontSize;
	}

	public int getAlignment() 
	{
		return alignment;
	}

	public void setAlignment(int alignment) 
	{
		this.alignment = alignment;
	}

	public int getFaceIndex() 
	{
		return faceIndex;
	}

	public void setFaceIndex(int faceIndex) 
	{
		this.faceIndex = faceIndex;
	}

	public int getFontStyle() 
	{
		return fontStyle;
	}

	public void setFontStyle(int fontStyle) 
	{
		this.fontStyle = fontStyle;
	}

	public int getStrokeStyle() 
	{
		return strokeStyle;
	}

	public void setStrokeStyle(int strokeStyle) 
	{
		this.strokeStyle = strokeStyle;
	}
	
	@Override
	public void recycle()
	{
		name = null;
		super.recycle();
	}

	@Override
	public String key()
	{
		return name;
	}


	public long getOrmId()
	{
		return ormId;
	}


	public void setOrmId(long ormId)
	{
		this.ormId = ormId;
	}
}
