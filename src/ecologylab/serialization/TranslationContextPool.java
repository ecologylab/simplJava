package ecologylab.serialization;


/**
 * a pool for TranslationContext objects (for performance).
 * 
 * @author quyin
 */
public class TranslationContextPool extends AbstractTranslationContextPool<TranslationContext>
{
	
	@Override
	protected TranslationContext generateNewResource()
	{
		return new TranslationContext();
	}

	private static TranslationContextPool translationContextPool = new TranslationContextPool();
			
	public static TranslationContextPool get()
	{
		return translationContextPool;
	}

}
