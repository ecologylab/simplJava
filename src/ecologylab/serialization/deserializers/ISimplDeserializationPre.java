package ecologylab.serialization.deserializers;

import ecologylab.serialization.TranslationContext;

public interface ISimplDeserializationPre
{
	void deserializationPreHook(TranslationContext translationContext);
}
