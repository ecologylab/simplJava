package ecologylab.oodss.distributed.impl;

import ecologylab.oodss.messages.ServiceMessage;

public class MessageWithUid {
	private ServiceMessage message;
	private long uid;
	
	public MessageWithUid(ServiceMessage message, long uid)
	{
		this.message = message;
		this.uid = uid;
	}
	
	public ServiceMessage getMessage()
	{
		return message;
	}
	
	public long getUid()
	{
		return uid;
	}
}
