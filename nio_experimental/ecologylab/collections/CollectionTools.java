/**
 * 
 */
package ecologylab.collections;

import java.util.HashMap;
import java.util.Map;

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
	    * Create a HashMap, and populate with entries from the passed in array of Strings.
	    * The key and value will be the same for each entry.
	    * @param strings
	    * @return
	    */
	   public static final HashMap buildHashMapFromStrings(String[] strings)
	   {
	      HashMap hashMap	= new HashMap(strings.length);
	      buildMapFromStrings(hashMap, strings);
	      return hashMap;
	   }

	/**
	    * Create a HashMap, and populate with entries from the passed in array of Strings.
	    * But only use Strings that are lower case; other entries in the array are ignored.
	    * The key and value will be the same for each entry.
	    * @param strings
	    * @return
	    */
	   public static final HashMap buildHashMapFromLCStrings(String[] strings)
	   {
	      HashMap hashMap	= new HashMap(strings.length);
	      buildMapFromLCStrings(hashMap, strings);
	      return hashMap;
	   }

	/**
	    * Create a HashMap, and popuplate with entries from the passed in array of
	    * key value pairs.
	    * @param entries
	    * @return
	    */
	   public static final HashMap buildHashMap(Object[][] entries)
	   {
	      HashMap hashMap	= new HashMap(entries.length);
	      buildMap(hashMap, entries);
	      return hashMap;
	   }

	/**
	    * Populate a HashMap from the supplied array of Strings.
	    * The key and value will be the same for each entry.
	    * @param map
	    * @param strings
	    */
	   public static final void buildMapFromStrings(Map map, String[] strings)
	   {
	      for (int i=0; i<strings.length; i++)
	      {
			 String thatString	= strings[i];
			 map.put(thatString, thatString);
	      }
	   }

	/**
	    * Populate a HashMap from the supplied array of Strings.
	    * But only use Strings that are lower case; other entries in the array are ignored.
	    * The key and value will be the same for each entry.
	    * @param map
	    * @param strings
	    */
	   public static final void buildMapFromLCStrings(Map map, String[] strings)
	   {
	      for (int i=0; i<strings.length; i++)
	      {
			 String thatString	= strings[i];
			 if (StringTools.isLowerCase(thatString))
				 map.put(thatString, thatString);
	      }
	   }

	public static final void buildMap(Map map, Object[][] entries)
	   {
	      for (int i=0; i<entries.length; i++)
	      {
	    	 Object[] thatEntry	= entries[i];
	    	 Object thatKey		= thatEntry[0];
	    	 Object thatValue	= thatEntry[1];
			 map.put(thatKey, thatValue);
	      }
	   }

	// The keys come from the String[], the values are corresponding number start with 0
	   public static final HashMap buildNumberHashMapFromStrings(String[] strings)
	   {
	      HashMap hashMap	= new HashMap(strings.length);
	      buildNumberMapFromStrings(hashMap, strings);
	      return hashMap;
	   }

	// The keys come from the String[], the values are corresponding number start with 0
	   public static final void buildNumberMapFromStrings(Map map, String[] strings)
	   {
	      for (int i=0; i<strings.length; i++)
	      {
			 String thatString	= strings[i];
			 Integer integer = new Integer(i);
			 map.put(thatString, integer);
	      }
	   }

	public static final void stringIntMapEntry(Map map,
						      String string, int integer)
	   {
	      map.put(string, new IntSlot(integer));
	   }

}
