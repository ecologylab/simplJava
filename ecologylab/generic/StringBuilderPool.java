/**
 * 
 */
package ecologylab.generic;

/**
 * ResourcePool for StringBuilders.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class StringBuilderPool extends ResourcePoolWithSize<StringBuilder>
{
	/**
	 * 
	 * @param builderSize
	 *          the size of StringBuilders created within this pool.
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
		this(poolSize, minimumCapacity, builderSize, false);
	}

	public StringBuilderPool(int poolSize, int minimumCapacity, int builderSize,
			boolean checkMultiRelease)
	{
		super(poolSize, minimumCapacity, builderSize, checkMultiRelease);
	}

	/**
	 * Alias for acquire().
	 * 
	 * @return
	 */
	public StringBuilder nextBuffer()
	{
		return this.acquire();
	}

	/**
	 * @see ecologylab.generic.ResourcePool#clean(java.lang.Object)
	 */
	@Override
	protected void clean(StringBuilder objectToClean)
	{
		objectToClean.setLength(0);
	}

	/**
	 * @see ecologylab.generic.ResourcePool#generateNewResource()
	 */
	@Override
	protected StringBuilder generateNewResource()
	{
		return new StringBuilder(this.resourceObjectCapacity);
	}

	public String releaseAndGetString(StringBuilder objectToRelease)
	{
		String s = StringTools.toString(objectToRelease);
		this.release(objectToRelease);
		return s;
	}

	public static void main(String[] args)
	{
		
		StringBuilderPool sbp = new StringBuilderPool(8, 2, 12, true);
		System.out.println("0 "+sbp.getPoolSize() + "/" + sbp.getCapacity());

		StringBuilder sb1 = new StringBuilder("asdf");
		StringBuilder sb2 = sb1;
		StringBuilder sb3 = sb1;

		sbp.release(sb1);
		System.out.println("1 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		sbp.release(sb2);
		System.out.println("2 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		sbp.release(sb3);
		System.out.println("3 "+sbp.getPoolSize() + "/" + sbp.getCapacity());

		StringBuilder sb4 = sbp.acquire();
		System.out.println("4 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		StringBuilder sb5 = sb4;

		sbp.release(sb5);
		System.out.println("5 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		sbp.release(sb4);
		System.out.println("6 "+sbp.getPoolSize() + "/" + sbp.getCapacity());

		StringBuilder sb6 = sbp.acquire();
		System.out.println("7 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		StringBuilder sb7 = sbp.acquire();
		System.out.println("8 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		StringBuilder sb8 = sbp.acquire();
		System.out.println("9 "+sbp.getPoolSize() + "/" + sbp.getCapacity());

		sbp.release(sb6);
		System.out.println("10 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		sbp.release(sb7);
		System.out.println("11 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
		sbp.release(sb8);
		System.out.println("12 "+sbp.getPoolSize() + "/" + sbp.getCapacity());
	}
}
