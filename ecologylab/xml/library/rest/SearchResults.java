package ecologylab.xml.library.rest;

import java.util.ArrayList;

import ecologylab.xml.ElementState;

/**
 * Represents REST search results
 * @author blake
 *
 */
public class SearchResults extends ElementState
{
	@simpl_composite protected ResultsInfo 				resultsInfo;
	
	@simpl_nowrap
	@simpl_collection("Record")
	protected ArrayList<Record>	results = new ArrayList<Record>();
	
	public SearchResults() {}
	
	public SearchResults(ResultsInfo resultsInfo, ArrayList<Record> results)
	{
		this.resultsInfo 	= resultsInfo;
		this.results		= results;
	}

	/**
	 * @param results the results to set
	 */
	public void setResults(ArrayList<Record> results)
	{
		this.results = results;
	}

	/**
	 * @return the results
	 */
	public ArrayList<Record> getResults()
	{
		return results;
	}
}
