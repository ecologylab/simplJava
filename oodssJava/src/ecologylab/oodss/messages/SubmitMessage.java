package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.annotations.simpl_inherit;

@simpl_inherit
public class SubmitMessage extends RequestMessage 
{
	public SubmitMessage()
	{
		
	}

	@Override
	public ResponseMessage performService(Scope objectRegistry) 
	{	
		return OkResponse.get();
	}
	
}
