package ecologylab.oodss.messages;

import ecologylab.serialization.simpl_inherit;

/**
 * The ResponseMessage send from server to client when the RequestMessage
 * is well-formed, but doesn't make sense in the current context.
 * 
 * @author andruid
 * @author blake
 * @author eunyee
 */
@simpl_inherit
public class BadSemanticContentResponse extends ErrorResponse
{

	public BadSemanticContentResponse()
	{
		super();
	}

	public BadSemanticContentResponse(String response)
	{
		super(response);
	}

}
