/**
 * 
 */
package ecologylab.translators.hibernate;

import java.util.HashMap;
import java.util.Map;

import ecologylab.generic.Debug;

/**
 * Utility for recording table / column names.
 * 
 * @author quyin
 * 
 */
public abstract class NameTable<T> extends Debug
{

	private Map<T, String>	names	= new HashMap<T, String>();

	/**
	 * Client need to implement this method to specify naming convention.
	 * 
	 * @param obj
	 * @return
	 */
	abstract public String createName(T obj);

	/**
	 * Look up the table for the name of an object, or create a new name if not recorded.
	 * 
	 * @param obj
	 * @return
	 */
	public String get(T obj)
	{
		if (names.containsKey(obj))
			return names.get(obj);
		String name = createName(obj);
		names.put(obj, name);
		return name;
	}
	
	/**
	 * Clear the table.
	 */
	public void clear()
	{
		names.clear();
	}

}
