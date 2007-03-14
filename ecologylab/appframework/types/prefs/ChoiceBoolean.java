/**
 * 
 */
package ecologylab.appframework.types.prefs;


/**
 * @author awebb
 *
 */
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
