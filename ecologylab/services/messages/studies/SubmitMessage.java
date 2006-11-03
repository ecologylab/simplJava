package ecologylab.services.messages.studies;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

public class SubmitMessage extends RequestMessage 
{
	//public boolean close;
	static final SubmitMessage SUBMIT_INSTANCE = new SubmitMessage();
	
	public SubmitMessage()
	{
		
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{	
		return OkResponse.get();
	}
	
	public static SubmitMessage get()
	{
		return SUBMIT_INSTANCE;
	}
}
