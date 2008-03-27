package ecologylab.xml.library.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Yahoo image search Thumbnail image XML description.
 *
 * @author andruid
 */
public class ThumbnailState extends ElementState
{
	@xml_leaf	ParsedURL		Url;
	
	@xml_leaf	int				Width;
	@xml_leaf	int				Height;

	static final String[]	LEAF_ELEMENT_FIELD_NAMES	= 
	{"Url", "Width", "Height"};
	
	/**
	 * The array of Strings with the names of the leaf elements.
	 * 
	 * @return
	 */
	protected String[] leafElementFieldNames()
	{
		return LEAF_ELEMENT_FIELD_NAMES;
	}

	public ParsedURL getUrl()
	{
		return Url;
	}

	public void setUrl(ParsedURL url)
	{
		Url = url;
	}

	public int getWidth()
	{
		return Width;
	}

	public void setWidth(int width)
	{
		Width = width;
	}

	public int getHeight()
	{
		return Height;
	}

	public void setHeight(int height)
	{
		Height = height;
	}

	public static String[] getLEAF_ELEMENT_FIELD_NAMES()
	{
		return LEAF_ELEMENT_FIELD_NAMES;
	}
}
