package ecologylab.appframework.types.prefs;

import ecologylab.xml.ElementState;

abstract public class RangeState<T extends Comparable> extends ElementState
{
	//@xml_attribute T	min;
	//@xml_attribute T	max;
	
	public RangeState()
	{
		super();
	}

    /**
     * Whether or not the value is in the range.
     * @param newValue
     */
	abstract protected boolean isWithinRange(T newValue);
    /**
     * Get the min of a choice. Type-specific.
     */
    public abstract T getMin();
    
    /**
     * Set the min of a choice. Type-specific.
     */
    public abstract void setMin(T newValue);
    /**
     * Get the max of a choice. Type-specific.
     */
    public abstract T getMax();
    
    /**
     * Set the max of a choice. Type-specific.
     */
    public abstract void setMax(T newValue);
/*	protected boolean isWithinRange(T newValue)
	{
		return (newValue >= min) && (newValue <= max);
	}
 */
}
