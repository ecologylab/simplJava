/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.scalar.TypeRegistry;
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
		super(TypeRegistry.getType(Integer.class));
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

    @Override
    public Integer getInstance(String string)
    {
        // TODO Auto-generated method stub
        return null;
    }
	
/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}