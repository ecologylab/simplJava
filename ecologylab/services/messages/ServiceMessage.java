package ecologylab.services.messages;

import java.net.InetAddress;

import ecologylab.collections.Scope;
import ecologylab.xml.ElementState;

/**
 * Abstract base class for ecologylab.services DCF request and response messages.
 * 
 * @author blake
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class ServiceMessage<S extends Scope> extends ElementState implements
		Comparable<ServiceMessage>
{
	public ServiceMessage()
	{
		super();
	}

	@simpl_scalar
	protected long				timeStamp	= 0;

	/**
	 * Used to carry uid for messages, now only used by legacy code. Retained temporarily for
	 * backwards compatability.
	 */
	@simpl_scalar
	@Deprecated
	protected long				uid;

	/**
	 * Contains the IP address of the host that sent this message. sender currently must be set by a
	 * server that recieves the message and associates it with the IP address from it's packet and/or
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

	@Deprecated
	public void setUid(long uid)
	{
		this.uid = uid;
	}

	@Deprecated
	public long getUid()
	{
		return uid;
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

	@Deprecated
	public int compareTo(ServiceMessage otherRequest)
	{
		return (int) (this.uid - otherRequest.getUid());
	}
}
