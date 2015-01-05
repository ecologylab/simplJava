package ecologylab.generic;

import java.util.Map;

/**
 * Hashed data structure with synchronized writes/deletes and unsynchronized reads.
 * 
 * @author andruid, robinson
 *
 * @param <K>
 * @param <V>
 */
public class HashMapWriteSynch<K, V> extends HashMapWriteSynchBase<K, V>
{
	final ValueFactory<K, V> factory;
	
	public HashMapWriteSynch(ValueFactory<K, V> factory, int arg0, float arg1)
	{
		super(arg0, arg1);
		this.factory	= factory;
	}

	public HashMapWriteSynch(ValueFactory<K, V> factory, int arg0)
	{
		super(arg0);
		this.factory	= factory;
	}

	public HashMapWriteSynch(ValueFactory<K, V> factory)
	{
		super();
		this.factory	= factory;
	}

	public HashMapWriteSynch(ValueFactory<K, V> factory, Map<? extends K, ? extends V> arg0)
	{
		super(arg0);
		this.factory	= factory;
	}

	/**
	 * If there is already an entry, return it.
	 * 
	 * Otherwise, create an entry with the factory.
	 * 
	 * @return	The entry matching key, found or constructed.
	 */
	public V getOrConstruct(K key)
	{
		V result	= get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result		= get(key);
				if (result == null)
				{
					result = factory.constructValue(key);
					super.put(key, result);
				}
			}
		}
		return result;
	}

	
	@Override
	public V put(K key, V value)
	{
		synchronized(this)
		{
			return super.put(key, value);
		}
	}

}
