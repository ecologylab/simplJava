package simpl.serialization;

import simpl.core.TranslationContext;

/**
 * Interface for applications to plugin functionality before serialization of an object.
 *  
 * @author nabeel
 */
public interface ISimplSerializationPre
{		
	 void serializationPreHook(TranslationContext translationContext);
}
