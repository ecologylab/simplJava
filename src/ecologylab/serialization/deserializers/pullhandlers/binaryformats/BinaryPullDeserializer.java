package ecologylab.serialization.deserializers.pullhandlers.binaryformats;

import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.deserializers.pullhandlers.PullDeserializer;

public abstract class BinaryPullDeserializer extends PullDeserializer
{

	public BinaryPullDeserializer(TranslationScope translationScope,
			TranslationContext translationContext)
	{
		super(translationScope, translationContext);
	}

	public abstract Object parse(byte[] byteArray);
}
