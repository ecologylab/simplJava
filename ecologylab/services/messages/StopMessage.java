package ecologylab.services.messages;

import ecologylab.generic.ObjectRegistry;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.ServicesClient;
import ecologylab.services.SessionObjects;
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
	 */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		StartAndStoppable sas = (StartAndStoppable) objectRegistry.lookupObject(MAIN_START_AND_STOPPABLE);
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
    public void processResponse(ObjectRegistry objectRegistry)
    {
		 ServicesClient browserServicesClient	= (ServicesClient) objectRegistry.lookupObject(BROWSER_SERVICES_CLIENT);
		 if (browserServicesClient != null)
		 {
			 browserServicesClient.disconnect();
		 }
    }
}
