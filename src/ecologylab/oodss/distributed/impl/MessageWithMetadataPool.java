/**
 * 
 */
package ecologylab.oodss.distributed.impl;

import ecologylab.generic.ResourcePool;
import ecologylab.oodss.messages.ServiceMessage;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class MessageWithMetadataPool<M extends ServiceMessage, A> extends ResourcePool<MessageWithMetadata<M, A>>
{

	/**
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 * @param resourceObjectCapacity
	 */
	public MessageWithMetadataPool(int initialPoolSize, int minimumPoolSize)
	{
		super(initialPoolSize, minimumPoolSize);
	}

	/**
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override protected void clean(MessageWithMetadata<M, A> objectToClean)
	{
		objectToClean.clear();
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override protected MessageWithMetadata<M, A> generateNewResource()
	{
		return new MessageWithMetadata<M,A>();
	}

}
