package ecologylab.oodss.messages;

import java.util.Date;

import ecologylab.collections.Scope;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

public @simpl_inherit class Ping extends RequestMessage 
{
	
	@simpl_scalar
	private Date timeSent;
	
	public Ping()
	{
		super();
		
		this.timeSent = new Date();
	}
	
	/**
	 * @see ecologylab.oodss.messages.HttpRequest#performService(ecologylab.collections.Scope)
	 */
	@Override 
	public ResponseMessage performService(Scope objectRegistry)
	{
		debug("ping "+System.currentTimeMillis());
		
		return new Pong(timeSent);
	}
}
