/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Metadata about a Float Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public class MetaPrefFloat extends MetaPref<Float>
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	float		        defaultValue;
    /**
     * Min/max values
     */
    @xml_nested     RangeFloatState     range;
	
    /**
     * Instantiate.
     */
	public MetaPrefFloat()
	{
        super(TypeRegistry.getType(Float.class));
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
	public Float getDefaultValue()
	{
		return defaultValue;
	}

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
	protected @Override Pref<Float> getPrefInstance()
	{
		return new PrefFloat();
	}

    /**
     * Get max value for this MetaPref.
     */
    @Override
    public Float getMaxValue()
    {
        return range.getMax();
    }

    /**
     * Get min value for this MetaPref.
     */
    @Override
    public Float getMinValue()
    {
        return range.getMin();
    }

    @Override
    public Float getInstance(String string)
    {
        // return scalarType.getInstance(string);
        return new Float(string);
    }
	
/*
	public boolean isWithinRange(Float newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}