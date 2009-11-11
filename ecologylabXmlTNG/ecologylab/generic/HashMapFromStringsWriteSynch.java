/**
 * 
 */
package ecologylab.generic;

import java.util.Map;

/**
 * @author andruid
 *
 */
abstract public class HashMapFromStringsWriteSynch<K, V> extends HashMapWriteSynch<String, V>
implements ValueFactory<String, V>
{

	/**
	 * @param arg0
	 * @param arg1
	 */
	public HashMapFromStringsWriteSynch(int arg0, float arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public HashMapFromStringsWriteSynch(int arg0)
	{
		super(arg0);
	}
	
	protected abstract String getKey(K kKey);
	
	/**
	 * ValueFactory K,V <-> String, V
	 * @param key
	 * @param factory
	 * @return
	 */
	public V lookup(K key)
	{
		// TODO Auto-generated method stub
		return super.getOrCreateAndPutIfNew(getKey(key), this);
	}

	/**
	 * ValueFactory K,V <-> String, V
	 * @param key
	 * @param factory
	 * @return
	 */
	public V getOrCreateAndPutIfNew(K key)
	{
		// TODO Auto-generated method stub
		return super.getOrCreateAndPutIfNew(getKey(key), this);
	}

	/**
	 * 
	 */
	public HashMapFromStringsWriteSynch()
	{
	}

	/**
	 * @param arg0
	 */
	public HashMapFromStringsWriteSynch(Map arg0)
	{
		super(arg0);
	}
	
	

}
