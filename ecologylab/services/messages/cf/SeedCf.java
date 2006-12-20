/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Pass a set of seeds to combinFormation's agents.
 * 
 * Version for client only:
 * 	<li>data slot definitions only with no other functionality.</li>
 * 
 * @author andruid
 */
@xml_inherit
public class SeedCf extends RequestMessage
{
	/**
	 * Indicates which CFSession the seeding message effect,
	 * in case there are more than one.
	 * The default, 0, indicates the first session that was created,
	 * or if none have been, to create one now, and then use it.
	 */
	@xml_attribute protected	int		sessionNumber;

	/**
	 * The set of seeds being passed to the combinFormation information collecting agent.
	 */
	@xml_nested protected		SeedSet seedSet;
	
	/**
	 * If there are multiple seeding requests in a single session, how should we handle it?
	 */
	@xml_attribute protected	int	handleMultipleRequests;
	
	public static final String[] MULTIPLE_REQUESTSTS_DIALOG_OPTIONS = {  "ignore", "replace seeds", "mix seeds",    };
	
	// NOTE! These values, except for ASK_USER, are 1 more than the corresponding index into the MULTIPLE_REQUESTSTS_DIALOG_OPTIONS array.
	
	public static final int MULTIPLE_REQUESTSTS_IGNORE = 1, MULTIPLE_REQUESTSTS_REPLACE = 2, MULTIPLE_REQUESTSTS_MIX = 3, MULTIPLE_REQUESTSTS_ASK_USER = 0;
	

	/**
	 * Default constructor.
	 */
	public SeedCf()
	{
		super();

	}

	/**
	 * Access the SeedSet slot. If it is currently null, create a new SeedSet, and set the slot to its value.
	 * 
	 * @return	An instance of SeedSet, from the SeedSet slot. Never null.
	 */
	public SeedSet seedSet()
	{
		SeedSet result	= seedSet;
		if (result == null)
		{
			result		= new SeedSet();
			seedSet		= result;
		}
		return result;
	}
	/**
	 * Add a Seed to the request, through its seedSet.
	 * 
	 * @param seed
	 */
	public void add(Seed seed)
	{
		seedSet().add(seed);
	}
	/**
	 * Dummy version -- will never get called, because this version lives only on the client,
	 * and the real functionality is on the server.
	 * 
	 * @param objectRegistry
	 * @return
	 */
	@Override
	public ResponseMessage performService(ObjectRegistry objectRegistry)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Remove entries from the SeedSet, if there is one.
	 */
	public void clear()
	{
		if (seedSet != null)
			seedSet.clear();
	}
}
