package ecologylab.serialization.types.element;

import ecologylab.serialization.ElementState;


/**
 * Reference version of an int type. Re-writable, unlike java.lang.Integer. <p/>
 * Object wrapper for int primitive values. Useful for storing in HashMaps, and
 * anywhere else that a reference type is needed.
 */
public class IntState extends ElementState implements Comparable<IntState>
{
    private @simpl_scalar int value;

    public IntState(int b)
    {
        super();
        value = b;
    }

    /**
     * 
     */
    public IntState()
    {
    }

    /**
     * Returns a hashcode for this Integer.
     * 
     * @return a hash code value for this object.
     * @since JDK1.0
     */
    @Override public int hashCode()
    {
        return value;
    }

    @Override public String toString()
    {
        return "IntState[" + value + "]";
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override public boolean equals(Object arg0)
    {
        return (arg0 != null) 
            && (((arg0 instanceof IntState) && (((IntState)arg0).value == value)));
    }
    
    public int compareTo(IntState arg0)
    {
        if (arg0 == null)
            throw new NullPointerException();
        
        if (value > arg0.value)
            return 1;
        else if (value == arg0.value)
            return 0;
        else
            return -1;
    }

    /**
     * @return the value
     */
    public int getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(int value)
    {
        this.value = value;
    }
}
