/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.appframework.types.MetaPref;

/**
 * Metadata about a Boolean Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
public class MetaPrefBoolean extends MetaPref<Boolean>
{
	@xml_attribute	Boolean		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefBoolean()
	{
		super();
	}
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}