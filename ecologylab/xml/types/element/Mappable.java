/**
 * 
 */
package ecologylab.xml.types.element;

/**
 * Implemented by objects that provide a key for automatic insertion into a Map.
 * @author andruid
 */
public interface Mappable<T>
{
	public T key();
}
