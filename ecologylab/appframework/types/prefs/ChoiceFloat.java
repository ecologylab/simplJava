package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

/**
 * A Float Choice object, for a multi-choice preference.
 * @author awebb
 *
 */
@xml_inherit
public class ChoiceFloat extends Choice<Float>
{
    /**
     * Value of the choice
     */
    @xml_attribute float      value;

    public ChoiceFloat()
    {
        super();
    }
    
    /**
     * Get the value of a choice
     * 
     * @return value    Value of the choice
     */
    @Override public void setValue(Float newValue)
    {
        this.value = newValue;
    }
    
    /**
     * Set the value of a choice.
     * 
     * @param newValue  New value the choice will be set to.
     */
    @Override public Float getValue()
    {
        return this.value;
    }
}
