package ecologylab.oodss.messages;

import ecologylab.collections.Scope;
import ecologylab.serialization.annotations.simpl_inherit;

public @simpl_inherit class Ping extends RequestMessage 
{
	/**
	 * @see ecologylab.oodss.messages.HttpRequest#performService(ecologylab.collections.Scope)
	 */
	@Override public ResponseMessage performService(Scope objectRegistry)
	{
		debug("ping "+System.currentTimeMillis());
		
		return new Pong();
	}

}
