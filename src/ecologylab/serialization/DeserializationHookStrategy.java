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
	
	void deserializationPreHook(O o, FD fd);
	
	void deserializationPostHook(O o, FD fd);
	
	/**
	 * change the deserialized object if necessary.
	 * 
	 * @param o
	 * @param fd
	 * @return the changed object. for preventing potential errors, if the returned value is null, it
	 *         will be ignored and nothing will be changed.
	 */
	O changeObjectIfNecessary(O o, FD fd);
	
}
