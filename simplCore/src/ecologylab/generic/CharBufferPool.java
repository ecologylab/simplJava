/**
 * 
 */
package ecologylab.generic;

import java.nio.CharBuffer;

/**
 * ResourcePool for StringBuilders.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class CharBufferPool extends ResourcePoolWithSize<CharBuffer>
{
	/**
	 * @param builderSize the size of StringBuilders created within this pool.
	 */
	public CharBufferPool(int builderSize)
	{
		this(DEFAULT_POOL_SIZE, DEFAULT_POOL_SIZE/4, builderSize);
	}

	/**
	 * @param poolSize
	 */
	public CharBufferPool(int poolSize, int minimumCapacity, int builderSize)
	{
		super(poolSize, minimumCapacity, builderSize);
	}
	
	/**
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override protected void clean(CharBuffer objectToClean)
	{
		objectToClean.clear();
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override protected CharBuffer generateNewResource()
	{
		return CharBuffer.allocate(this.resourceObjectCapacity);
	}

}
