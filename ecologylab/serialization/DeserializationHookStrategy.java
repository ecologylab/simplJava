/**
 * 
 */
package ecologylab.serialization;

/**
 * Used to connect the state of an object (outside the ElementState subclasses being created)
 * to deserialization hooks.
 * 
 * @author andruid
 *
 */
public interface DeserializationHookStrategy<O extends Object, FD extends FieldDescriptor>
{
	public void deserializationPreHook(O o, FD fd);
	
	public void deserializationPostHook(O o, FD fd);
}
