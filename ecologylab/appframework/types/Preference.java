package ecologylab.services.messages;

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
	public String 	name;
	public String 	value;
	
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
	
	
}
