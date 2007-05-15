/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.appframework.types.prefs.MetaPref;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * Metadata about a String Preference.
 * Defines information to enable editing the Preference.
 * 
 * @author andruid
 *
 */

@xml_inherit
public class MetaPrefString extends MetaPref<String>
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	String		defaultValue;
	
    /**
     * Instantiate.
     */
	public MetaPrefString()
	{
        super(TypeRegistry.getType(String.class));
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref
     */
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