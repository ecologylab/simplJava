/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.TypeRegistry;

/**
 * Metadata about a Boolean Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
@simpl_inherit
public class MetaPrefBoolean extends MetaPref<Boolean>
{
    /**
     * Default value for this MetaPref
     */
	@simpl_scalar	boolean		defaultValue;
	
	public static final ScalarType BOOLEAN_SCALAR_TYPE	= TypeRegistry.getScalarType(boolean.class);

	/**
	 * Instantiate.
	 */
	public MetaPrefBoolean()
	{
        super(BOOLEAN_SCALAR_TYPE);
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
	public Boolean getDefaultValue()
	{
		return defaultValue;
	}

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return new Pref instance
     */
	protected @Override Pref<Boolean> getPrefInstance()
	{
		return new PrefBoolean();
	}

    /**
     * Get max value; returns null for this type.
     */
    @Override
    public Boolean getMaxValue()
    {
        return null;
    }

    /**
     * Get min value; returns null for this type.
     */
    @Override
    public Boolean getMinValue()
    {
        return null;
    }
    
    /**
     * Get the current PrefBoolean object associated with this.
     * If there is not one yet, create one with the default value specified in this.
     * 
     * @return
     */
    public PrefBoolean usePrefBoolean()
    {
    	return Pref.usePrefBoolean(getID(), getDefaultValue());   	
    }
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}