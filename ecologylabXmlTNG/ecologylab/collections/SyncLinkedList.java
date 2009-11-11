package ecologylab.collections;

import java.util.*;

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

	public boolean equals(Object o) {
	    synchronized(list) {return list.equals(o);}
        }
	public int hashCode() {
	    synchronized(list) {return list.hashCode();}
        }

	public Object get(int index) {
	    synchronized(list) {return list.get(index);}
        }
	public Object set(int index, Object element) {
	    synchronized(list) {return list.set(index, element);}
        }
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
	
	public int indexOf(Object o) {
	    synchronized(list) {return list.indexOf(o);}
        }
	public int lastIndexOf(Object o) {
	    synchronized(list) {return list.lastIndexOf(o);}
        }

	public boolean addAll(int index, Collection c) {
	    synchronized(list) {return list.addAll(index, c);}
        }

	public ListIterator listIterator() {
	    return list.listIterator(); // Must be manually synched by user
        }

	public ListIterator listIterator(int index) {
	    return list.listIterator(index); // Must be manually synched by usr
        }

	public List subList(int fromIndex, int toIndex) {
	    synchronized(list) {
	       return list.subList(fromIndex, toIndex);
            }
        }

	public int size() {
	    synchronized(list) {return list.size();}
        }
	public boolean isEmpty() {
	    synchronized(list) {return list.isEmpty();}
        }
	public boolean contains(Object o) {
	    synchronized(list) {return list.contains(o);}
        }
	public Object[] toArray() {
	    synchronized(list) {return list.toArray();}
        }
	public Object[] toArray(Object[] a) {
	    synchronized(list) {return list.toArray(a);}
        }

	public Iterator iterator() {
            return list.iterator(); // Must be manually synched by user!
        }

	public boolean add(Object o) {
	    synchronized(list) {return list.add(o);}
        }
	public boolean remove(Object o) {
	    synchronized(list) {return list.remove(o);}
        }

	public boolean containsAll(Collection coll) {
	    synchronized(list) {return list.containsAll(coll);}
        }
	public boolean addAll(Collection coll) {
	    synchronized(list) {return list.addAll(coll);}
        }
	public boolean removeAll(Collection coll) {
	    synchronized(list) {return list.removeAll(coll);}
        }
	public boolean retainAll(Collection coll) {
	    synchronized(list) {return list.retainAll(coll);}
        }
	public void clear() {
	    synchronized(list) {list.clear();}
        }
	public String toString() {
	    synchronized(list) {return list.toString();}
        }
    }
