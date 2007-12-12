/**
 * 
 */
package ecologylab.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * @author robinson
 *
 */
public abstract class HashMapWriteSynchBase<K, V> extends HashMap<K, V>
{

	public HashMapWriteSynchBase(int arg0, float arg1)
	{
		super(arg0, arg1);
	}

	public HashMapWriteSynchBase(int arg0)
	{
		super(arg0);
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
