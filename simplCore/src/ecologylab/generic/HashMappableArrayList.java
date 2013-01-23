/**
 * 
 */
package ecologylab.generic;

import java.util.Collection;
import java.util.Map;

import ecologylab.serialization.types.element.IMappable;

/**
 * A HashMap with an ArrayList backing store, for speedy linear and hashed access.
 * Each Value also implements Mappable, and so functions as a key.
 * This means that we can implement Collection, because it guides us, for example,
 * in performing a hashed put() when a linear add() is called.
 * 
 * @author andruid
 * @param <K>
 */
public class HashMappableArrayList<K, V extends IMappable<K>> extends HashMapArrayList<K, V>
//implements Collection
{

	/**
	 * 
	 */
	public HashMappableArrayList()
	{}

	/**
	 * @param arg0
	 */
	public HashMappableArrayList(int arg0)
	{
		super(arg0);

	}

	/**
	 * @param arg0
	 */
	public HashMappableArrayList(Map arg0)
	{
		super(arg0);

	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HashMappableArrayList(int arg0, float arg1)
	{
		super(arg0, arg1);

	}

	/**
	 * Add element to HashMap and to ArrayList, unless same element was already in HashMap.
	 * 
	 * @param v
	 * @return
	 */
	public boolean add(V v)
	{
		V fromMap	= put(v.key(), v);
		boolean mapChanged = fromMap != v;
		if (mapChanged)
		{	// map changed - new element added
			if (fromMap != null)
			{	// old element must be removed
				arrayList.remove(fromMap);
			}
			arrayList.add(v);
		}
		return mapChanged;
	}

	public boolean addAll(Collection collection)
	{
		boolean result	= false;
		for (Object o : collection)
		{
			if (o != null)
			{
				V value	= (V) o;
				result	|= this.add(value);
			}
		}
		return result;
	}

	public boolean contains(V v)
	{
		return super.containsKey(v.key());
	}

	public boolean containsAll(Collection c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Collection c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public Object[] toArray()
	{
		return this.arrayList.toArray();
	}

	public Object[] toArray(Object[] a)
	{
		return this.arrayList.toArray(a);
	}

}
