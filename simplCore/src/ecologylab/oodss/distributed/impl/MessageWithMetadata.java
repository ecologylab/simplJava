/*
 * Created on Apr 4, 2007
 */
package ecologylab.oodss.distributed.impl;

import ecologylab.oodss.messages.ServiceMessage;

/**
 * Represents a RequestMessage that has been translated to XML. This object encapsulates the XML
 * String, along with the request's UID, SID, and a user specified Attachment;
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * @author Bill Hamilton (bill@ecologylab.net)
 */
// FIXME -- Can we use StringBuilders in here directly to utilize memory better
public class MessageWithMetadata<M extends ServiceMessage, A> implements
		Comparable<MessageWithMetadata<M, A>>
{
	private long	uid;

	private M			message			= null;

	private A			attachment	= null;

	/**
	 * 
	 */
	public MessageWithMetadata(M response, long uid, A attachment)
	{
		this();

		this.uid = uid;

		this.attachment = attachment;

		this.setMessage(response);
	}

	public MessageWithMetadata(M response, long uid)
	{
		this(response, uid, null);
	}

	public MessageWithMetadata(M response)
	{
		this(response, -1, null);
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
		this.attachment = null;
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

	/**
	 * @return the attachment
	 */
	public A getAttachment()
	{
		return this.attachment;
	}

	@Override
	public int compareTo(MessageWithMetadata<M, A> arg0)
	{
		return (int) (this.uid - arg0.getUid());
	}

	/**
	 * @param uid
	 *          the uid to set
	 */
	public void setUid(long uid)
	{
		this.uid = uid;
	}

	/**
	 * @param attachment
	 *          the attachment to set
	 */
	public void setAttachment(A attachment)
	{
		this.attachment = attachment;
	}
}
