package ecologylab.xml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * An ElementState XML tree node that supports an ArrayList of children (as well as whatever else
 * you add to it).
 * 
 * @author andruid
 */
public class ArrayListState extends ElementState implements Cloneable //, Iterable
{
	@xml_nested	public ArrayList set;

    public ArrayListState()
    {
        super();
    }
    /**
     * Use lazy evaluation for creating the set, in order to make it possible this
     * class lightweight enough to use in subclass situations where they may be no elements
     * added to the set, where the ElementState is only being used for direct fields.
     * @return
     */
    protected ArrayList set()
    {
    	ArrayList	result	= set;
    	if (result == null)
    	{
    		result			= new ArrayList();
    		set			 	= result;
    	}
    	return result;
    }
    public void add(ElementState elementState)
    {
        set().add(elementState);
    }

    public ElementState remove(int i)
    {
        return (set == null) ? null : (ElementState) set.remove(i);
    }

    public Iterator iterator()
    {
        return set().iterator();
    }

    public void add(int i, ElementState obj)
    {
        set().add(i, obj);
    }

    /**
     * @param i
     *            the index of the element to get.
     * @return the element located at i; if i is greater than the size of set or less than 0,
     *         returns null.
     */
    public ElementState get(int i)
    {
    	if (set == null)
    		return null;
    	
        if (i < 0)
        {
            return null;
        }
        else if (i >= set.size())
        {
            return null;
        }
        else
        {
            return (ElementState) set.get(i);
        }
    }
    
    public boolean contains(Object o)
    {
    	return (set == null) ? false : set.contains(o);
    }
    
    /**
     * Return the collection object associated with this
     * 
     * @return	The ArrayList we collect in.
     */
	protected Collection getCollection(Class thatClass)
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
        return (set == null) ? 0 : set.size();
    }

    public Object clone()
    {
        ArrayListState clone = new ArrayListState();

        clone.set = (ArrayList) this.set.clone();

        return clone;
    }
    
    public void trimToSize()
    {
        if (set != null)
        	set.trimToSize();
    }
    public Object[] toArray()
    {
        return set().toArray();
    }
}
