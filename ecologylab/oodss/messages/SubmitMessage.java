package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.simpl_inherit;

@simpl_inherit
public class SubmitMessage extends RequestMessage 
{
	public SubmitMessage()
	{
		
	}

	public ResponseMessage performService(Scope objectRegistry) 
	{	
		return OkResponse.get();
	}
	
}
