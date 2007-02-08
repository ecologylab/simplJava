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

/**
 * Generic base class for application Preference objects.
 * 
 * @author andruid
 */
abstract public class Pref<T> extends ElementState
{
	static HashMap<String, Pref> allPrefs	= new HashMap<String, Pref>();
	
	/**
	 * 
	 */
	public Pref()
	{
		super();
	}

	public Pref(T value)
	{
		
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
	abstract void setValue(T newValue);
	
	protected void invalidate()
	{
		valueCached	= null;
	}

	public static Integer valueInt(String name) throws ClassCastException
	{
	    return (Integer)allPrefs.get(name).value();
	}
	
	public static Boolean valueBoolean(String name) throws ClassCastException
	{
	    return (Boolean)allPrefs.get(name).value();
	}
	
	public static Float valueFloat(String name) throws ClassCastException
	{
	    return (Float)allPrefs.get(name).value();
	}
	
	public static String valueString(String name) throws ClassCastException
	{
	    return (String)allPrefs.get(name).value();
	}
	
	// TODO: get preferenceRegistry functions from Preference.java
	
	public static Pref lookup(String key)
	{
		// need to make sure allPrefs is actually populated - otherwise you get an exception
		return allPrefs.get(key);
	}
	public static void main(String[] a)
	{
		//PrefInt ti	= (PrefInt) lookup(a[0]);
		
		//int i	= (int) ti.value();
		
		PrefInt ii = new PrefInt(5);
		int ix	= (int) ii.value();
		println("ix = " + ix);
		
		PrefBoolean ib = new PrefBoolean(true);
		boolean bi = ib.value();
		println("bi = " + bi);
		
		PrefString is = new PrefString("this is a test");
		String si = is.value();
		println("si = " + si);
		
		PrefFloat il = new PrefFloat(1645);
		float li = il.value();
		println("li = " + li);
	}
}
