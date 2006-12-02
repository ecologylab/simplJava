/**
 * 
 */
package ecologylab.services.messages.cf;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;
import ecologylab.xml.ElementState.xml_nested;

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
	
	@xml_nested protected		SeedSet seedSet;
	

	/**
	 * 
	 */
	public SeedCf()
	{
		super();

	}

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
	public void add(Seed seed)
	{
		seedSet().add(seed);
	}
	/**
	 * Dummy version -- will never get called, because this version lives only on the client.
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

}
