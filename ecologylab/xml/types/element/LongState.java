package ecologylab.xml.types.element;

import ecologylab.xml.ElementState;


/**
 * Reference version of an long type. Re-writable, unlike java.lang.longeger. <p/>
 * Object wrapper for long primitive values. Useful for storing in HashMaps, and
 * anywhere else that a reference type is needed.
 */
public class LongState extends ElementState implements Comparable<LongState>
{
    private @xml_attribute long value;

    public LongState(long b)
    {
        super();
        value = b;
    }

    /**
     * 
     */
    public LongState()
    {
    }

    /**
     * Returns a hashcode for this Long.
     * 
     * @return a hash code value for this object.
     * @since JDK1.0
     */
    @Override public int hashCode()
    {
        return (int) value;
    }

    @Override public String toString()
    {
        return "longState[" + value + "]";
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override public boolean equals(Object arg0)
    {
        return (arg0 != null) 
            && (((arg0 instanceof LongState) && (((LongState)arg0).value == value)));
    }
    
    public int compareTo(LongState arg0)
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
    public long getValue()
    {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(long value)
    {
        this.value = value;
    }
}
