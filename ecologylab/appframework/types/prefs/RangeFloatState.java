/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * @author andruid
 *
 */
@simpl_inherit
public class RangeFloatState extends ElementState /* RangeState<Integer> */
{
    /**
     * Min value.
     */
    @simpl_scalar  float     min;
    /**
     * Max value.
     */
    @simpl_scalar  float     max;
    
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
