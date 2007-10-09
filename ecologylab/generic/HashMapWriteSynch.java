package ecologylab.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * Hashed data structure with synchronized writes and unsynchronized reads.
 * 
 * @author andruid
 *
 * @param <K>
 * @param <V>
 */
public class HashMapWriteSynch<K, V> extends HashMap<K, V>
{

	public HashMapWriteSynch(int arg0, float arg1)
	{
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public HashMapWriteSynch(int arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public HashMapWriteSynch()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public HashMapWriteSynch(Map arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
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
		V result	= get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result		= put(key, value);
			}
		}
		return result;
	}

	/**
	 * If there is already an entry, return it.
	 * 
	 * Otherwise, add the entry, and return null.
	 */
	public V getOrCreateAndPutIfNew(K key, ValueFactory<K, V> factory)
	{
		V result	= get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result		= get(key);
				if (result == null)
					result	= super.put(key, factory.createValue(key));
			}
		}
		return result;
	}
	
	/**
	 * Sycnhronizes remove.
	 */
	@Override public synchronized V remove(Object key)
	{
		return remove(key);
	}
}
