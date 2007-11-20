/*
 * Created on Nov 19, 2007
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Queue implementation backed by an ArrayList.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class ArrayListQueue<T> implements Queue<T>
{
	/** The backing list for the Queue. */
	private ArrayList<T>	list;

	/**
	 * Tracks the largest capacity the backing list has had; used to ensure that
	 * it is contracted when necessary, to conserve memory.
	 */
	private int				largestCapacity;

	/**
	 * 
	 */
	public ArrayListQueue()
	{
		this(0);
	}

	/**
	 * @param initialCapacity
	 */
	public ArrayListQueue(int initialCapacity)
	{
		list = new ArrayList<T>(initialCapacity);

		largestCapacity = list.size();
	}

	/**
	 * @see java.util.Queue#element()
	 */
	public T element()
	{
		if (list.size() > 0)
		{
			return this.peek();
		}

		throw new NoSuchElementException(
				"Backing array is empty; cannot retrieve head.");
	}

	/**
	 * @see java.util.Queue#offer(java.lang.Object)
	 */
	public boolean offer(T o)
	{
		this.list.add(o);

		int listSize;
		if ((listSize = list.size()) > this.largestCapacity)
			largestCapacity = listSize;

		return true;
	}

	/**
	 * @see java.util.Queue#peek()
	 */
	public T peek()
	{
		return list.size() > 0 ? list.get(0) : null;
	}

	/**
	 * @see java.util.Queue#poll()
	 */
	public T poll()
	{
		T retVal = null;

		synchronized (list)
		{
			retVal = (list.size() > 0) ? list.remove(0) : null;

			int listSize = list.size();
			if ((listSize * 4) <= this.largestCapacity)
			{
				// TODO this is kludge, but it doesn't seem possible to do anything
				// better w/ ArrayList :(
				this.list.trimToSize();
				this.list.ensureCapacity(listSize * 2);
				this.largestCapacity = listSize * 2;
			}
		}

		return retVal;
	}

	/**
	 * @see java.util.Queue#remove()
	 */
	public T remove()
	{
		synchronized (list)
		{
			if (list.size() > 0)
			{
				return this.poll();
			}
		}
		throw new NoSuchElementException(
				"Backing array is empty; cannot retrieve head.");
	}

	/**
	 * Alias for offer(T)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(T o)
	{
return		this.offer(o);
	}

	/**
	 * Calls offer(T) for each element of the input collection.
	 * 
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends T> c)
	{
		for (T obj : c)
		{
			this.offer(obj);
		}
		
		return true; // this version of offer always returns true, because there's no way for it to fail
	}

	public void clear()
	{
		this.list.clear();
		this.largestCapacity = 0;
	}

	public boolean contains(Object o)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsAll(Collection<?> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEmpty()
	{
		// TODO Auto-generated method stub
		return false;
	}

	public Iterator<T> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public boolean remove(Object o)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Collection<?> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection<?> c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	public Object[] toArray()
	{
		// TODO Auto-generated method stub
		return null;
	}

	public <T> T[] toArray(T[] a)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
