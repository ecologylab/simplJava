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
	 * Currently supported are "google", "flickr", "yahoo", "yahoo_image", "yahoo_news", "yahoo_buzz", "delicious".
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
	 * Default constructor.
	 */
	public SearchState()
	{
		super();
	}

	/**
	 * Construct with engine and query.
	 * 
	 * @param engine	Search engine to use. 
	 * 					Currently supported are "google", "flickr", "yahoo", "yahoo_image", "yahoo_news", "yahoo_buzz", "delicious".

	 * @param query		Search query to pass to the engine.
	 */
	public SearchState(String engine, String query)
	{
		this();
		this.setEngineAndQuery(engine, query);
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
	 * Set search engine.
	 * 
	 * @param engine	Search engine to use. 
	 * 					Currently supported are "google", "flickr", "yahoo", "yahoo_image", "yahoo_news", "yahoo_buzz", "delicious".
	 */
	public void setEngine(String engine)
	{
		this.engine = engine;
	}


	/**
	 * @return Returns the search query being sent to the engine.
	 */
	public String getQuery()
	{
		return query;
	}


	/**
	 * @param query The search query to send to the engine.
	 */
	public void setQuery(String query)
	{
		this.query = query;
	}

	/**
	 * Form a typical search query.
	 * 
	 * @param engine	Search engine to use. 
	 * 					Currently supported are "google", "flickr", "yahoo", "yahoo_image", "yahoo_news", "yahoo_buzz", "delicious".

	 * @param query		Search query to pass to the engine.
	 */
	public void setEngineAndQuery(String engine, String query)
	{
		this.engine = engine;		
		this.query = query;
	}
}
