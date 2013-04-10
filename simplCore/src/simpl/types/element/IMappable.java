/**
 * 
 */
package simpl.types.element;

/**
 * Implemented by objects that provide a key for automatic insertion into a Map.
 * @author andruid
 */
public interface IMappable<T>
{
	public T key();
}
