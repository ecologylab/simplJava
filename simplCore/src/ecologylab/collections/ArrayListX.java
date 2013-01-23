/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Extension of ArrayList.
 * Makes removeRange() visible.
 * 
 * @author andruid
 *
 */
public class ArrayListX<E> extends ArrayList<E>
{

	/**
	 * 
	 */
	public ArrayListX()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param initialCapacity
	 */
	public ArrayListX(int initialCapacity)
	{
		super(initialCapacity);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param c
	 */
	public ArrayListX(Collection<? extends E> c)
	{
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void removeRange(int fromIndex, int toIndex)
	{
		super.removeRange(fromIndex, toIndex);
	}
}
