package ecologylab.serialization.deserializers;

import ecologylab.serialization.TranslationContext;

public interface ISimplDeserializationHooks {
	void deserializationInHook(TranslationContext translationContext);
	void deserializationPostHook(TranslationContext translationContext, Object object);
	void deserializationPreHook(TranslationContext translationContext);

}
