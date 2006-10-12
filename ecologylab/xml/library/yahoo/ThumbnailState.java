package ecologylab.xml.yahoo;

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
	@xml_leaf	public ParsedURL		Url;
	
	@xml_leaf	public int				Width;
	@xml_leaf	public int				Height;

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
