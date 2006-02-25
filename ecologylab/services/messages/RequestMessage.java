package ecologylab.services.messages;

/**
 * Service request message.
 * 
 * @author blake
 */
public abstract class RequestMessage 
extends ServiceMessage
implements ResponseTypes
{
	public abstract ResponseMessage performService(RequestMessage requestMessage);
}
