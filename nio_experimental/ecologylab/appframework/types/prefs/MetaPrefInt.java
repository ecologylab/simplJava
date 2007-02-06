/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.appframework.types.prefs.MetaPref;

/**
 * Metadata about an Integer Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
public class MetaPrefInt extends MetaPref<Integer>
{
	@xml_attribute	Integer		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefInt()
	{
		super();
	}
/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}