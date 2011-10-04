/**
 * 
 */
package ecologylab.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author andruid
 *
 */
public class ConcurrentHashMapArrayList<K, V> extends ConcurrentHashMap<K, V> implements Iterable<V>
{
	protected	final	ArrayList<V>	arrayList;
	/**
	 * 
	 */
	public ConcurrentHashMapArrayList()
	{
		arrayList	= new ArrayList<V>();
	}

	/**
	 * @param initialCapacity
	 */
	public ConcurrentHashMapArrayList(int initialCapacity)
	{
		super(initialCapacity);
		arrayList	= new ArrayList<V>(initialCapacity);
	}

	/**
	 * @param m
	 */
	public ConcurrentHashMapArrayList(Map<? extends K, ? extends V> m)
	{
		super(m);
		arrayList	= new ArrayList<V>(m.size());
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 */
	public ConcurrentHashMapArrayList(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
		arrayList	= new ArrayList<V>(initialCapacity);
	}

	/**
	 * @param initialCapacity
	 * @param loadFactor
	 * @param concurrencyLevel
	 */
	public ConcurrentHashMapArrayList(int initialCapacity, float loadFactor, int concurrencyLevel)
	{
		super(initialCapacity, loadFactor, concurrencyLevel);
		arrayList	= new ArrayList<V>(initialCapacity);
	}

	@Override public V put(K key, V value)
	{
		V result	= super.put(key, value);
		if (result != null)	// the object that was overridden
			arrayList.remove(result);
		arrayList.add(value);
		
		return result;
	}
	
	@Override public void putAll(Map<? extends K, ? extends V> map)
	{
		for (K key : map.keySet())
			this.put(key, map.get(key));
	}
	
	public V get(int index)
	{
		return arrayList.get(index);
	}
	
	public Iterator<V> iterator()
	{
		return arrayList.iterator();
	}
	
	@Override public V remove(Object key)
	{
		V result	= super.remove(key);
		if (result != null)
			arrayList.remove(result);
		return result;
	}
	
	@Override public void clear()
	{
		synchronized (this)
		{
			super.clear();
			arrayList.clear();
		}
	}
	
	public void recycle()
	{
		clear();
	}
	
	@Override public Collection<V> values()
	{
		return arrayList;
	}

	@Override
	public V putIfAbsent(K key, V value)
	{
		V newValue	= super.putIfAbsent(key, value);
		if (newValue == null)
			arrayList.add(value);
		return newValue;
	}
}
