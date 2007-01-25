/**
 * 
 */
package ecologylab.appframework.types;

import java.util.HashMap;

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
	
	protected void invalidate()
	{
		valueCached	= null;
	}

	public static Pref lookup(String key)
	{
		return allPrefs.get(key);
	}
	public static void main(String[] a)
	{
		PrefInt ti	= (PrefInt) lookup(a[0]);
		
		int i	= (int) ti.value();
	}
}
