package ecologylab.serialization.deserializers.pullhandlers.binaryformats;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.deserializers.pullhandlers.PullDeserializer;

public abstract class BinaryPullDeserializer extends PullDeserializer
{

	public BinaryPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext)
	{
		super(translationScope, translationContext);
	}

	public abstract Object parse(byte[] byteArray) throws SIMPLTranslationException;
}
