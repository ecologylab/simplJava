/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.TypeRegistry;

/**
 * Metadata about a String Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@simpl_inherit
public class MetaPrefString extends MetaPref<String>
{
    /**
     * Default value for this MetaPref
     */
	@simpl_scalar	String		defaultValue;
	
	public static final ScalarType STRING_SCALAR_TYPE	= TypeRegistry.getScalarType(String.class);

    /**
     * Instantiate.
     */
	public MetaPrefString()
	{
        super(STRING_SCALAR_TYPE);
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
    @Override
	public String getDefaultValue()
	{
		return defaultValue;
	}

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return
     */
	protected @Override Pref<String> getPrefInstance()
	{
		return new PrefString();
	}

    /**
     * Get max value; returns null for this type.
     */
    @Override
    public String getMaxValue()
    {
        return null;
    }

    /**
     * Get min value; returns null for this type.
     */
    @Override
    public String getMinValue()
    {
        return null;
    }

    @Override
    public String getInstance(String string)
    {
        return string;
    }
	
/*
	public boolean isWithinRange(String newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}