/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;

/**
 * Metadata about a Boolean Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
@xml_inherit
public class MetaPrefBoolean extends MetaPref<Boolean>
{
	@xml_attribute	boolean		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefBoolean()
	{
		super();
	}
	
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}
	
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}