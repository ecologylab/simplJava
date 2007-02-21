/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * @author andruid
 *
 */

@xml_inherit
public class PrefFloat extends Pref<Float>
{
    @xml_attribute float			value;
	
	/**
	 * 
	 */
	public PrefFloat()
	{
		super();
	}
	public PrefFloat(float value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Float getValue()
	{
		return value;
	}
	
	public void setValue(Float newValue)
	{
		setValue(newValue.floatValue());
	}
	public void setValue(float value)
	{
		invalidate();
		this.value	= value;
	}
}
