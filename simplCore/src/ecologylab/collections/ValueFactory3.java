package ecologylab.collections;


/**
 * Factory for Value objects for hashed data structure with synchronized writes and unsynchronized reads.

 *
 * @author andruid @param <K>
 * @author andruid @param <V>
 * @author andruid @param <A1>
 * @author andruid @param <A2>
 */
public interface ValueFactory3<K, V, A1, A2>
{
	/**
	 * Wrapper for the constructor of type V.
	 * 
	 * @param key
	 * @param arg1
	 * @param arg2
	 * @return
	 */
	public V createValue(K key, A1 arg1, A2 arg2);
}
