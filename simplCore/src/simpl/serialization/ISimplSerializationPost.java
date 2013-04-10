package simpl.serialization;

import simpl.core.TranslationContext;

/**
 * Interface for applications to plugin functionality after serialization of an object.
 *  
 * @author nabeel
 */
public interface ISimplSerializationPost
{
	void serializationPostHook(TranslationContext translationContext);
}
