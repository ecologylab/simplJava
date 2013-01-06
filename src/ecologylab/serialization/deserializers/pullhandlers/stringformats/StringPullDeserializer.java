package ecologylab.serialization.deserializers.pullhandlers.stringformats;

import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.deserializers.pullhandlers.PullDeserializer;

public abstract class StringPullDeserializer extends PullDeserializer
{

	public StringPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext)
	{
		super(translationScope, translationContext);
	}

	/**
	 * Constructs that creates a deserialization handler
	 * 
	 * @param translationScope
	 *          translation scope to use for de/serializing subsequent char sequences
	 * @param translationContext
	 *          used for graph handling
	 */
	public StringPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext, DeserializationHookStrategy deserializationHookStrategy)
	{
		super(translationScope, translationContext, deserializationHookStrategy);
	}	

	public abstract Object parse(CharSequence charSequence) throws SIMPLTranslationException;
}
