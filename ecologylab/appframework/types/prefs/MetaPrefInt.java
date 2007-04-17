/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;

/**
 * Metadata about an Integer Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
@xml_inherit
public class MetaPrefInt extends MetaPref<Integer>
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	int		defaultValue;
	
    /**
     * Instantiate.
     */
	public MetaPrefInt()
	{
		super();
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    public Integer getDefaultValue()
	{
		return defaultValue;
	}

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
	protected @Override Pref<Integer> getPrefInstance()
	{
		return new PrefInt();
	}
	
/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}