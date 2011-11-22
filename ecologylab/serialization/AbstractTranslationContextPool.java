package ecologylab.serialization;

import ecologylab.generic.ResourcePool;

/**
 * a generic pool for TranslationContext objects (for performance).
 * 
 * @author quyin
 *
 * @param <T>
 */
public abstract class AbstractTranslationContextPool<T extends TranslationContext> extends ResourcePool<T>
{
	
	public static final int INITIAL_AND_MINIMUM_POOL_SIZE = 10;

	protected AbstractTranslationContextPool()
	{
		super(INITIAL_AND_MINIMUM_POOL_SIZE, INITIAL_AND_MINIMUM_POOL_SIZE);
	}

	@Override
	protected abstract T generateNewResource();

	@Override
	protected void clean(T objectToClean)
	{
		objectToClean.clean();
	}
	
}
