package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.simpl_inherit;

/**
 * Abstract base class for ecologylab.oodss DCF request messages.
 * 
 * @author blake
 * @author andruid
 */
@simpl_inherit
public abstract class RequestMessage<S extends Scope> extends ServiceMessage<S> implements
		SendableRequest
{
	public RequestMessage()
	{
		super();
	}

	/**
	 * Perform the service associated with the request, using the supplied context as needed.
	 * 
	 * @param localScope
	 *          Context to perform it in/with.
	 * @return Response to pass back to the (remote) caller.
	 */
	public abstract ResponseMessage performService(S clientSessionScope);

	/**
	 * Indicates whether or not this type of message may be ignored by the server, if the server
	 * becomes backed-up. For example, a RequestMessage subclass that simply requests the server's
	 * current state may be ignored if a more recent copy of one has arrived later.
	 * 
	 * By default, RequestMessages are not disposable; this method should be overriden if they are to
	 * be.
	 * 
	 * @return false.
	 */
	public boolean isDisposable()
	{
		return false;
	}

	/**
	 * A URL can be provided, indicating the response should be accomplished with HTTP redirect. Used
	 * when browser security is an issue.
	 * <p/>
	 * This is the redirect URL for response when processing is successful.
	 * 
	 * @param clientSessionScope
	 *          Can be used to generate HTTP GET style arguments in the redirect URL.
	 * 
	 * @return null in this the base class case.
	 */
	public ParsedURL okRedirectUrl(S clientSessionScope)
	{
		return null;
	}

	/**
	 * A URL can be provided, indicating the response should be accomplished with HTTP redirect. Used
	 * when browser security is an issue.
	 * <p/>
	 * This is the redirect URL for response when processing results in an error.
	 * 
	 * @param clientSessionScope
	 *          Can be used to generate HTTP GET style arguments in the redirect URL.
	 * 
	 * @return null in this the base class case.
	 */
	public ParsedURL errorRedirectUrl(S clientSessionScope)
	{
		return null;
	}

}
