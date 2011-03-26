/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.types.scalar.ScalarType;
import ecologylab.serialization.types.scalar.TypeRegistry;

/**
 * Metadata about a Float Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@simpl_inherit
public class MetaPrefFloat extends MetaPref<Float>
{
    /**
     * Default value for this MetaPref
     */
	@simpl_scalar	float		        defaultValue;
    /**
     * Min/max values
     */
    @simpl_composite     RangeFloatState     range;
	
	public static final ScalarType FLOAT_SCALAR_TYPE	= TypeRegistry.getType(float.class);

	     /**
     * Instantiate.
     */
	public MetaPrefFloat()
	{
        super(FLOAT_SCALAR_TYPE);
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
     * Get max value for this MetaPref; returns null if it is not defined.
     */
    @Override
    public Float getMaxValue()
    {
        if (range != null)
            return range.getMax();
        else
            return null;
    }

    /**
     * Get min value for this MetaPref; returns null if it is not defined.
     */
    @Override
    public Float getMinValue()
    {
        if (range != null)
            return range.getMin();
        else
            return null;
    }

//    @Override
//    public Float getInstance(String string)
//    {
//        // return scalarType.getInstance(string);
//        return new Float(string);
//    }
	
/*
	public boolean isWithinRange(Float newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}