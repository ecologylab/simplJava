package ecologylab.xml.library.rest;

import ecologylab.xml.ElementState;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Represents REST search results
 * @author blake
 *
 */
public class SearchResults extends ElementState
{
	@xml_nested protected ResultsInfo 				resultsInfo;
	@xml_nested protected ArrayListState<Record>	results = new ArrayListState<Record>();
	
	public SearchResults() {}
	
	public SearchResults(ResultsInfo resultsInfo, ArrayListState<Record> results)
	{
		this.resultsInfo 	= resultsInfo;
		this.results		= results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(ArrayListState<Record> results)
	{
		this.results = results;
	}

	/**
	 * @return the results
	 */
	public ArrayListState<Record> getResults()
	{
		return results;
	}
}
