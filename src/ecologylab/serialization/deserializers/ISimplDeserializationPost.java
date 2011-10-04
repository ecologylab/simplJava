package ecologylab.serialization.deserializers;

import ecologylab.serialization.TranslationContext;

public interface ISimplDeserializationPost
{
	void deserializationPostHook(TranslationContext translationContext, Object object);
}
