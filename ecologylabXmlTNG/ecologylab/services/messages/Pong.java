package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.xml_inherit;

/**
 * Base class for all ResponseMessages that were processed successfully.
 * 
 * @author andruid
 */
@xml_inherit
public class Pong extends ResponseMessage
{
	public static final Pong reusableInstance	= new Pong();
	
	public Pong()
	{
		super();
	}

	@Override public boolean isOK()
	{
		return true;
	}
	
	public static Pong get()
	{
		return reusableInstance;
	}

	/**
	 * @see ecologylab.services.messages.ResponseMessage#processResponse(ecologylab.collections.Scope)
	 */
	@Override public void processResponse(Scope objectRegistry)
	{
		debug("pong: "+System.currentTimeMillis());
		super.processResponse(objectRegistry);
	}
}
