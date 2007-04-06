/**
 * 
 */
package ecologylab.appframework.types.prefs;

/**
 * @author andruid
 *
 */
public class RangeIntState extends RangeState<Integer>
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
	@Override
	protected boolean isWithinRange(Integer newValue)
	{
		int value	= newValue.intValue();
		return (min <= value) && (value <= max);
	}

}
