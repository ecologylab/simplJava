/**
 * 
 */
package ecologylab.appframework.types.prefs;

import java.awt.Color;

/**
 * @author awebb
 *
 */
public class MetaPrefColor extends MetaPref<Color> 
{

	@xml_attribute	Color		defaultValue;
	
	public MetaPrefColor()
	{
		super();
	}
	
	@Override
	public Color getDefaultValue() 
	{
		return (defaultValue != null) ? defaultValue : Color.BLACK;
	}

	@Override
	protected Pref<Color> getPrefInstance() 
	{
		return new PrefColor();
	}

}
