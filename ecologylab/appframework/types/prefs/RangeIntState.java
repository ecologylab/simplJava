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
	@xml_attribute	int		min;
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


	public Integer getMax()
	{
	    return this.max;
	}


	public Integer getMin()
	{
	    return this.min;
	}


	public void setMax(Integer newValue)
	{
	    this.max = newValue;
	}


	public void setMin(Integer newValue)
	{
        this.min = newValue;
	}

}
