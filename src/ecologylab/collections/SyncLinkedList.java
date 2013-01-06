package ecologylab.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * A synchronized version of {@link java.util.LinkedList LinkedList},
 * because they didn't seem to provided a nice one.
 */
public class SyncLinkedList
implements List
{
	LinkedList list;

	public SyncLinkedList()
	{
	   this(new LinkedList());
	}
	public SyncLinkedList(LinkedList list)
	{
	    this.list = list;
	}

	@Override
	public boolean equals(Object o) {
	    synchronized(list) {return list.equals(o);}
        }
	@Override
	public int hashCode() {
	    synchronized(list) {return list.hashCode();}
        }

	@Override
	public Object get(int index) {
	    synchronized(list) {return list.get(index);}
        }
	@Override
	public Object set(int index, Object element) {
	    synchronized(list) {return list.set(index, element);}
        }
	@Override
	public void add(int index, Object element) {
	    synchronized(list) {list.add(index, element);}
        }
	public void addFirst(Object o)
	{
	    synchronized(list) {list.addFirst(o);}
	}
	public void addLast(Object o) 
	{
	   synchronized(list) {list.addLast(o);}
	}
	@Override
	public Object remove(int index) {
	    synchronized(list) {return list.remove(index);}
        }

	public Object removeFirst() 
	{
	    synchronized(list) {return list.removeFirst();}
	}
	public Object removeLast() 
	{
	    synchronized(list) {return list.removeLast();}
	}
	public Object getLast() 
	{
	    synchronized(list) {return list.getLast();}
	}
	
	@Override
	public int indexOf(Object o) {
	    synchronized(list) {return list.indexOf(o);}
        }
	@Override
	public int lastIndexOf(Object o) {
	    synchronized(list) {return list.lastIndexOf(o);}
        }

	@Override
	public boolean addAll(int index, Collection c) {
	    synchronized(list) {return list.addAll(index, c);}
        }

	@Override
	public ListIterator listIterator() {
	    return list.listIterator(); // Must be manually synched by user
        }

	@Override
	public ListIterator listIterator(int index) {
	    return list.listIterator(index); // Must be manually synched by usr
        }

	@Override
	public List subList(int fromIndex, int toIndex) {
	    synchronized(list) {
	       return list.subList(fromIndex, toIndex);
            }
        }

	@Override
	public int size() {
	    synchronized(list) {return list.size();}
        }
	@Override
	public boolean isEmpty() {
	    synchronized(list) {return list.isEmpty();}
        }
	@Override
	public boolean contains(Object o) {
	    synchronized(list) {return list.contains(o);}
        }
	@Override
	public Object[] toArray() {
	    synchronized(list) {return list.toArray();}
        }
	@Override
	public Object[] toArray(Object[] a) {
	    synchronized(list) {return list.toArray(a);}
        }

	@Override
	public Iterator iterator() {
            return list.iterator(); // Must be manually synched by user!
        }

	@Override
	public boolean add(Object o) {
	    synchronized(list) {return list.add(o);}
        }
	@Override
	public boolean remove(Object o) {
	    synchronized(list) {return list.remove(o);}
        }

	@Override
	public boolean containsAll(Collection coll) {
	    synchronized(list) {return list.containsAll(coll);}
        }
	@Override
	public boolean addAll(Collection coll) {
	    synchronized(list) {return list.addAll(coll);}
        }
	@Override
	public boolean removeAll(Collection coll) {
	    synchronized(list) {return list.removeAll(coll);}
        }
	@Override
	public boolean retainAll(Collection coll) {
	    synchronized(list) {return list.retainAll(coll);}
        }
	@Override
	public void clear() {
	    synchronized(list) {list.clear();}
        }
	@Override
	public String toString() {
	    synchronized(list) {return list.toString();}
        }
    }
