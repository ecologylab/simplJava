package ecologylab.services.messages;

import ecologylab.generic.ObjectRegistry;

/**
 * Informs the service server to shut down.
 * 
 * @author blake
 */
public class StopMessage extends RequestMessage 
{

	/**
	 * This message doesn't DO anything. It just 
	 * indicates that a stop (or shutdown) event should occur.
	 */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		return null;
	}
}
