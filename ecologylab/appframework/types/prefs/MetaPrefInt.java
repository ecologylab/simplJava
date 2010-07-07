/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.net.ParsedURL;
import ecologylab.xml.simpl_inherit;
import ecologylab.xml.types.scalar.ScalarType;
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
@simpl_inherit
public class MetaPrefInt extends MetaPref<Integer>
{
    /**
     * Default value for this MetaPref
     */
	@simpl_scalar	int		               defaultValue;
   
    /**
     * Min/max values
     */
    @simpl_composite     RangeIntState          range;
	
	public static final ScalarType INT_SCALAR_TYPE	= TypeRegistry.getType(int.class);

    /**
     * Instantiate.
     */
	public MetaPrefInt()
	{
		super(INT_SCALAR_TYPE);
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
     * Get max value for this MetaPref; returns null if it is not defined.
     */
    @Override
    public Integer getMaxValue()
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
    public Integer getMinValue()
    {
        if (range != null)
            return range.getMin();
        else
            return null;
    }

/*
	public boolean isWithinRange(Integer newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}