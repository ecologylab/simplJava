/**
 * 
 */
package ecologylab.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * A HashMap that is synchronized for writes, but not for reads.
 * 
 * @param <K>			Key type of the HashMap.
 * @param <V>			Value type of the HashMap.
 *
 * @author andruid
 */
public abstract class HashMapWriteSynchBase<K, V> extends HashMap<K, V>
{

	public HashMapWriteSynchBase(int size, float load)
	{
		super(size, load);
	}

	public HashMapWriteSynchBase(int size)
	{
		super(size);
	}

	public HashMapWriteSynchBase()
	{
		super();
	}

	public HashMapWriteSynchBase(Map<? extends K, ? extends V> arg0)
	{
		super(arg0);
	}

	/**
	 * If there is already an entry, return it.
	 * 
	 * Otherwise, add the entry, and return null.
	 * <p/>
	 * NB: NEVER replaces an existing entry.
	 */
	public V getOrPutIfNew(K key, V value)
	{
		V result = get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result = put(key, value);
			}
		}
		return result;
	}

	/**
	 * Sycnhronizes remove.
	 */
	@Override
	public synchronized V remove(Object key)
	{
		return super.remove(key);
	}

	/**
	 * Sycnhronizes if you add another map to this one.
	 */
	@Override
	public synchronized void putAll(Map<? extends K, ? extends V> m)
	{
		super.putAll(m);
	}

	@Override
	public V put(K key, V value)
	{
		synchronized (this)
		{
			return super.put(key, value);
		}
	}

}
