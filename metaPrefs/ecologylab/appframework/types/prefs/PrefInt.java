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
public class PrefInt extends Pref<Integer>
{
    @xml_attribute int			value;
	
	/**
	 * 
	 */
	public PrefInt()
	{
		super();
	}
	public PrefInt(int value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	Integer getValue()
	{
		return value;
	}
	
	public void setValue(Integer newValue)
	{
		setValue(newValue.intValue());
	}
	public void setValue(int value)
	{
		invalidate();
		this.value	= value;
	}
}
