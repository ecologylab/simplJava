/**
 * 
 */
package ecologylab.services.distributed.impl;

import ecologylab.generic.ResourcePool;
import ecologylab.services.messages.ServiceMessage;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class MessageWithMetadataPool<M extends ServiceMessage> extends ResourcePool<MessageWithMetadata<M>>
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
	@Override protected void clean(MessageWithMetadata<M> objectToClean)
	{
		objectToClean.clear();
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override protected MessageWithMetadata<M> generateNewResource()
	{
		return new MessageWithMetadata<M>();
	}

}
