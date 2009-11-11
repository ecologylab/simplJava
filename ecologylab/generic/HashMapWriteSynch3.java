package ecologylab.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * Hashed data structure with synchronized writes/deletes and unsynchronized reads.
 * Uses three objects, instead of two. The values and keys are both formed from a third intermediate object.
 * This enables, for example, using a String as a key, but getting that String from an object of another class,
 * which is also the value.
 * 
 * @author andruid, robinson
 *
 * @param <K>	Class of the resulting key.
 * @param <I>	Class of the intermediate object, which is used to form keys and values.
 * @param <V>	Class of the value.
 */
abstract public class HashMapWriteSynch3<K, I, V> extends HashMap<K, V>
implements ValueFactory<I, V>
{

	public HashMapWriteSynch3(int arg0, float arg1)
	{
		super(arg0, arg1);
	}

	public HashMapWriteSynch3(int arg0)
	{
		super(arg0);
	}

	public HashMapWriteSynch3()
	{
		super();
	}

	public HashMapWriteSynch3(Map<? extends K, ? extends V> arg0)
	{
		super(arg0);
	}
	
	abstract protected K createKey(I intermediate);
	
	abstract public V createValue(I intermediate);


	public V getOrCreateAndPutIfNew(I intermediate)
	{
		return getOrCreateAndPutIfNew(intermediate, this);
	}
	/**
	 * If there is already an entry, return it.
	 * 
	 * Otherwise, create an entry with the factory.
	 * 
	 * @return	The entry matching key, found or constructed.
	 */
	public V getOrCreateAndPutIfNew(I intermediate, ValueFactory<I, V> factory)
	{
		K key		= createKey(intermediate);
		V result	= get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result		= get(key);
				if (result == null)
				{
					result = factory.createValue(intermediate);
					super.put(key, result);
				}
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
}
