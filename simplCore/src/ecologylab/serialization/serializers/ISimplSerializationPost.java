package ecologylab.serialization.serializers;

import ecologylab.serialization.TranslationContext;

/**
 * Interface for applications to plugin functionality after serialization of an object.
 *  
 * @author nabeel
 */
public interface ISimplSerializationPost
{
	void serializationPostHook(TranslationContext translationContext);
}
