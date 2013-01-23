package ecologylab.serialization;

/**
 * 
 * @author quyin
 *
 */
public interface SerializationHookStrategy
{
	
	ElementState serializationPreHook(ElementState es);

}
