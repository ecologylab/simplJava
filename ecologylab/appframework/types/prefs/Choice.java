package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;


abstract public class Choice<T> extends ElementState
{
    @xml_attribute String      name;
    @xml_attribute String      label;
    //@xml_attribute T           value;

    public Choice()
    {
        super();
    }
    
    public abstract T getValue();
    
    public abstract void setValue(T newValue);
    
    public String getName()
    {
        return this.name;
    }

    public String getLabel()
    {
        return this.label;
    }
}
