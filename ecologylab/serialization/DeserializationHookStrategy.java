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
public interface DeserializationHookStrategy<E extends ElementState, FD extends FieldDescriptor>
{
	public void deserializationPreHook(E e, FD fd);
	
	public void deserializationPostHook(E e, FD fd);
}
