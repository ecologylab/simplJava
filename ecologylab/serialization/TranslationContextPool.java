package ecologylab.serialization;

import ecologylab.generic.ResourcePool;

public class TranslationContextPool extends ResourcePool<TranslationContext>
{
	
	public static final int INITIAL_AND_MINIMUM_POOL_SIZE = 10;

	protected TranslationContextPool()
	{
		super(INITIAL_AND_MINIMUM_POOL_SIZE, INITIAL_AND_MINIMUM_POOL_SIZE);
	}

	@Override
	protected TranslationContext generateNewResource()
	{
		return new TranslationContext();
	}

	@Override
	protected void clean(TranslationContext objectToClean)
	{
		objectToClean.clean();
	}
	
	private static TranslationContextPool translationContextPool = new TranslationContextPool();
	
	public static TranslationContextPool get()
	{
		return translationContextPool;
	}

}
