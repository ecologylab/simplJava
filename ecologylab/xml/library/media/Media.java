package ecologylab.xml.media;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Root class for inserting elements from the Yahoo Media XML Namespace.
 * 
 * @see	{@link http://search.yahoo.com/mrss http://search.yahoo.com/mrss}.
 *
 * @author andruid
 */
public class Media extends ElementState
{
	/**
	 * A single Content element in the Yahoo schema.
	 * <p/>
	 * An alternative would be to implement this as an ArrayListState, and be able
	 * to aggregate a set of these. We can do this when there is a need, with an example.
	 */
	public Content		content;
	
	//public Credit		credit;
	
	@xml_leaf	public String		category;
	
	@xml_leaf	public String		text;
	
	@xml_leaf	public String		rating;

	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= {"text", "category", "rating"};
	
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
