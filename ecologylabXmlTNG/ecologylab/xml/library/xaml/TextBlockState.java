/**
 * 
 */
package ecologylab.xml.library.xaml;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * ecologylab.xml representation of the TextBlock WPF element for translating to XAML.
 * 
 * @author awebb
 *
 */
@xml_tag("TextBlock")
public class TextBlockState extends PanelChildState
{
	public static final String WRAP 		= "Wrap";
	public static final String ITALIC 		= "Italic";
	public static final String BOLD			= "Bold";
	public static final String UNDERLINE 	= "Underline";
	
	@xml_attribute @xml_tag("FontSize") 		int 	fontSize;
	@xml_attribute @xml_tag("FontFamily") 		String 	fontFamily;
	@xml_attribute @xml_tag("TextWrapping")		String 	textWrap;
	@xml_attribute @xml_tag("FontStyle")		String  fontStyle;
	@xml_attribute @xml_tag("FontWeight")		String  fontWeight;
	@xml_attribute @xml_tag("TextAlignment") 	String 	alignment;
	
	@xml_text String textNode;
	
	public TextBlockState(String text)
	{
		this.textNode = text;
		this.textWrap = WRAP;
	}

	public int getFontSize()
	{
		return fontSize;
	}

	public void setFontSize(int fontSize)
	{
		this.fontSize = fontSize;
	}

	public String getFontFamily()
	{
		return fontFamily;
	}

	public void setFontFamily(String fontFamily)
	{
		this.fontFamily = fontFamily;
	}

	public String getWrap()
	{
		return textWrap;
	}

	public void setWrap(String wrap)
	{
		this.textWrap = wrap;
	}

	public String getTextWrap()
	{
		return textWrap;
	}

	public void setTextWrap(String textWrap)
	{
		this.textWrap = textWrap;
	}

	public String getFontStyle()
	{
		return fontStyle;
	}

	public void setFontStyle(String fontStyle)
	{
		this.fontStyle = fontStyle;
	}

	public String getFontWeight()
	{
		return fontWeight;
	}

	public void setFontWeight(String fontWeight)
	{
		this.fontWeight = fontWeight;
	}

	public String getAlignment()
	{
		return alignment;
	}

	public void setAlignment(String alignment)
	{
		this.alignment = alignment;
	}

	public String getTextNode()
	{
		return textNode;
	}

	public void setTextNode(String textNode)
	{
		this.textNode = textNode;
	}
	
}
