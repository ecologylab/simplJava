package ecologylab.xml.media;

import ecologylab.xml.ElementState;

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
	Content		content;
	
	Credit		credit;
	
	String		category;
	
	String		text;
	
	String		rating;
}
