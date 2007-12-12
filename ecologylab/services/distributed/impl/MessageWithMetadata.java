/*
 * Created on Apr 4, 2007
 */
package ecologylab.services.distributed.impl;

import ecologylab.services.messages.ServiceMessage;

/**
 * Represents a RequestMessage that has been translated to XML. This object encapsulates the XML String, along with the
 * request's UID.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
// FIXME -- Can we use StringBuilders in here directly to utilize memory better
public class MessageWithMetadata<M extends ServiceMessage> implements Comparable<MessageWithMetadata<M>>
{
	private long				uid;

	private M	message	= null;

	/**
	 * 
	 */
	public MessageWithMetadata(M response, long uid)
	{
		this();

		this.uid = uid;

		this.setMessage(response);
	}

	public MessageWithMetadata()
	{

	}

	/**
	 * Resets this for re-use.
	 */
	public void clear()
	{
		this.uid = -1;
		this.message = null;
	}

	/**
	 * @return the request
	 */
	public M getMessage()
	{
		return message;
	}

	public void setMessage(M response)
	{
		this.message = response;
	}

	/**
	 * @return the uid
	 */
	public long getUid()
	{
		return uid;
	}

	public int compareTo(MessageWithMetadata<M> arg0)
	{
		return (int) (this.uid - arg0.getUid());
	}

	/**
	 * @param uid
	 *           the uid to set
	 */
	public void setUid(long uid)
	{
		this.uid = uid;
	}
}
