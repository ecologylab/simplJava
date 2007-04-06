package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;

abstract public class RangeState<T extends Comparable> extends ElementState
{
	T	min;
	T	max;
	
	public RangeState()
	{
		super();
	}

	abstract protected boolean isWithinRange(T newValue);
/*	protected boolean isWithinRange(T newValue)
	{
		return (newValue >= min) && (newValue <= max);
	}
 */
}
