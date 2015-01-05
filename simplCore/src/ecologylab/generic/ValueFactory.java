package ecologylab.generic;

/**
 * Create Value objects for hashed data structure with synchronized writes and unsynchronized reads.
 * @author andruid
 *
 * @param <K>
 * @param <V>
 */
public interface ValueFactory<K, V>
{
	public V constructValue(K key);
}
