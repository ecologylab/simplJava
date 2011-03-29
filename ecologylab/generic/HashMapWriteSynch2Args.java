/**
 * 
 */
package ecologylab.generic;

import java.util.Map;

/**
 * Hashed data structure with synchronized writes/deletes and unsynchronized reads.
 * Constructor takes 2 args. First one is the key.
 * 
 * @author andruid, robinson
 *
 * @param <K>
 * @param <V>
 * @param <A>	2nd argument to value constructor
 */
public class HashMapWriteSynch2Args<K, V, A> extends HashMapWriteSynchBase<K, V>
{
	ValueFactory2<K, V, A> factory;
	
	/**
	 * @param arg0
	 * @param arg1
	 */
	public HashMapWriteSynch2Args(int arg0, float arg1)
	{
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public HashMapWriteSynch2Args(int arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public HashMapWriteSynch2Args()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public HashMapWriteSynch2Args(Map arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * If there is already an entry, return it.
	 * 
	 * Otherwise, create an entry with the factory.
	 * 
	 * @return	The entry matching key, found or constructed.
	 */
	public V getOrCreateAndPutIfNew(K key, A arg)
	{
		V result	= get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result		= get(key);
				if (result == null)
				{
					result = this.factory.createValue(key, arg);
					super.put(key, result);
				}
			}
		}
		return result;
	}
	
	public ValueFactory2<K, V, A> getFactory()
	{
		return factory;
	}

	/**
	 * @param factory the factory to set
	 */
	public void setFactory(ValueFactory2<K, V, A> factory)
	{
		this.factory = factory;
	}

}
