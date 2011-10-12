package ecologylab.serialization.library.yahoo;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * Top level result set collection element for Yahoo search. The collected elements are all of type
 * result.
 * 
 * http://www.yahooapis.com/search/web/V1/webSearch.html
 * 
 * @author andruid
 */
@simpl_inherit
@simpl_tag("ResultSet")
public class ResultSet extends ElementState
{

	@simpl_scalar
	@simpl_tag("xsi:schemaLocation")
	String											schemaLocation;

	@simpl_scalar
	@simpl_tag("type")
	String											type;

	@simpl_scalar
	@simpl_tag("moreSearch")
	String											moreSearch;

	@simpl_scalar
	@simpl_tag("totalResultsAvailable")
	int													totalResultsAvailable;

	@simpl_scalar
	@simpl_tag("totalResultsReturned")
	int													totalResultsReturned;

	@simpl_scalar
	@simpl_tag("firstResultPosition")
	int													firstResultPosition;

	@simpl_nowrap
	@simpl_collection("Result")
	ArrayList<Result>						results;

	public static final String	YAHOO					= "yahoo";

	public static final Class[]	YAHOO_CLASSES	=
																						{ ResultSet.class, Result.class, ThumbnailState.class, };

	public static SimplTypesScope getTranslationScope()
	{
		return SimplTypesScope.get(YAHOO, YAHOO_CLASSES);
	}

	public static String	SEOUL_SHOPPING	= "http://api.search.yahoo.com/WebSearchService/V1/webSearch?appid=yahoosearchwebrss&results=15&start=1&query=shopping+seoul";

	public static void main(String[] a)
	{
		ParsedURL purl = ParsedURL.getAbsolute(SEOUL_SHOPPING, "uck");
		try
		{
			ResultSet resultSet = (ResultSet) ResultSet.getTranslationScope().deserialize(purl,
					Format.XML);

			SimplTypesScope.serialize(resultSet, System.out, StringFormat.XML);
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}

	public Result get(int i)
	{
		return results == null ? null : results.get(i);
	}

	public int size()
	{
		return results == null ? 0 : results.size();
	}
}
