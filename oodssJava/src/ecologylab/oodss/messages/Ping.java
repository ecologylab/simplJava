package ecologylab.oodss.messages;

import simpl.annotations.dbal.simpl_inherit;
import ecologylab.collections.Scope;

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
