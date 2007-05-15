/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Metadata about a Boolean Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */
@xml_inherit
public class MetaPrefBoolean extends MetaPref<Boolean>
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	boolean		defaultValue;
	
	/**
	 * Instantiate.
	 */
	public MetaPrefBoolean()
	{
        super(TypeRegistry.getType(Boolean.class));
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
    
/*
	public boolean isWithinRange(Boolean newValue)
	{
		return (range == null) ? true :  range.isWithinRange(newValue);
	}
	*/
}