package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.simpl_inherit;

/**
 * A message to be sent to a ServicesServer through HTTP that the Services will 
 * respond with an HTTP redirect URL based on whether there was an error or not.
 * 
 * @author andrew
 */

public @simpl_inherit class HttpRequest extends RequestMessage 
{
	/**
	 * Redirect URL if no errors or problems occured.
	 */
	protected @simpl_scalar ParsedURL okResponseUrl;
	
	/**
	 * Redirect URL if an error or problem occured.
	 */
	protected @simpl_scalar ParsedURL errorResponseUrl;

	@Override
	public ResponseMessage performService(Scope objectRegistry) 
	{
		return OkResponse.get();
	}
	
	/**
	 * A URL can be provided, indicating the response should be accomplished with HTTP redirect.
	 * Used when browser security is an issue.
	 * <p/>
	 * This is the redirect URL for response when processing is successful.

	 * 
	 * @param clientSessionScope	Can be used to generate HTTP GET style arguments in the redirect URL.
	 * 
	 * @return						Value passed in this message.
	 */
	@Override
	public ParsedURL okRedirectUrl(Scope clientSessionScope)
	{
		return okResponseUrl;
	}
	
	/**
	 * A URL can be provided, indicating the response should be accomplished with HTTP redirect.
	 * Used when browser security is an issue.
	 * <p/>
	 * This is the redirect URL for response when processing results in an error.
	 * 
	 * @param clientSessionScope	Can be used to generate HTTP GET style arguments in the redirect URL.
	 * 
	 * @return						Value passed in this message.
	 */
	@Override
	public ParsedURL errorRedirectUrl(Scope clientSessionScope)
	{
		return errorResponseUrl;
	}

}
