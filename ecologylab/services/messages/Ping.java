package ecologylab.services.messages;

import ecologylab.collections.Scope;
import ecologylab.xml.simpl_inherit;

public @simpl_inherit class Ping extends RequestMessage 
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
