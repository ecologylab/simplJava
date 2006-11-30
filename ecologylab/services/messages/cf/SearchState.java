/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.xml.xml_inherit;

/**
 * {@link Seed Seed} element that directs combinFormation to perform a search.
 * 
 * Starts by providing a basis for specification of search seeds.
 * Then, keeps state during processing of the search.
 * 
 * Version for client only:
 * 	<li>data slot definitions only with no other functionality.</li>
 * 
 * @author andruid
 */
@xml_inherit
public class SearchState extends Seed
{
	/**
	 * Search engine to use. 
	 * Currently supported are google, flickr, yahoo, yahoo_image, yahoo_news, yahoo_buzz, delicious.
	 */
	@xml_attribute protected String			engine;
	/**
	 * Query string to pass to the search engine.
	 */
	@xml_attribute protected String			query;
	
	/**
	 * For del.icio.us only. Allows querying the delicious tags for a particular user.
	 */
	@xml_attribute protected String			creator;
	   

	/**
	 * 
	 */
	public SearchState()
	{
		super();

	}


	/**
	 * @return Returns the creator.
	 */
	public String getCreator()
	{
		return creator;
	}


	/**
	 * @param creator The creator to set.
	 */
	public void setCreator(String creator)
	{
		this.creator = creator;
	}


	/**
	 * @return Returns the engine.
	 */
	public String getEngine()
	{
		return engine;
	}


	/**
	 * @param engine The engine to set.
	 */
	public void setEngine(String engine)
	{
		this.engine = engine;
	}


	/**
	 * @return Returns the query.
	 */
	public String getQuery()
	{
		return query;
	}


	/**
	 * @param query The query to set.
	 */
	public void setQuery(String query)
	{
		this.query = query;
	}

}
