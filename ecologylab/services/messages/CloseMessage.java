package ecologylab.services.messages;

import ecologylab.generic.ObjectRegistry;
import ecologylab.xml.xml_inherit;

@xml_inherit
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
