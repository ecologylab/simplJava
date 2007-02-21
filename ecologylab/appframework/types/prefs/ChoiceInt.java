package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_attribute;

@xml_inherit
public class ChoiceInt extends Choice<Integer>
{
    @xml_attribute int      value;

    public ChoiceInt()
    {
        super();
    }
    
    public void setValue(Integer newValue)
    {
        this.value = newValue;
    }
    
    public Integer getValue()
    {
        return this.value;
    }
}
