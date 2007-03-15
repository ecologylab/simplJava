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
	
	/**
	 * This is for working with <code>Pref</code>s whose values you will continue to access as they
	 * are edited, live, by the user. The result will be immediate changes in the program's behavior.
	 * <p/>
	 * Lookup a Pref associated with name.
	 * If you find it return it.
	 * If not, create a new Pref object of the correct type.
	 * Set its value to default value.
	 * 
	 * @param name			Name of the Pref to lookup and find or create.
	 * @param defaultValue	Initial value of the Pref if it didn't already exist.
	 * 
	 * @return	A usable Pref object associated with name, either from the registry or newly created
	 */
    public static PrefBoolean usePrefBoolean(String name, boolean defaultValue)
    {
        PrefBoolean pref= (PrefBoolean) lookupPref(name);
        if (pref == null)
        {
        	pref		= new PrefBoolean(defaultValue);
        	pref.name	= name;
        	pref.register();
        }
        return pref;
    }
    
	/**
	 * This is for working with <code>Pref</code>s whose values you will continue to access as they
	 * are edited, live, by the user. The result will be immediate changes in the program's behavior.
	 * <p/>
	 * Lookup a Pref associated with name.
	 * If you find it return it.
	 * If not, create a new Pref object of the correct type.
	 * Set its value to default value.
	 * 
	 * @param name			Name of the Pref to lookup and find or create.
	 * @param defaultValue	Initial value of the Pref if it didn't already exist.
	 * 
	 * @return	A usable Pref object associated with name, either from the registry or newly created
	 */
    public static PrefFloat usePrefFloat(String name, float defaultValue)
    {
        PrefFloat pref	= (PrefFloat) lookupPref(name);
        if (pref == null)
        {
        	pref		= new PrefFloat(defaultValue);
        	pref.name	= name;
        	pref.register();
        }
        return pref;
    }
    
	/**
	 * This is for working with <code>Pref</code>s whose values you will continue to access as they
	 * are edited, live, by the user. The result will be immediate changes in the program's behavior.
	 * <p/>
	 * Lookup a Pref associated with name.
	 * If you find it return it.
	 * If not, create a new Pref object of the correct type.
	 * Set its value to default value.
	 * 
	 * @param name			Name of the Pref to lookup and find or create.
	 * @param defaultValue	Initial value of the Pref if it didn't already exist.
	 * 
	 * @return	A usable Pref object associated with name, either from the registry or newly created
	 */
    public static PrefString usePrefString(String name, String defaultValue)
    {
    	PrefString pref = (PrefString) lookupPref(name);
        if (pref == null)
        {
        	pref		= new PrefString(defaultValue);
        	pref.name	= name;
        	pref.register();
        }
        return pref;
    }
    
	/**
	 * This is for working with <code>Pref</code>s whose values you will continue to access as they
	 * are edited, live, by the user. The result will be immediate changes in the program's behavior.
	 * <p/>
	 * Lookup a Pref associated with name.
	 * If you find it return it.
	 * If not, create a new Pref object of the correct type.
	 * Set its value to default value.
	 * 
	 * @param name			Name of the Pref to lookup and find or create.
	 * @param defaultValue	Initial value of the Pref if it didn't already exist.
	 * 
	 * @return	A usable Pref object associated with name, either from the registry or newly created
	 */
    public static PrefInt usePrefInt(String name, int defaultValue)
    {
    	PrefInt pref = (PrefInt) lookupPref(name);
        if (pref == null)
        {
        	pref		= new PrefInt(defaultValue);
        	pref.name	= name;
        	pref.register();
        }
        return pref;
    }
    
    /**
	 * This is for working with <code>Pref</code>s whose values you will continue to access as they
	 * are edited, live, by the user. The result will be immediate changes in the program's behavior.
	 * <p/>
	 * Lookup a Pref associated with name.
	 * If you find it return it.
	 * If not, create a new Pref object of the correct type.
	 * Set its value to default value.
	 * 
	 * @param name			Name of the Pref to lookup and find or create.
	 * @param defaultValue	Initial value of the Pref if it didn't already exist.
	 * 
	 * @return	A usable Pref object associated with name, either from the registry or newly created
	 */
    public static PrefColor usePrefColor(String name, Color defaultValue)
    {
    	PrefColor pref = (PrefColor) lookupPref(name);
        if (pref == null)
        {
        	pref		= new PrefColor(defaultValue);
        	pref.name	= name;
        	pref.register();
        }
        return pref;
    }

	
    public static Pref lookupPref(String name)
    {
        Pref pref = allPrefsMap.lookupObject(name);
        return pref;
    }
    
    public static int lookupInt(String name, int defaultValue) throws ClassCastException
    {
    	/* could do this -- its heavier weight -- create a Pref if there wasn't one, and set its default value as here
        PrefInt prefInt = usePrefInt(name, defaultValue);
        return prefInt.value();
        */
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
    
    public static Color lookupColor(String name, Color defaultValue) throws ClassCastException
    {
        PrefColor prefColor = ((PrefColor)lookupPref(name));
		return (prefColor == null) ? defaultValue : prefColor.value();
    }
    public static Color lookupColor(String name) throws ClassCastException
    {
        return lookupColor(name, null);
    }
       
    public static ElementState lookupElementState(String name) throws ClassCastException
    {
        PrefElementState prefElementState = ((PrefElementState)lookupPref(name));
		return (prefElementState == null) ? null : prefElementState.value();
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
