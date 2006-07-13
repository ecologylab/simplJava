package ecologylab.xml.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementStateWithLeafElements;

/**
 * Yahoo image search Thumbnail image XML description.
 *
 * @author andruid
 */
public class ThumbnailState extends ElementStateWithLeafElements
{
	public ParsedURL		Url;
	
	public int				Width;
	public int				Height;

	static final String[]	LEAF_ELEMENT_FIELD_NAMES	= 
	{"Url", "Width", "Height"};
	
	static
	{
		defineLeafElementFieldNames(LEAF_ELEMENT_FIELD_NAMES);
	}
	
}
