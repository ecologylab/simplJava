/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.appframework.types.MetaPref;

/**
 * Metadata about a String Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
public class MetaPrefString extends MetaPref<String>
{
	@xml_attribute	String		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefString()
	{
		super();
	}
/*
	public boolean isWithinRange(String newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}