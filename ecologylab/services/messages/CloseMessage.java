package ecologylab.services.messages;

import ecologylab.generic.ObjectRegistry;

public class CloseMessage extends RequestMessage 
{
	private static final CloseMessage INSTANCE = new CloseMessage();

	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		System.exit(0);
		return null;
	}
	
	public static CloseMessage get()
	{
		return INSTANCE;
	}

}
