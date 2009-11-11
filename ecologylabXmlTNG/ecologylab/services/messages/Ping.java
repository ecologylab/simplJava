package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.xml_inherit;

public @xml_inherit class Ping extends RequestMessage 
{
	/**
	 * @see ecologylab.services.messages.HttpRequest#performService(ecologylab.collections.Scope)
	 */
	@Override public ResponseMessage performService(Scope objectRegistry)
	{
		debug("ping "+System.currentTimeMillis());
		
		return new Pong();
	}

}
