package ecologylab.xml.types.element;

import java.util.Collection;
import java.util.Vector;

import ecologylab.xml.ElementState;

/**
 * An ElementState XML tree node for collecting a set of nested elements, using a Vector
 * (synchronized).
 * <p/> In general, one should use {@link ArrayListState ArrayListState}
 * for this kind of functionality, but, in some cases, there may be concurrency
 * issues, in which case, this more expensive class will be required.
 * 
 * @author andruid
 */
public class VectorState<T extends ElementState> extends ElementState
{
    public Vector<T> set = new Vector<T>();

    public VectorState()
    {
        super();
    }

    public void add(T elementState)
    {
    	set.add(elementState);
    }

    /**
     * Return the collection object associated with this
     * 
     * @return	The ArrayList we collect in.
     */
	@SuppressWarnings("unchecked") @Override protected Collection<? extends ElementState> getCollection(Class thatClass)
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

    /**
     * @see ecologylab.xml.ElementState#recycle()
     */
    @Override public void recycle()
    {
        if (this.set != null)
        {
            for (int i = 0; i < this.set.size(); i++)
            {
                T e = set.remove(i);
                e.recycle();
            }
            
            this.clear();
        }
            
        super.recycle();
    }
}
