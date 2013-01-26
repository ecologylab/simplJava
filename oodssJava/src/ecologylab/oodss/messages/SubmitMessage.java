package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.collections.Scope;

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
