package ecologylab.xml.types.element;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_attribute;

public class StringState extends ElementState
{
    @xml_attribute public String string;
    
    public StringState()
    {
        super();
    }
    
    public StringState(String string)
    {
        this.string = string;
    }

    /**
     * @return the string
     */
    public String getString()
    {
        return string;
    }
}
