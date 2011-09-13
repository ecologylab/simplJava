package ecologylab.serialization.deserializers;

import ecologylab.serialization.TranslationContext;

public interface ISimplDeserializatonPost
{
	void deserializationPostHook(TranslationContext translationContext);
}
