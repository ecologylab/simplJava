/**
 * 
 */
package ecologylab.collections;

import java.util.Map;

import ecologylab.generic.HashMapWriteSynchBase;


/**
 * Hashed data structure with synchronized writes/deletes and unsynchronized reads.
 * Value factory takes 3 args. First one is the key.
 * 
 *
 * @author andruid @param <K>		Type of the Key.
 * @author andruid @param <K2>	Type the value factory returns. Usually the same as type of Key, it can also extend it.
 * 															Also the type of an argument to the value factory.
 * @author andruid @param <V>		Type of the value.
 * @author andruid @param <A1>	Type of an argument to the value factory.
 * @author andruid @param <A2>	Type of an argument to the value factory.
 */
public class HashMapWriteSynch3Args<K, K2 extends K, V, V2 extends V, A1, A2> extends HashMapWriteSynchBase<K, V>
{
	ValueFactory3<K2, V2, A1, A2> valueFactory;
	
/**
 * 
 * @param size
 * @param load
 */
	public HashMapWriteSynch3Args(int size, float load)
	{
		super(size, load);
	}

	public HashMapWriteSynch3Args(int size, float load, ValueFactory3<K2, V2, A1, A2> valueFactory)
	{
		this(size, load);
		this.valueFactory			= valueFactory;
	}
	/**
	 * @param arg0
	 */
	public HashMapWriteSynch3Args(int arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	public HashMapWriteSynch3Args()
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public HashMapWriteSynch3Args(Map arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * If there is already an entry, return it.
	 * Otherwise, create an entry with the valueFactory slot of this.
	 * 
	 * @param key
	 * @param arg1
	 * @param arg2
	 * 
	 * @return				The key matching value, found or constructed.
	 */
	public V getOrCreateAndPutIfNew(K2 key, A1 arg1, A2 arg2)
	{
		return getOrCreateAndPutIfNew(key, arg1, arg2, valueFactory);
	}

	/**
	 * If there is already an entry, return it.
	 * Otherwise, create an entry with the factory.
	 * 
	 * @param key
	 * @param arg1
	 * @param arg2
	 * @param factory
	 * 
	 * @return				The key matching value, found or constructed.
	 */
	public V getOrCreateAndPutIfNew(K2 key, A1 arg1, A2 arg2, ValueFactory3<K2, V2, A1, A2> factory)
	{
		V result	= get(key);
		if (result == null)
		{
			synchronized (this)
			{
				result		= get(key);
				if (result == null)
				{
					result = factory.createValue(key, arg1, arg2);
					super.put(key, result);
				}
			}
		}
		return result;
	}

}
