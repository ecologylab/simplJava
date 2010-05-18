package ecologylab.textformat;

import java.awt.Color;

import ecologylab.appframework.types.prefs.Pref;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_attribute;
import ecologylab.xml.types.element.Mappable;

/**
 * Set of variables that control the font style. May contain a name.
 * A Map State of named stles is stored in AWTBridge.
 * @author alexgrau
 */
public class NamedStyle extends ElementState
implements Mappable<String>
{
	public static final						int				STROKE_RECTANGLE_FIT 	= 0;
	public static final						int				STROKE_SMOOTH_FIT 		= 1;
	public static final						int				STROKE_RECTANGLE 			= 2;
	public static final						int				STROKE_NONE 					= 3;
	
	/**Name for this given style */
	@xml_attribute protected			String		name;
	
	/**Boolean indicating whether to underline the entire chunk or not*/
	@xml_attribute protected 			boolean		underline;
	
	/**Integer indicating the size of the chunk font*/
	@xml_attribute protected			int				fontSize;
	
	/**Integer indicating the alignment*/
	@xml_attribute protected			int				alignment;
	
	/** Integer indicating the faceIndex of the chunk*/
	@xml_attribute protected			int				faceIndex;
	
	/** Integer indicating the fontStyle of the chunk. Follows Font constants.*/
	@xml_attribute protected			int				fontStyle;
	
	/** Integer that indicates the stroke style for this chunk*/
	@xml_attribute protected			int				strokeStyle = Pref.lookupInt("stroke_style", STROKE_RECTANGLE_FIT);

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

	public String name()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean underline() 
	{
		return underline;
	}

	public void setUnderline(boolean underline) 
	{
		this.underline = underline;
	}

	public int fontSize() 
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

	public int faceIndex() 
	{
		return faceIndex;
	}

	public void setFaceIndex(int faceIndex) 
	{
		this.faceIndex = faceIndex;
	}

	public int fontStyle() 
	{
		return fontStyle;
	}

	public void setFontStyle(int fontStyle) 
	{
		this.fontStyle = fontStyle;
	}

	public int strokeStyle() 
	{
		return strokeStyle;
	}

	public void setStrokeStyle(int strokeStyle) 
	{
		this.strokeStyle = strokeStyle;
	}
	
	public void recycle()
	{
		name = null;
		super.recycle();
	}

	public String key()
	{
		return name;
	}
}
