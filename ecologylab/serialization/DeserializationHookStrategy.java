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
public interface DeserializationHookStrategy<E extends ElementState>
{
	public void preDeserializationHook(E e);
}
