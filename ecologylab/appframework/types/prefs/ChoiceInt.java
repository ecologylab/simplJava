package ecologylab.appframework.types.prefs;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * An Integer Choice object, for a multi-choice preference.
 * @author awebb
 *
 */
@simpl_inherit
public class ChoiceInt extends Choice<Integer>
{
    /**
     * Value of the choice
     */
    @simpl_scalar int      value;

    public ChoiceInt()
    {
        super();
    }
    
    /**
     * Get the value of a choice
     * 
     * @return value    Value of the choice
     */
    @Override public void setValue(Integer newValue)
    {
        this.value = newValue;
    }
    
    /**
     * Set the value of a choice.
     * 
     * @param newValue  New value the choice will be set to.
     */
    @Override public Integer getValue()
    {
        return this.value;
    }
}
