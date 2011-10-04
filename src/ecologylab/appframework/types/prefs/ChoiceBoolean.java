/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;


/**
 * A Boolean Choice object, for a multi-choice preference.
 * @author awebb
 *
 */
@simpl_inherit
public class ChoiceBoolean extends Choice<Boolean> 
{
    /**
     * Value of the choice
     */
    @simpl_scalar boolean      value;


	/**
	 * 
	 */
	public ChoiceBoolean() 
	{
		super();
	}

    /**
     * Get the value of a choice
     * 
     * @return value    Value of the choice
     */
	@Override
	public Boolean getValue() 
	{
		return value;
	}

    /**
     * Set the value of a choice.
     * 
     * @param newValue  New value the choice will be set to.
     */
	@Override public void setValue(Boolean newValue) 
	{
		this.value	= newValue;
	}

}
