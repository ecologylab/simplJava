package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;

public class Choice<T> extends ElementState
{
    String      name;
    T           value;

    public Choice()
    {
        super();
    }

    public void setValue(T newValue)
    {
        value = newValue;
    }
    
    public T getValue()
    {
        return value;
    }
    
    public String getName()
    {
        return name;
    }
}
