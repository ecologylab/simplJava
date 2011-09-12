package ecologylab.serialization.serializers;

/**
 * Interface for applications to plugin functionality before serialization of an object.
 *  
 * @author nabeel
 */
public interface ISimplSerializationPre
{		
	public void serializationPreHook();
}
