package ecologylab.xml.library.yahoo;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;

/**
 * Top level result set collection element for Yahoo search.
 * The collected elements are all of type result.
 * 
 * http://www.yahooapis.com/search/web/V1/webSearch.html
 *
 * @author andruid
 */
@xml_inherit
@xml_tag("ResultSet")
public class ResultSet extends ElementState
{
	
	@xml_attribute 	@xml_tag("xsi:schemaLocation")		String 		schemaLocation;
	@xml_attribute 	@xml_tag("type")					String 		type;
	@xml_attribute  @xml_tag("moreSearch")				String 		moreSearch;
	
	@xml_attribute	@xml_tag("totalResultsAvailable")	int			totalResultsAvailable;
	@xml_attribute	@xml_tag("totalResultsReturned")	int			totalResultsReturned;
	
	@xml_attribute	@xml_tag("firstResultPosition")		int			firstResultPosition;
	
	@xml_nowrap
	@xml_collection("Result")	ArrayList<Result>					results;
	
	public static final String YAHOO = "yahoo";
	
	public static final Class[]	YAHOO_CLASSES	= 
	{
		ResultSet.class, Result.class, ThumbnailState.class,
	};
	
	public static TranslationScope getTranslationScope()
	{
		return TranslationScope.get(YAHOO, YAHOO_CLASSES);
	}
	
	public static String SEOUL_SHOPPING		= "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=yahoosearchwebrss&results=15&start=1&query=shopping+seoul";
	
	public static void main(String[] a)
	{
		ParsedURL purl = ParsedURL.getAbsolute(SEOUL_SHOPPING, "uck");
		try
		{
			ResultSet resultSet	= 
				(ResultSet) ElementState.translateFromXML(purl, ResultSet.getTranslationScope());
			
			resultSet.translateToXML(System.out);
		} catch (XMLTranslationException e)
		{
			e.printStackTrace();
		}

	}
}
