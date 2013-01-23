package ecologylab.oodss.messages;

import java.net.InetAddress;

import ecologylab.collections.Scope;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Abstract base class for ecologylab.oodss DCF request and response messages.
 * 
 * @author blake
 * @author Zachary O. Dugas Toups (zach@ecologylab.net)
 */
public class ServiceMessage<S extends Scope> extends ElementState
{
	public ServiceMessage()
	{
		super();
	}

	@simpl_scalar
	protected long				timeStamp	= 0;

	/**
	 * Contains the IP address of the host that sent this message. sender currently must be set by a
	 * server that receives the message and associates it with the IP address from it's packet and/or
	 * channel.
	 */
	protected InetAddress	sender		= null;

	/**
	 * Sets timeStamp to equal the current system time in milliseconds.
	 * 
	 */
	public void stampTime()
	{
		timeStamp = System.currentTimeMillis();
	}

	/**
	 * @return Returns the timeStamp in milliseconds.
	 */
	public long getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @return the sender's IP address
	 */
	public InetAddress getSender()
	{
		return sender;
	}

	/**
	 * This method should be called by a server when it translates this message.
	 * 
	 * @param sender
	 *          the sender's IP address to set
	 */
	public void setSender(InetAddress sender)
	{
		this.sender = sender;
	}
}
