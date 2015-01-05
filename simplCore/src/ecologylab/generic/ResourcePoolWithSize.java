/**
 * 
 */
package ecologylab.generic;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public abstract class ResourcePoolWithSize<T> extends ResourcePool<T>
{
	protected int	resourceObjectCapacity;

	/**
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 * @param resourceObjectCapacity
	 *          the capacity of objects that are contained in this pool; for example, if this is a
	 *          ResourcePoolWithSize\<StringBuilder\>, this would specify the size of the
	 *          StringBuilders in the pool.
	 */
	public ResourcePoolWithSize(int initialPoolSize, int minimumPoolSize, int resourceObjectCapacity)
	{
		super(false, initialPoolSize, minimumPoolSize, false);

		this.resourceObjectCapacity = resourceObjectCapacity;

		this.instantiateResourcesInPool();
	}

	/**
	 * @param initialPoolSize
	 * @param minimumPoolSize
	 * @param resourceObjectCapacity
	 *          the capacity of objects that are contained in this pool; for example, if this is a
	 *          ResourcePoolWithSize\<StringBuilder\>, this would specify the size of the
	 *          StringBuilders in the pool.
	 */
	public ResourcePoolWithSize(int initialPoolSize, int minimumPoolSize, int resourceObjectCapacity, boolean checkMultiRelease)
	{
		super(false, initialPoolSize, minimumPoolSize, checkMultiRelease);

		this.resourceObjectCapacity = resourceObjectCapacity;

		this.instantiateResourcesInPool();
	}
}
