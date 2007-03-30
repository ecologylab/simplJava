/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * Pref for a Float
 * 
 * @author andruid
 *
 */

@xml_inherit
public class PrefFloat extends Pref<Float>
{
    /**
     * Value of Pref
     */
    @xml_attribute float			value;
	
	/**
	 * 
	 */
	public PrefFloat()
	{
		super();
	}
    /**
     * Instantiate Pref to value
     * 
     * @param value
     */
	public PrefFloat(float value)
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
	Float getValue()
	{
		return value;
	}
	
    /**
     * Set the value of the Pref given a Float
     * (big F)
     * 
     * @param  The Float value the Pref will be set to
     */
	public void setValue(Float newValue)
	{
		setValue(newValue.floatValue());
	}
    /**
     * Set the value of the Pref given a float
     * (small f)
     * 
     * @param  The float value the Pref will be set to
     */
	public void setValue(float value)
	{
		invalidate();
		this.value	= value;
	}
}
