/**
 * 
 */
package ecologylab.generic;

/**
 * ResourcePool for StringBuilders.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class StringBuilderPool extends ResourcePoolWithSize<StringBuilder>
{
	/**
	 * 
	 * @param builderSize the size of StringBuilders created within this pool.
	 */
	public StringBuilderPool(int builderSize)
	{
		this(DEFAULT_POOL_SIZE, NEVER_CONTRACT, builderSize);
	}

	public StringBuilderPool(int poolSize, int builderSize)
	{
		this(poolSize, NEVER_CONTRACT, builderSize);
	}

	/**
	 * @param poolSize
	 */
	public StringBuilderPool(int poolSize, int minimumCapacity, int builderSize)
	{
		super(poolSize, minimumCapacity, builderSize);
	}

	/**
	 * Alias for acquire().
	 * @return
	 */
	public StringBuilder nextBuffer()
	{
		return this.acquire();
	}
	
	/**
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override protected void clean(StringBuilder objectToClean)
	{
		objectToClean.setLength(0);
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override protected StringBuilder generateNewResource()
	{
		return new StringBuilder(this.resourceObjectCapacity);
	}
	
	public String releaseAndGetString ( StringBuilder objectToRelease )
	{
		String s = StringTools.toString(objectToRelease);
		this.release(objectToRelease);
		return s;
		
	}

}
