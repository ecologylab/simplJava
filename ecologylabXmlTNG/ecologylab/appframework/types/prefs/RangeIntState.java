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
public class RangeIntState extends ElementState /* RangeState<Integer> */
{
    /**
     * Min value.
     */
	@xml_attribute	int		min;
    /**
     * Max value.
     */
	@xml_attribute	int		max;
	
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
