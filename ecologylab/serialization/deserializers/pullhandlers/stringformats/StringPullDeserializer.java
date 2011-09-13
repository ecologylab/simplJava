package ecologylab.serialization.deserializers.pullhandlers.stringformats;

import java.io.File;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.deserializers.pullhandlers.PullDeserializer;

public abstract class StringPullDeserializer extends PullDeserializer
{

	public StringPullDeserializer(TranslationScope translationScope,
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
	public StringPullDeserializer(TranslationScope translationScope,
			TranslationContext translationContext, DeserializationHookStrategy deserializationHookStrategy)
	{
		super(translationScope, translationContext, deserializationHookStrategy);
	}
	

	@Override
	public Object parse(File file)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object parse(ParsedURL purl)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public abstract Object parse(CharSequence charSequence) throws SIMPLTranslationException;

}
