package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.distributed.common.SessionObjects;
import ecologylab.services.distributed.legacy.ServicesClient;
import ecologylab.xml.xml_inherit;

/**
 * Informs the service server to shut down.
 * 
 * @author blake
 */
@xml_inherit
public class StopMessage
extends RequestMessage
implements SessionObjects
{

	/**
	 * This message doesn't DO anything. It just 
	 * indicates that a stop (or shutdown) event should occur.
	 * @deprecated Use {@link #performService(Scope,String)} instead
	 */
	public ResponseMessage performService(Scope objectRegistry) 
	{
		return performService(objectRegistry, null);
	}
	/**
	 * This message doesn't DO anything. It just 
	 * indicates that a stop (or shutdown) event should occur.
	 */
	public ResponseMessage performService(Scope objectRegistry, String sessionId) 
	{
		StartAndStoppable sas = (StartAndStoppable) objectRegistry.get(MAIN_START_AND_STOPPABLE);
		debug("performService(): call stop(" + sas);
		if (sas != null)
			sas.stop();
		return new OkResponse(); //shouldn't get here
	}
    /**
     * Allows for custom processing of ResponseMessages by ServicesClient, without extending that.
     * Stop the server, once we confirm processing of our Stop message.
     * 
     * @param objectRegistry provide a context for response message processing.
     */
    public void processResponse(Scope objectRegistry)
    {
		 ServicesClient browserServicesClient	= (ServicesClient) objectRegistry.get(BROWSER_SERVICES_CLIENT);
		 if (browserServicesClient != null)
		 {
			 browserServicesClient.disconnect();
		 }
    }
}
