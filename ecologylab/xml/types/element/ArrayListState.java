package ecologylab.xml.types.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ecologylab.xml.ElementState;

/**
 * An ElementState XML tree node for collecting a set of nested elements, using an ArrayList
 * (non-synchronized).
 * 
 * @author andruid
 */
public class ArrayListState<T extends ElementState> extends ElementState implements Cloneable, Iterable<T>//, List<T>
{
    @xml_collection protected ArrayList<T> set;

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
    protected ArrayList<T> set()
    {
        ArrayList<T>    result  = set;
        
        if (result == null)
        {
            result          = new ArrayList<T>();
            set             = result;
        }
        
        return result;
    }
    
    /**
     * Returns the underlying ArrayList implementation.
     * @return
     */
    public ArrayList<T> getArrayList()
    {
        return set();
    }
    
    public boolean add(T elementState)
    {
        return set().add(elementState);
    }

    public T remove(int i)
    {
        return (set == null) ? null : set.remove(i);
    }

    public Iterator<T> iterator()
    {
        return set().iterator();
    }

    public void add(int i, T obj)
    {
        set().add(i, obj);
    }

    /**
     * @param i
     *            the index of the element to get.
     * @return the element located at i; if i is greater than the size of set or less than 0,
     *         returns null.
     */
    public T get(int i)
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
            return set.get(i);
        }
    }
    
    public boolean contains(Object o)
    {
        return (set == null) ? false : set.contains(o);
    }
    
    /**
     * Return the collection object associated with this
     * 
     * @return  The ArrayList we collect in.
     */
    protected Collection<? extends ElementState> getCollection(Class thatClass)
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

    @SuppressWarnings("unchecked") public ArrayListState<T> clone()
    {
        ArrayListState<T> clone = new ArrayListState<T>();

        if (set != null)
            clone.set = (ArrayList<T>) this.set.clone();

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

    public boolean addAll(Collection<? extends T> c)
    {
        return set().addAll(c);
    }

    public boolean containsAll(Collection<?> c)
    {
        return set().containsAll(c);
    }

    public boolean isEmpty()
    {
        return (set == null ? true : set.isEmpty());
    }

    public boolean remove(Object o)
    {
        return (set != null) && set.remove(o);
    }

    public boolean removeAll(Collection<?> c)
    {
        return (set != null) && set.removeAll(c);
    }

    public boolean retainAll(Collection<?> c)
    {
        return (set == null ? false : set.retainAll(c));
    }

    @SuppressWarnings("hiding") public <T> T[] toArray(T[] a)
    {
        return set().toArray(a);
    }

    public boolean addAll(int index, Collection<? extends T> c)
    {
        return set().addAll(index, c);
    }

    public int indexOf(Object elem)
    {
        return (set == null ? -1 : set.indexOf(elem));
    }

    public int lastIndexOf(Object elem)
    {
        return (set == null ? -1 : set.indexOf(elem));
    }

    public ListIterator<T> listIterator()
    {
        return set().listIterator();
    }

    public ListIterator<T> listIterator(int index)
    {
        return set().listIterator(index);
    }

    public T set(int index, T element)
    {
        return set().set(index, element);
    }

    public List<T> subList(int fromIndex, int toIndex)
    {
        return set().subList(fromIndex, toIndex);
    }

    /**
     * Clear data structures and references to enable garbage collecting of resources associated with this.
     */
    public void recycle()
    {
    	if (set != null)
    	{
			int last	= size() - 1;
			for (int i=last; i>=0; i--)
			{
				ElementState es	= remove(i);
				if (es != null)
					es.recycle();
			}
    		set.clear();
    	}
    	super.recycle();
    }
}
