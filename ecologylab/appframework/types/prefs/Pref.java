/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;
import java.util.HashMap;

import ecologylab.appframework.Environment;
import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.Palette;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Generic base class for application Preference objects.
 * 
 * @author andruid
 */

@xml_inherit
public abstract class Pref<T> extends ArrayListState
{
	/**
	 * The global registry of Pref objects. Used for providing lookup services.
	 */
    static final ObjectRegistry<Pref> allPrefsMap	= new ObjectRegistry<Pref>();
    
    @xml_attribute String name;
	
	/**
	 * 
	 */
	public Pref()
	{
		super();
	}

	T		valueCached;
	
	/**
	 * Public generic accessor for the value.
	 * Caches autoboxed values, for efficiency.
	 * @return
	 */
	public T value()
	{
		T result	= valueCached;
		if (result == null)
		{
			result	= getValue();
			valueCached	= result;
		}
		return result;
	}
    
    public void print()
    {
        println("Pref: name: " + name + ", value: " + this.getValue());
    }
    
    public String toString()
    {
        return "Pref: name: "+name+", value: "+this.getValue();
    }
	
	/**
	 * Generic get value returns the value as the actual type you want.
	 * This version should only be called by value(), so that autoboxed types
	 * can be cached. This method *does not* do the caching.
	 * 
	 * @return
	 */
	abstract T getValue();
	
	/**
	 * Generic value setter.
	 * Uses boxed reference objects for primitives, which are a bit extra expensive.
	 * 
	 * @param newValue
	 */
	public abstract void setValue(T newValue);
	
	protected void invalidate()
	{
		valueCached	= null;
	}
	
	
    public static Pref lookupPref(String name)
    {
        Pref pref = allPrefsMap.lookupObject(name);
        return pref;
    }
    
    public static int lookupInt(String name, int defaultValue) throws ClassCastException
    {
        PrefInt prefInt = ((PrefInt)lookupPref(name));
		return (prefInt == null) ? defaultValue : prefInt.value();
    }
    public static int lookupInt(String name) throws ClassCastException
    {
        return lookupInt(name, 0);
    }
   
    public static boolean lookupBoolean(String name, boolean defaultValue) throws ClassCastException
    {
        PrefBoolean prefBoolean = ((PrefBoolean)lookupPref(name));
		return (prefBoolean == null) ? defaultValue : prefBoolean.value();
    }
    public static boolean lookupBoolean(String name) throws ClassCastException
    {
        return lookupBoolean(name, false);
    }
       
    public static float lookupFloat(String name, float defaultValue) throws ClassCastException
    {
        PrefFloat prefFloat = ((PrefFloat)lookupPref(name));
		return (prefFloat == null) ? defaultValue : prefFloat.value();
    }
    public static float lookupFloat(String name) throws ClassCastException
    {
        return lookupFloat(name, 1.0f);
    }
   
    public static String lookupString(String name, String defaultValue) throws ClassCastException
    {
        PrefString prefString = ((PrefString)lookupPref(name));
		return (prefString == null) ? defaultValue : prefString.value();
    }
    public static String lookupString(String name) throws ClassCastException
    {
        return lookupString(name, null);
    }
       
    public static ElementState lookupElementState(String name) throws ClassCastException
    {
        return ((PrefElementState)lookupPref(name)).value();
    }
    
    public static boolean hasPref(String name)
    {
        return allPrefsMap.containsKey(name);
    }

    /**
     * Create an entry for this in the allPrefsMap.
     *
     */
    void register()
    {
    	allPrefsMap.registerObject(this.name, this);
    }
    
    /**
     * Check for existence / membership.
     * 
     * @param key
     * 
     * @return	true if there is a Pref already registered with name key
     */
    public static boolean containsKey(String key)
    {
    	return allPrefsMap.containsKey(key);
    }
}
