/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;

/**
 * Preference that is a String
 * @author andruid
 *
 */
@xml_inherit
public class PrefString extends Pref<String>
{
    /**
     * Value of Pref
     */
    @xml_attribute String			value;
	
	/**
	 * 
	 */
	public PrefString()
	{
		super();
	}
    /**
     * Instantiate Pref to value
     * 
     * @param value
     */
	public PrefString(String value)
	{
		super();
		this.value	= value;
	}

	/**
     * Get the value of the Pref
     * 
	 * @return	The value of the Pref
	 */
	@Override
	String getValue()
	{
		return value;
	}
	
    /**
     * Set the value of the Pref
     * 
     * @param  The value the Pref will be set to
     */
    @Override
	public void setValue(String value)
	{
		invalidate();
		this.value	= value;
        
        this.prefUpdated();
	}
}
