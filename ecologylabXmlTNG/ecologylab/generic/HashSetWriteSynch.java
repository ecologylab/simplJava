package ecologylab.generic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * Hashed data structure with synchronized writes/deletes and unsynchronized reads.
 * 
 * @author andruid, robinson
 *
 * @param <K>
 * @param <V>
 */
public class HashSetWriteSynch<K> extends HashSet<K>
{

	public HashSetWriteSynch(int arg0, float arg1)
	{
		super(arg0, arg1);
	}

	public HashSetWriteSynch(int arg0)
	{
		super(arg0);
	}

	public HashSetWriteSynch()
	{
		super();
	}

	
	public HashSetWriteSynch(Collection<? extends K> collection)
    {
        super(collection);
    }


	/**
     * Sycnhronizes add.
     */
    @Override 
    public synchronized boolean add(K key)
    {
        return super.add(key);
    }
	
	/**
	 * Sycnhronizes remove.
	 */
	@Override 
	public synchronized boolean remove(Object key)
	{
		return super.remove(key);
	}

	/**
     * Sycnhronizes if you add another set to this one.
     */
    @Override
    public synchronized boolean addAll(Collection<? extends K> c)
    {
        return super.addAll(c);
    }
}
