/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;

import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * @author awebb
 *
 */
public class MetaPrefColor extends MetaPref<Color> 
{
    /**
     * Default value for this MetaPref
     */
	@xml_attribute	Color		defaultValue;
	
	public static final ScalarType COLOR_SCALAR_TYPE	= TypeRegistry.getType(Color.class);

	public MetaPrefColor()
	{
	    super(COLOR_SCALAR_TYPE);
	}
	
    /**
     * Gets the default value of a MetaPref. 
     * 
     * @return Default value of MetaPref, or Black if null.
     */
	@Override
	public Color getDefaultValue() 
	{
		return (defaultValue != null) ? defaultValue : Color.BLACK;
	}

    /**
     * Construct a new instance of the Pref that matches this.
     * Use this to fill-in the default value.
     * 
     * @return new Pref instance
     */
	@Override
	protected Pref<Color> getPrefInstance() 
	{
		return new PrefColor();
	}

    /**
     * Get max value; returns null for this type.
     */
    @Override
    public Color getMaxValue()
    {
        return null;
    }

    /**
     * Get min value; returns null for this type.
     */
    @Override
    public Color getMinValue()
    {
        return null;
    }

}
