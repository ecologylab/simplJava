/**
 * 
 */
package ecologylab.serialization.library.xaml;

import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.Hint;

/**
 * ecologylab.serialization representation of the TextBlock WPF element for translating to XAML.
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
	
	@simpl_scalar @xml_tag("FontSize") 		int 	fontSize;
	@simpl_scalar @xml_tag("FontFamily") 		String 	fontFamily;
	@simpl_scalar @xml_tag("TextWrapping")		String 	textWrap;
	@simpl_scalar @xml_tag("FontStyle")		String  fontStyle;
	@simpl_scalar @xml_tag("FontWeight")		String  fontWeight;
	@simpl_scalar @xml_tag("TextAlignment") 	String 	alignment;
	
	@simpl_scalar @simpl_hints(Hint.XML_TEXT) String textNode;
	
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
