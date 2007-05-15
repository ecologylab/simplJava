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
    /**
     * Min value.
     */
    @xml_attribute  float     min;
    /**
     * Max value.
     */
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

    /**
     * Get max value.
     */
    public Float getMax()
    {
        return this.max;
    }

    /**
     * Get min value.
     */
    public Float getMin()
    {
        return this.min;
    }

    /**
     * Set max value.
     */
    public void setMax(Float newValue)
    {
        this.max = newValue;
    }

    /**
     * Set min value.
     */
    public void setMin(Float newValue)
    {
        this.min = newValue;
    }

}
