package ecologylab.xml;


/**
 * Reference version of an int type. Re-writable, unlike java.lang.Integer. <p/>
 * Object wrapper for int primitive values. Useful for storing in HashMaps, and
 * anywhere else that a reference type is needed.
 */
public class IntState extends ElementState implements Comparable
{
    public int value;

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
    public int hashCode()
    {
        return value;
    }

    public String toString()
    {
        return "IntState[" + value + "]";
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        return (arg0 != null) 
            && (((arg0 instanceof IntState) && (((IntState)arg0).value == value)));
    }
    
    public int compareTo(Object arg0)
    {
        if (arg0 == null)
            throw new NullPointerException();
        
        if (!(arg0 instanceof IntState))
            throw new ClassCastException();
        
        if (value > ((IntState)arg0).value)
            return 1;
        else if (value == ((IntState)arg0).value)
            return 0;
        else
            return -1;
    }
}
