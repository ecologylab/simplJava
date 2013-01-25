package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.annotations.simpl_composite;

public class SubmitHttpRequest extends HttpGetRequest
{
	protected @simpl_composite SubmitMessage submitMessage;
	
	@Override public ResponseMessage performService(Scope objectRegistry) 
	{
		if (submitMessage != null)
			return submitMessage.performService(objectRegistry);
		else
			return new ErrorResponse("Failed to perform service. No SubmitMessage provided.");
	}
	
}
