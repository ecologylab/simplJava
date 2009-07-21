package ecologylab.xml.types.element;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import ecologylab.xml.ElementState;

/**
 * An ElementState XML tree node that supports an ArrayList of children (as well
 * as whatever else you add to it).
 * 
 * @author andruid
 */
public class HashSetState<T extends ElementState> extends ElementState implements Cloneable, Iterable<T>
{
    @xml_collection protected HashSet<T> set;

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
    @SuppressWarnings("unchecked") @Override protected Collection<? extends ElementState> getCollection(Class thatClass)
    {
        return set();
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

    @Override @SuppressWarnings("unchecked") public HashSetState<T> clone()
    {
        HashSetState<T> clone = new HashSetState<T>();

        if (set != null)
            clone.set = (HashSet<T>) this.set().clone();

        return clone;
    }

    /**
     * Clear data structures and references to enable garbage collecting of resources associated with this.
     * 
     * Calling recycle() on an HashSetState has the side effect of recycling every value contained in the HashSetState.
     */
    @Override public void recycle()
    {
        if (this.set != null)
        {
            Iterator<T> elementIterator = set.iterator();

            while (elementIterator.hasNext())
            {
                T element = elementIterator.next();
                
                element.recycle();
                
                elementIterator.remove();
            }
            
            this.clear();
        }
        
        super.recycle();
    }
}
