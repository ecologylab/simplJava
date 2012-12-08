package ecologylab.oodss.messages;

import java.util.Date;

import ecologylab.collections.Scope;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Base class for all ResponseMessages that were processed successfully.
 * 
 * @author andruid
 */
@simpl_inherit
public class Pong extends ResponseMessage
{
	public static final Pong reusableInstance	= new Pong();
	
	@simpl_scalar
	private Date clientSentTime;
	
	@simpl_scalar
	private Date serverSentTime; 
	
	public Pong(Date client)
	{
		super();

		this.clientSentTime = client;
		this.serverSentTime = new Date();
	}
	
	public Pong()
	{
		super();
	}

	@Override public boolean isOK()
	{
		return true;
	}
	
	public static Pong get()
	{
		return reusableInstance;
	}

	/**
	 * @see ecologylab.oodss.messages.ResponseMessage#processResponse(ecologylab.collections.Scope)
	 */
	@Override public void processResponse(Scope objectRegistry)
	{
		Date fromServerTime = new Date();
		debug("pong: "+System.currentTimeMillis());
		super.processResponse(objectRegistry);
	}
}
