package ecologylab.xml;


/**
 * Reference version of an int type. Re-writable, unlike java.lang.Integer. <p/>
 * Object wrapper for int primitive values. Useful for storing in HashMaps, and
 * anywhere else that a reference type is needed.
 */
public class IntState extends ElementState
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

        // TODO Auto-generated constructor stub
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
        return "IntSlot[" + value + "]";
    }
}
