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
public class PrefString extends Pref<String>
{
    @xml_attribute String			value;
	
	/**
	 * 
	 */
	public PrefString()
	{
		super();
	}
	public PrefString(String value)
	{
		super();
		this.value	= value;
	}

	/**
	 * @return	The
	 */
	@Override
	String getValue()
	{
		return value;
	}
	
	public void setValue(String value)
	{
		invalidate();
		this.value	= value;
	}
}
