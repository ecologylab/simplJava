/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A collection of objects that belong to binary categories. Lazilly
 * instantiates the necessary intersections of the categories. Maximum
 * categories are determined dynamically. Uses bit-masks to indicate container
 * relationships.
 * 
 * 63 is the maximum number of binary categories for this class.
 * 
 * Does not allow multiple identical objects in the same set (per the rules of
 * Set).
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * @author William Hamilton (bill@ecologylab.net)
 */
public class OverlappingSets<O extends Object>
{
	/**
	 * A map of categories to sets of objects contained in them. Keys are bit
	 * masks indicating the container set. For example, 0001 indicates an object
	 * is contained only in set 1, 1001 indicates the sets 1 and 4.
	 */
	private Map<Long, List<O>>	setMap;

	public OverlappingSets()
	{
		setMap = new HashMap<Long, List<O>>();
	}

	public List<O> getSet(Long setID)
	{
		List<O> result = setMap.get(setID);

		if (result == null)
		{
			synchronized (setMap)
			{
				result = setMap.get(setID);

				if (result == null)
				{
					result = new ArrayList<O>();

					setMap.put(setID, result);
				}
			}
		}

		return result;
	}

	public void put(Long setID, O value)
	{
		List<O> setToInsertInto = getSet(setID);

		setToInsertInto.add(value);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override public String toString()
	{
		return this.setMap.toString();
	}
}
