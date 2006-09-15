package ecologylab.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * An ElementState XML tree node that supports an ArrayList of children (as well as whatever else
 * you add to it).
 * 
 * @author andruid
 */
public class HashSetState extends ElementState implements Cloneable
{
    public HashSet set = new HashSet();

    public HashSetState()
    {
        super();
    }

    public boolean add(ElementState elementState)
    {
        return set.add(elementState);
    }

    public Iterator iterator()
    {
        return set.iterator();
    }

    /**
     * Return the collection object associated with this
     * 
     * @return	The ArrayList we collect in.
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
        set.clear();
    }

    /**
     * Get the number of elements in the set.
     * 
     * @return
     */
    public int size()
    {
        return set.size();
    }

    public Object clone()
    {
        HashSetState clone = new HashSetState();

        clone.set = (HashSet) this.set.clone();

        return clone;
    }
}
