/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a String Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
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
	
    public String getDefaultValue()
	{
		return defaultValue;
	}
	
/*
	public boolean isWithinRange(String newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}