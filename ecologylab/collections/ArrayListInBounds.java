/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;

/**
 * An ArrayList that returns null if get(int)'ing an element that does not exist.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class ArrayListInBounds<T> extends ArrayList<T>
{
	private static final long	serialVersionUID	= -4733082961336679335L;

	/**
	 * @see java.util.ArrayList#get(int)
	 */
	@Override
	public T get(int index)
	{
		if (index >= super.size() || index < 0)
			return null;

		return super.get(index);
	}

}
