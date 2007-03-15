/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.xml_inherit;


/**
 * @author awebb
 *
 */
@xml_inherit
public class ChoiceBoolean extends Choice<Boolean> 
{
    @xml_attribute boolean      value;


	/**
	 * 
	 */
	public ChoiceBoolean() 
	{
		super();
	}


	@Override
	public Boolean getValue() 
	{
		return value;
	}


	public void setValue(Boolean newValue) 
	{
		this.value	= newValue;
	}

}
