package ecologylab.xml.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

/**
 * Yahoo image search Thumbnail image XML description.
 *
 * @author andruid
 */
public class ThumbnailState extends ElementState
{
	public ParsedURL		Url;
	
	public int				Width;
	public int				Height;

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
}
