package ecologylab.generic;

import java.util.HashMap;

/**
 * A registry of Objects, each of which is available using a String key.
 * Used for passing context across application modules, in a very general way.
 *
 * These replace statics and are much more flexible.
 */
public class ObjectRegistry
{
   /**
    * Registry of objects associated with this Window.
    * These replace statics and are much more flexible.
    */
   private		HashMap			registryMap	= new HashMap();
   
	/**
	 * Register an object in this.
     * 
	 * @param name
	 * @param value
	 */
	public void registerObject(String name, Object value)
	{
		//TODO -- consider checking to see if its an overwrite of a previously 
		//defined object w the same key, and throwing a RuntimeException if it is.
		registryMap.put(name, value);
	}
	/**
	 * Lookup an object in this.
	 * 
	 * @param name
	 * 
	 * @return	The object associatedt with this name, found in the registry,
	 * 			or null if there is none.
	 */
	public Object lookupObject(String name)
	{
		return registryMap.get(name);
	}
    
    public void modifyObject(String name, Object value)
    {
        registryMap.put(name, value);
    }
   
    public boolean containsKey(String key)
    {
    	return registryMap.containsKey(key);
    }
}
