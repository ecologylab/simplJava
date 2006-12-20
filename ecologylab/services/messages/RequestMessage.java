package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.xml.xml_inherit;

/**
 * Abstract base class for ecologylab.services DCF request messages.
 * 
 * @author blake
 * @author andruid
 */
@xml_inherit
public abstract class RequestMessage 
extends ServiceMessage
{
	/**
	 * Perform the service associated with the request, using the supplied
	 * context as needed.
	 * @param objectRegistry	Context to perform it in/with.
	 * 
	 * @return					Response to pass back to the (remote) caller.
	 */
	public abstract ResponseMessage performService(ObjectRegistry objectRegistry);

	/**
	 * Perform the service associated with the request, using a null 
	 * ObjectRegistry context.
	 * 
	 * @return					Response to pass back to the (remote) caller.
	 */
	public ResponseMessage performService()
	{
	   return performService(null);
	}
}
