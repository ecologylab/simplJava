/**
 * 
 */
package ecologylab.appframework.types;

import ecologylab.appframework.types.MetaPref;

/**
 * Metadata about a Float Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
public class MetaPrefFloat extends MetaPref<Float>
{
	@xml_attribute	Float		defaultValue;
	
	/**
	 * 
	 */
	public MetaPrefFloat()
	{
		super();
	}
/*
	public boolean isWithinRange(Float newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}