/**
 * 
 */
package ecologylab.appframework.types.prefs;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * @author andruid
 *
 */
@simpl_inherit
public class RangeIntState extends ElementState /* RangeState<Integer> */
{
    /**
     * Min value.
     */
	@simpl_scalar	int		min;
    /**
     * Max value.
     */
	@simpl_scalar	int		max;
	
	/**
	 * 
	 */
	public RangeIntState()
	{
		super();
	}

/**
 * Check to see that the newValue is within the range specifed by this.
 * @param newValue
 * @return
 */
	protected boolean isWithinRange(Integer newValue)
	{
		int value	= newValue.intValue();
		return (min <= value) && (value <= max);
	}

    /**
     * Get max value.
     */
	public Integer getMax()
	{
	    return this.max;
	}

    /**
     * Get min value.
     */
	public Integer getMin()
	{
	    return this.min;
	}

    /**
     * Set max value.
     */
	public void setMax(Integer newValue)
	{
	    this.max = newValue;
	}

    /**
     * Set min value.
     */
	public void setMin(Integer newValue)
	{
        this.min = newValue;
	}

}
