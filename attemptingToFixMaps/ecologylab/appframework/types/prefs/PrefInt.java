/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;

/**
 * Pref for an Integer
 * @author andruid
 *
 */

@xml_inherit
public class PrefInt extends Pref<Integer>
{
    /**
     * Value of Pref
     */
    @xml_attribute int			value;
	
	/**
	 * 
	 */
	public PrefInt()
	{
		super();
	}
    /**
     * Instantiate Pref to value
     * 
     * @param value
     */
	public PrefInt(int value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Integer getValue()
	{
		return value;
	}
	
    /**
     * Set the value of the Pref given an Integer
     * 
     * @param  The Integer value the Pref will be set to
     */
	public void setValue(Integer newValue)
	{
		setValue(newValue.intValue());
	}
    /**
     * Set the value of the Pref given an int
     * 
     * @param  The int value the Pref will be set to
     */
	public void setValue(int value)
	{
		this.value	= value;
        
        prefChanged();
	}
}
