package ecologylab.services.messages;

import ecologylab.generic.Environment;
import ecologylab.generic.ObjectRegistry;
import ecologylab.xml.ArrayListState;
import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

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
		return "Preference\tname="+name+" value="+value;
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
	 * @return
	 */
	protected static ObjectRegistry preferencesRegistry()
	{
		return Environment.the.preferencesRegistry();
	}
	/**
	 * Register this into the Environment's default preferencesRegistry.
	 * 
	 * @param preferencesRegistry
	 */
	public void register()
	{
		register(preferencesRegistry());
	}
	/**
	 * Register this into a PreferencesRegistry.
	 * 
	 * @param preferencesRegistry
	 */
	public void register(ObjectRegistry preferencesRegistry)
	{
		debug("registering");
		ElementState child	= child();
		// is there at least one child?
		if (child != null)
		{
			preferencesRegistry.registerObject(this.name, this);
		}
		else
		{
			String value = this.value;
			if (value != null)
				preferencesRegistry.registerObject(this.name, value);
		}
	}
	
	/**
	 * Get a full Preference object entry from the registry.
	 * 
	 * @param name	The name of the preference.
	 * 
	 * @return		The corresponding Preference object.
	 */
	public static Preference lookup(String name)
	{
		return (Preference) preferencesRegistry().lookupObject(name);
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
}
