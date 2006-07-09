package ecologylab.xml.yahoo;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;

/**
 * Top level result set collection element for Yahoo search.
 * The collected elements are all of type result.
 *
 * @author andruid
 */
public class ResultSet extends ArrayListState
{
	public int			totalResultsAvailable;
	public int			totalResultsReturned;
	
	
}
