/**
 * 
 */
package ecologylab.collections;

/**
 * @author eunyee
 *
 */
public interface LinearAccess<E>
{
	/**
	 * Return the ith element in the collection.
	 * @param i
	 * @return
	 */
	public E get(int i);
	
	public int size();

	public boolean isEmpty();

}
