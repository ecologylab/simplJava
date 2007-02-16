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
public class PrefBoolean extends Pref<Boolean>
{
    @xml_attribute boolean			value;
	
	/**
	 * 
	 */
	public PrefBoolean()
	{
		super();
	}
	public PrefBoolean(boolean value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Boolean getValue()
	{
		return value;
	}
	
	public void setValue(Boolean newValue)
	{
		setValue(newValue.booleanValue());
	}
	public void setValue(boolean value)
	{
		invalidate();
		this.value	= value;
	}
}
