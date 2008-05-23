package ecologylab.generic;

/**
 * Create Value objects for hashed data structure with synchronized writes and unsynchronized reads.
 * @author andruid
 *
 * @param <K>
 * @param <V>
 * @param <A> 2nd argument to constructor
 */
public interface ValueFactory2<K, V, A>
{
	public V createValue(K key, A arg);
}
