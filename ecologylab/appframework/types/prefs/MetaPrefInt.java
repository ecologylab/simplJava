/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.appframework.types.prefs.RangeIntState;
import ecologylab.appframework.types.prefs.RangeState;
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
	@xml_attribute	int		               defaultValue;
   
    /**
     * Min/max values
     */
    @xml_nested     RangeIntState          range;
	
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

    /**
     * Get max value for this MetaPref.
     */
    @Override
    public Integer getMaxValue()
    {
        return range.getMax();
    }

    /**
     * Get min value for this MetaPref.
     */
    @Override
    public Integer getMinValue()
    {
        return range.getMin();
    }
	
/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}