package ecologylab.xml.yahoo;

import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;

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
	
	public int			firstResultPosition;
	
	public String		xmlns;
	public String		schemaLocation;
	public String		xsi;
	
	
	public static TranslationSpace translationSpace()
	{
		return TranslationSpace.get("yahoo", "ecologylab.xml.yahoo");
	}
}
