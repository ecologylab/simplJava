package ecologylab.services.messages;

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

	public String getName() 
	{
		return name;
	}

	public String getValue() 
	{
		return value;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public void setValue(String value) 
	{
		this.value = value;
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
	
}
