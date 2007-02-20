package ecologylab.xml.types.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ecologylab.xml.ElementState;

/**
 * An ElementState XML tree node that supports an ArrayList of children (as well as whatever else
 * you add to it).
 * 
 * @author andruid
 */
public class HashMapState<K, V extends ElementState & Mappable<K>> extends ElementState implements Cloneable, Map<K, V> 
{
    /**
     * Stores the actual mappings.
     */
    @xml_map private HashMap<K, V> map = null;

    public HashMapState()
    {
        super();
    }
    
    /**
     * Use lazy evaluation for creating the map, in order to make it possible this
     * class lightweight enough to use in subclass situations where they may be no elements
     * added to the set, where the ElementState is only being used for direct fields.
     * @return
     */
    protected HashMap<K, V> map()
    {
    	HashMap<K, V>	result	= map;
        
    	if (result == null)
    	{
    		result			= new HashMap<K, V>();
    		map			 	= result;
    	}
        
    	return result;
    }

    @Override
    protected Map<K, V> getMap(Class thatClass)
    {
        return map();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        // TODO Auto-generated method stub
        return super.clone();
    }

    public void clear()
    {
        if (map != null)
            map.clear();
    }

    public boolean containsKey(Object key)
    {
        return (map == null ? false : map.containsKey(key));
    }

    public boolean containsValue(Object value)
    {
        return (map == null ? false : map.containsValue(value));
    }

    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        return map().entrySet();
    }

    public V get(Object key)
    {
        return map().get(key);
    }

    public boolean isEmpty()
    {
        return (map == null ? true : map.isEmpty());
    }

    public Set<K> keySet()
    {
        return (map == null ? new HashSet<K>() : map.keySet());
    }

    public V put(K key, V value)
    {
        return map().put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> t)
    {
        map().putAll(t);
    }

    public V remove(Object key)
    {
        return (map == null ? null : map.remove(key));
    }

    public int size()
    {
        return (map == null ? 0 : map.size());
    }

    public Collection<V> values()
    {
        return (map == null ? new ArrayList<V>() : map.values());
    }
}
