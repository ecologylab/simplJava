package simpl.deserialization.binaryformats;

import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.deserialization.PullDeserializer;
import simpl.exceptions.SIMPLTranslationException;

public abstract class BinaryPullDeserializer extends PullDeserializer
{

	public BinaryPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext)
	{
		super(translationScope, translationContext);
	}

	public abstract Object parse(byte[] byteArray) throws SIMPLTranslationException;
}
