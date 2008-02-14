package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.xml_inherit;

@xml_inherit
public class CloseMessage extends RequestMessage 
{
	private static final CloseMessage INSTANCE = new CloseMessage();

	/**
	 * @deprecated Use {@link #performService(Scope,String)} instead
	 */
	public ResponseMessage performService(Scope objectRegistry) 
	{
		return performService(objectRegistry, null);
	}

	public ResponseMessage performService(Scope objectRegistry, String sessionId) 
	{
		System.exit(0);
		return null;
	}
	
	public static CloseMessage get()
	{
		return INSTANCE;
	}

}
