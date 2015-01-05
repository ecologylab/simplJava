/**
 * 
 */
package ecologylab.oodss.distributed.impl;

import ecologylab.generic.ResourcePoolWithSize;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class PreppedRequestPool extends ResourcePoolWithSize<PreppedRequest>
{

	/**
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 * @param resourceObjectCapacity
	 */
	public PreppedRequestPool(int initialPoolSize, int minimumPoolSize, int resourceObjectCapacity)
	{
		super(initialPoolSize, minimumPoolSize, resourceObjectCapacity);
	}

	/**
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override protected void clean(PreppedRequest objectToClean)
	{
		objectToClean.clear();
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override protected PreppedRequest generateNewResource()
	{
		return new PreppedRequest(this.resourceObjectCapacity);
	}

}
