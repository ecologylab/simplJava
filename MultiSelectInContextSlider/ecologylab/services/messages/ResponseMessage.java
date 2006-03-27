package ecologylab.services.messages;

/**
 * Service response message.
 * @author blake
 *
 */
public class ResponseMessage 
extends ServiceMessage
implements ResponseTypes
{
	public String response;
	
	public ResponseMessage() {}
	
	public ResponseMessage(String response)
	{
		this.response = response;
	}
}
