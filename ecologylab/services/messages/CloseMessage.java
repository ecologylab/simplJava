package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.xml_inherit;

@xml_inherit public class CloseMessage extends RequestMessage
{
	private static final CloseMessage	INSTANCE	= new CloseMessage();

	@Override public ResponseMessage performService(Scope objectRegistry)
	{
		System.exit(0);
		return null;
	}

	public static CloseMessage get()
	{
		return INSTANCE;
	}

}
