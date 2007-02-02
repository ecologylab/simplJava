package ecologylab.appframework.types;

import java.awt.Color;

import ecologylab.appframework.Environment;
import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.Palette;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Represents a preference in the form of name/val.
 * Can also hold a tree of ElementState objects of any type!
 * that is, one or more.
 * 
 * @author blake
 * @author andruid
 */
public @xml_inherit class Preference extends ArrayListState 
{
	@xml_attribute protected String 	name;
	@xml_attribute protected String 	value;
	
	public Preference() {}
	
	public Preference(String name, String value)
	{
		this.name 	= name;
		this.value 	= value; 
	}
	public String toString()
	{
		return "Preference["+name+"\t"+value + "]\t";
	}

	/**
	 * Natural accessor when you have a single ElementState child in your preference.
	 * 
	 * @return Returns the first child in the set.
	 */
	public ElementState child()
	{
		return get(0);
	}

	/**
	 * Access the name of this Preference.
	 * 
	 * @return	The Preference's name.
	 */
	public String getName() 
	{
		return name;
	}

	/**
	 * Access the value of this Preference.
	 * 
	 * @return	The Preference's value.
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * Set the name of this Preference.
	 * 
	 * @param name
	 */
	public void setName(String name) 
	{
		this.name = name;
	}

	/**
	 * Set the value of this Preference.
	 * 
	 * @param value
	 */
	public void setValue(String value) 
	{
		this.value = value;
	}
	/**
	 * The default ObjectRegistry that preferences are stored in for the current application's Environment.
	 * 
	 * @return	ObjectRegistry that Preferences are stored in
	 */
	protected static ObjectRegistry preferencesRegistry()
	{
		return Environment.the.preferencesRegistry();
	}
	/**
	 * Register this into the Environment's default preferencesRegistry.
	 */
	public void register()
	{
		register(preferencesRegistry());
	}
	
	public static void register(String name, String value)
	{
		Preference pref	= new Preference(name, value);
		pref.register();
	}
	/**
	 * Register this into a PreferencesRegistry.
	 * 
	 * @param preferencesRegistry
	 */
	public void register(ObjectRegistry preferencesRegistry)
	{
		//debug("registering");
		ElementState child	= child();
		// is there at least one child?
/*		if (child != null)
		{
 */			preferencesRegistry.registerObject(this.name, this);
/*		}
		else
		{
			String value = this.value;
			if (value != null)
				preferencesRegistry.registerObject(this.name, value);
		}
 */	}
	
	/**
	 * Get a full Preference object entry from the registry.
	 * 
	 * @param name	The name of the preference.
	 * 
	 * @return		The corresponding Preference object.
	 */
	public static Preference lookup(String name)
	{
		Object lookupObject = preferencesRegistry().lookupObject(name);
		//println("lookup(" + name + ") got " + lookupObject);
		return (Preference) lookupObject;
	}
	/**
	 * Get an entry from the registry, with value type String.
	 * 
	 * @param name			Key to use to lookup the Preference.
	 * @param defaultValue	Default value to return if the Preference is not found in the registry.
	 * 
	 * @return			String value.
	 */
	public static String lookupString(String name, String defaultValue)
	{
		/* Object prefObj	= preferencesRegistry().lookupObject(name);
		if (prefObj instanceof String)
			return (String) */
		Preference pref	= lookup(name);
		return (pref != null) ? pref.value : defaultValue;
	}
	/**
	 * Get an entry from the registry, with value type String.
	 * 
	 * @param name		Key to use to lookup the Preference.
	 * 
	 * @return			String value, or null if the preferences registry contains no value for this key..
	 */
	public static String lookupString(String name)
	{
		return lookupString(name, null);
	}
	
	/**
	 * Get a boolean parameter from the Environment. Environment is an interface that enables
	 * consistent runtime preferences to be passed into code from diverse circumstances, such as
	 * applets, applications, and servlets. <p/>
	 * If the value is the
	 * string <code>true</code> or <code>yes</code>, the result is 
	 * <code>true</code>; else false.
	 * 
	 * @param	name	The name of the parameter's key.
	 */
	public static boolean lookupBoolean(String name)
	{
		String value	= lookupString(name);
		boolean result= (value != null) && Generic.booleanFromString(value);
		return result;
	}
	
	/**
	 * Get a boolean parameter from the Environment. Environment is an interface that enables
	 * consistent runtime preferences to be passed into code from diverse circumstances, such as
	 * applets, applications, and servlets. <p/>
	 * If the value is the
	 * string <code>true</code> or <code>yes</code>, the result is 
	 * <code>true</code>; else false.
	 * 
	 * @param name
	 * @param defaultValue
	 * 
	 * @return		The boolean tranlated from the preference in the Environment, or the defaultValue if no such preference was found.
	 */
	public static boolean lookupBoolean(String name, boolean defaultValue)
	{
		String value	= lookupString(name);
		return (value == null) ? defaultValue : Generic.booleanFromString(value);
	}
	
	/**
	 * Get an integer from the Environment. Environment is an interface that enables
	 * consistent runtime preferences to be passed into code from diverse circumstances, such as
	 * applets, applications, and servlets. <p/>
	 * The default is 0.
	 * 
	 * @param	paramName	The name of the parameter's key.
	 */
	public static int lookupInt(String paramName)
	{ 
		return lookupInt(paramName, 0); 
	}
	
	/**
	 * Get an integer parameter  from the Environment. Environment is an interface that enables
	 * consistent runtime preferences to be passed into code from diverse circumstances, such as
	 * applets, applications, and servlets. <p/> 
	 * 
	 * @param	paramName	The name of the parameter's key.
	 * @param	defaultValue	Default integer value, in case param is 
	 *				unspecified in the runtime env.
	 */
	public static int lookupInt(String paramName, int defaultValue)
	{
		String paramValue	= lookupString(paramName);
		int result	= defaultValue;
		if (paramValue != null)
		{
			try
			{
				result	= Integer.parseInt(paramValue);
			} catch (NumberFormatException e)
			{
				Debug.println("bad number format: "+paramName+"="+paramValue);
			}
		}
		return result;
	}
	
	/**
	 * Get a float parameter  from the Environment. Environment is an interface that enables
	 * consistent runtime preferences to be passed into code from diverse circumstances, such as
	 * applets, applications, and servlets. <p/>
	 * 
	 * @param	paramName	The name of the parameter's key.
	 * @param	defaultValue	Default floating point value, in case param is 
	 *				unspecified in the runtime env.
	 *
	 * @return	The float tranlated from the preference in the Environment, or the defaultValue if no such preference was found.

	 */
	public static float lookupFloat(String paramName, float defaultValue)
	{
		String paramValue	= lookupString(paramName);
		float result	= defaultValue;
		if (paramValue != null)
		{
			float parsedValue	= Generic.parseFloat(paramValue);
			if (!Float.isNaN(parsedValue))
				result	= parsedValue;
		}
		return result;
	}
	
	/**
	 * Look-up a preference which is translated into a Color.
	 * 
	 * @param param
	 * 
	 * @return	The translated Color preference, or Color.white if the Preference is not found.
	 */
	public static Color lookupColor(String param)
	{	
		String s = lookupString(param);
		return (s != null) ? Palette.hexToColor(s) : Color.white;
	}
}
