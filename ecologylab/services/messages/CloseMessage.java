package ecologylab.services.messages;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.xml.xml_inherit;

@xml_inherit
public class CloseMessage extends RequestMessage 
{
	private static final CloseMessage INSTANCE = new CloseMessage();

	/**
	 * @deprecated Use {@link #performService(ObjectRegistry,String)} instead
	 */
	public ResponseMessage performService(ObjectRegistry objectRegistry) 
	{
		return performService(objectRegistry, null);
	}

	public ResponseMessage performService(ObjectRegistry objectRegistry, String sessionId) 
	{
		System.exit(0);
		return null;
	}
	
	public static CloseMessage get()
	{
		return INSTANCE;
	}

}
