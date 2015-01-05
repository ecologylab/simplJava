package ecologylab.serialization.library.rest;

import java.net.URL;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * Rest Search Results
 * @author blake
 *
 */
public class RestSearchResult extends ElementState
{
	@simpl_scalar protected String 		schemaVersion;
	@simpl_scalar protected String 		xmlns;
	@simpl_scalar @simpl_tag("xmlns:xsi")	
					protected String			xsi = "http://www.w3.org/2001/XMLSchema-instance";
	@simpl_scalar @simpl_tag("xmlns:dc")	
					protected String			dc = "http://purl.org/dc/elements/1.1/";
	@simpl_scalar @simpl_tag("xsi:schemaLocation")
					protected String		schemaLocation;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF) 	protected 	 String 		responseTime;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)   protected 	 URL			request;
	@simpl_composite @simpl_tag("SearchResults") 
				protected 	 SearchResults 	SearchResults;
	
	public RestSearchResult() {}

	/**
	 * @param schemaVersion the schemaVersion to set
	 */
	public void setSchemaVersion(String schemaVersion)
	{
		this.schemaVersion = schemaVersion;
	}

	/**
	 * @return the schemaVersion
	 */
	public String getSchemaVersion()
	{
		return schemaVersion;
	}

	/**
	 * @param xmlns the xmlns to set
	 */
	public void setXmlns(String xmlns)
	{
		this.xmlns = xmlns;
	}

	/**
	 * @return the xmlns
	 */
	public String getXmlns()
	{
		return xmlns;
	}

	/**
	 * @param searchResults the searchResults to set
	 */
	public void setSearchResults(SearchResults searchResults)
	{
		this.SearchResults = searchResults;
	}

	/**
	 * @return the searchResults
	 */
	public SearchResults getSearchResults()
	{
		return SearchResults;
	}
	
	@Override
	public String toString()
	{
		return "RestSearchResult{\n" + 
				"responseTime: "	+ responseTime 	+ "\n" + 
				"request: "			+ getRequest()		+ "\n" +
				"SearchResults: "	+ SearchResults + "\n" +
				"}";
	}

	public void setRequest(URL request)
	{
		this.request = request;
	}

	public URL getRequest()
	{
		return request;
	}

	/**
	 * @param schemaLocation the schemaLocation to set
	 */
	public void setSchemaLocation(String schemaLocation)
	{
		this.schemaLocation = schemaLocation;
	}

	/**
	 * @return the schemaLocation
	 */
	public String getSchemaLocation()
	{
		return schemaLocation;
	}
	
}
