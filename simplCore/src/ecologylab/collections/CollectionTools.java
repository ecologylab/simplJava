/**
 * 
 */
package ecologylab.collections;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ecologylab.generic.Debug;
import ecologylab.generic.IntSlot;
import ecologylab.generic.StringTools;

/**
 * Convenience methods for creating and manipulating collections.
 * 
 * @author andruid
 * 
 */
public class CollectionTools extends Debug
{

	/**
	 * Create a HashMap, and populate with entries from the passed in array of Strings. The key and
	 * value will be the same for each entry.
	 * 
	 * @param strings
	 * @return
	 */
	public static final HashMap<String, String> buildHashMapFromStrings(String[] strings)
	{
		HashMap<String, String> hashMap = new HashMap<String, String>(strings.length);
		buildMapFromStrings(hashMap, strings);
		return hashMap;
	}

	/**
	 * Create a HashSet, and populate with entries from the passed in array of Strings. 
	 * 
	 * @param strings
	 * @return
	 */
	public static final HashSet<String> buildHashSetFromStrings(String[] strings)
	{
		HashSet<String> result = new HashSet<String>(strings.length);
		buildSetFromStrings(result, strings);
		return result;
	}

	/**
	 * Create a HashMap, and populate with entries from the passed in array of Strings. But only use
	 * Strings that are lower case; other entries in the array are ignored. The key and value will be
	 * the same for each entry.
	 * 
	 * @param strings
	 * @return
	 */
	public static final HashMap<String, String> buildHashMapFromLCStrings(String[] strings)
	{
		HashMap<String, String> hashMap = new HashMap<String, String>(strings.length);
		buildMapFromLCStrings(hashMap, strings);
		return hashMap;
	}

	/**
	 * Create a HashMap, and popuplate with entries from the passed in array of key value pairs.
	 * 
	 * @param entries
	 * @return
	 */
	public static <T> HashMap<T, T> buildHashMap(T[][] entries)
	{
		HashMap<T, T> hashMap = new HashMap<T, T>(entries.length);
		buildMap(hashMap, entries);
		return hashMap;
	}

	/**
	 * Populate a HashMap from the supplied array of Strings. The key and value will be the same for
	 * each entry.
	 * 
	 * @param map
	 * @param strings
	 */
	public static final void buildMapFromStrings(Map<String, String> map, String[] strings)
	{
		for (String thatString: strings)
			map.put(thatString, thatString);
	}

	/**
	 * Populate a HashMap from the supplied array of Strings. The key and value will be the same for
	 * each entry.
	 * 
	 * @param map
	 * @param strings
	 */
	public static final void buildSetFromStrings(Set<String> set, String[] strings)
	{
		for (String thatString: strings)
			set.add(thatString);
	}

	/**
	 * Populate a HashMap from the supplied array of Strings. But only use Strings that are lower
	 * case; other entries in the array are ignored. The key and value will be the same for each
	 * entry.
	 * 
	 * @param map
	 * @param strings
	 */
	public static final void buildMapFromLCStrings(Map<String, String> map, String[] strings)
	{
		for (String thatString: strings)
		{
			if (StringTools.isLowerCase(thatString))
				map.put(thatString, thatString);
		}
	}

	public static <T> void buildMap(Map<T, T> map, T[][] entries)
	{
		for (int i = 0; i < entries.length; i++)
		{
			T[] thatEntry = entries[i];
			T thatKey = thatEntry[0];
			T thatValue = thatEntry[1];
			map.put(thatKey, thatValue);
		}
	}

	// The keys come from the String[], the values are corresponding number start with 0
	public static final HashMap buildNumberHashMapFromStrings(String[] strings)
	{
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>(strings.length);
		buildNumberMapFromStrings(hashMap, strings);
		return hashMap;
	}

	// The keys come from the String[], the values are corresponding number start with 0
	public static final void buildNumberMapFromStrings(Map<String, Integer> map, String[] strings)
	{
		for (int i = 0; i < strings.length; i++)
		{
			String thatString = strings[i];
			Integer integer = new Integer(i);
			map.put(thatString, integer);
		}
	}

	public static final void stringIntMapEntry(Map<String, IntSlot> map, String string, int integer)
	{
		map.put(string, new IntSlot(integer));
	}

}
