package ecologylab.serialization.serializers;

/**
 * Interface for applications to plugin functionality after serialization of an object.
 *  
 * @author nabeel
 */
public interface ISimplSerializationPost
{
	public void serializationPostHook();
}
