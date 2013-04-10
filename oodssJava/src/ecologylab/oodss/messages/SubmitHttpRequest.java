package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_composite;
import ecologylab.collections.Scope;

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
