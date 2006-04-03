package ecologylab.services.messages;

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
     *
     */
    public void processResponse()
    {
    	
    }
}
