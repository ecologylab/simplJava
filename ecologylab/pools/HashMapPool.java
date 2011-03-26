/**
 * 
 */
package ecologylab.pools;

import java.util.HashMap;

import ecologylab.generic.ResourcePoolWithSize;

/**
 * @author andruid
 *
 */
public class HashMapPool extends ResourcePoolWithSize<HashMap>
{
	
	public HashMapPool(int hashMapSize)
	{
		this(DEFAULT_POOL_SIZE, NEVER_CONTRACT, hashMapSize, false);
	}

	public HashMapPool(int poolSize, int hashMapSize)
	{
		this(poolSize, NEVER_CONTRACT, hashMapSize, false);
	}

	public HashMapPool(int poolSize, int minimumCapacity, int hashMapSize,
			boolean checkMultiRelease)
	{
		super(poolSize, minimumCapacity, hashMapSize, checkMultiRelease);
	}

	@Override
	protected void clean(HashMap objectToClean)
	{
		objectToClean.clear();
	}

	@Override
	protected HashMap generateNewResource()
	{
		return new HashMap(this.resourceObjectCapacity);
	}

}
