/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * @author andruid
 *
 */
@xml_inherit
public class RangeFloatState extends ElementState /* RangeState<Integer> */
{
    @xml_attribute  float     min;
    @xml_attribute  float     max;
    
    /**
     * 
     */
    public RangeFloatState()
    {
        super();
    }

/**
 * Check to see that the newValue is within the range specifed by this.
 * @param newValue
 * @return
 */
    protected boolean isWithinRange(Float newValue)
    {
        float value   = newValue.floatValue();
        return (min <= value) && (value <= max);
    }


    public Float getMax()
    {
        return this.max;
    }


    public Float getMin()
    {
        return this.min;
    }


    public void setMax(Float newValue)
    {
        this.max = newValue;
    }


    public void setMin(Float newValue)
    {
        this.min = newValue;
    }

}
