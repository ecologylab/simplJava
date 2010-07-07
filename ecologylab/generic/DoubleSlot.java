package ecologylab.generic;

import java.lang.Object;

import ecologylab.serialization.ElementState;

/**
 * Reference version of a double type. Re-writable, unlike java.lang.Double.
 */
public class DoubleSlot extends ElementState
{
    public double value = 0;
    
    /**
     * Used for ElementState.
     *
     */
    public DoubleSlot()
    {
        super();
    }

    public DoubleSlot(double d)
    {
        super();
        value = d;
    }
}
