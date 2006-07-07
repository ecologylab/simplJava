package cf.services.messages;

import ecologylab.xml.ElementState;

/**
 * Represents a preference in the form of name/val.
 * 
 * @author blake
 */
public class Preference extends ElementState 
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
}
