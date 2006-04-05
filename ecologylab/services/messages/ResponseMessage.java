package ecologylab.services.messages;

import ecologylab.generic.ObjectRegistry;

/**
 * Service response message.
 * 
 * @author blake
 * @author andruid
 */
abstract public class ResponseMessage 
extends ServiceMessage
{
	public ResponseMessage() {}
	
    /**
     * Let's the client easily test for OK or error.
     * 
     * @return
     */
    public abstract boolean isOK();

    /**
     * Allows for custom processing of ResponseMessages by ServicesClient, without extending that.
     * @param objectRegistry provide a context for response message processing.
     *
     */
    public void processResponse(ObjectRegistry objectRegistry)
    {
    	
    }
}
