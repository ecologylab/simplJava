package ecologylab.xml;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * An ElementState XML tree node that supports an ArrayList of children (as well
 * as whatever else you add to it).
 * 
 * @author andruid
 */
public class HashSetState<T extends ElementState> extends ElementState implements Cloneable, Iterable<T>
{
    private @xml_nested HashSet<T> set = null;

    public HashSetState()
    {
        super();
    }
    
    protected HashSet<T> set()
    {
        HashSet<T>   result  = set;
        if (result == null)
        {
            result          = new HashSet<T>();
            set             = result;
        }
        return result;
    }
    
    public boolean add(T elementState)
    {
        return set().add(elementState);
    }

    public Iterator<T> iterator()
    {
        return set().iterator();
    }

    /**
     * Return the collection object associated with this
     * 
     * @return The ArrayList we collect in.
     */
    protected Collection getCollection(Class thatClass)
    {
        return set;
    }

    /**
     * Remove all elements from our Collection.
     * 
     */
    public void clear()
    {
        if (set != null) 
            set.clear();
    }

    /**
     * Get the number of elements in the set.
     * 
     * @return
     */
    public int size()
    {
        return (set == null ? 0 : set.size());
    }

    @SuppressWarnings("unchecked") public Object clone()
    {
        HashSetState clone = new HashSetState();

        clone.set = (HashSet<ElementState>) this.set().clone();

        return clone;
    }
}
