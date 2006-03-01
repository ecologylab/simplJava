package ecologylab.services.messages;

import ecologylab.generic.ObjectRegistry;

/**
 * Service request message.
 * 
 * @author blake
 * @author andruid
 */
public abstract class RequestMessage 
extends ServiceMessage
implements ResponseTypes
{
	/**
	 * Perform the service associated with the request, using the supplied
	 * context as needed.
	 * 
	 * @param requestMessage	Message to perform.
	 * @param objectRegistry	Context to perform it in/with.
	 * 
	 * @return					Response to pass back to the (remote) caller.
	 */
	public abstract ResponseMessage performService(RequestMessage requestMessage, ObjectRegistry objectRegistry);

	/**
	 * Perform the service associated with the request, using a null 
	 * ObjectRegistry context.
	 * 
	 * @param requestMessage	Message to perform.
	 * 
	 * @return					Response to pass back to the (remote) caller.
	 */
	public ResponseMessage performService(RequestMessage requestMessage)
	{
	   return performService(requestMessage, null);
	}
}
