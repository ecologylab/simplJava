/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Pref for a Boolean
 * @author andruid
 *
 */

@xml_inherit
public class PrefBoolean extends Pref<Boolean>
{
    /**
     * Value of Pref
     */
    @xml_attribute boolean			value;
	
	/**
	 * 
	 */
	public PrefBoolean()
	{
		super();
	}
    /**
     * Instantiate Pref to value
     * 
     * @param value
     */
	public PrefBoolean(boolean value)
	{
		super();
		this.value	= value;
	}

    /**
     * Get the value of the Pref
     * 
     * @return  The value of the Pref
     */
	@Override
	Boolean getValue()
	{
		return value;
	}
	
    /**
     * Set the value of the Pref given a Boolean
     * (big B)
     * 
     * @param  The Boolean value the Pref will be set to
     */
	public void setValue(Boolean newValue)
	{
		setValue(newValue.booleanValue());
	}
    /**
     * Set the value of the Pref given a boolean
     * (small b)
     * 
     * @param  The boolean value the Pref will be set to
     */
	public void setValue(boolean value)
	{
		invalidate();
		this.value	= value;
	}
}
