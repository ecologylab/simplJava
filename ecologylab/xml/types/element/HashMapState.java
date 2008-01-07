package ecologylab.xml.types.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ecologylab.xml.ElementState;

/**
 * An ElementState XML tree node that supports a HashMap whose values are Mappable (capable of supplying their own key
 * into the map).
 * 
 * @author andruid
 */
public class HashMapState<K, V extends ElementState & Mappable<K>> extends ElementState implements Cloneable, Map<K, V>
{
    /**
     * Stores the actual mappings.
     */
    @xml_map protected HashMap<K, V> map = null;

    public HashMapState()
    {
        super();
    }

    /**
     * Use lazy evaluation for creating the map, in order to make it possible this class lightweight enough to use in
     * subclass situations where they may be no elements added to the set, where the ElementState is only being used for
     * direct fields.
     * 
     * @return
     */
    protected HashMap<K, V> map()
    {
        HashMap<K, V> result = map;

        if (result == null)
        {
            result = new HashMap<K, V>();
            map = result;
        }

        return result;
    }

    @Override protected Map getMap(
            Class thatClass)
    {
        return map();
    }

    @Override protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    /**
     * Convienence method for adding Mappable elements. This method simply calls put(value.key(), value).
     * 
     * @param value
     */
    public V add(V value)
    {
        return this.put(value.key(), value);
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

    /**
     * Clear data structures and references to enable garbage collecting of resources associated with this.
     * 
     * Calling recycle() on an HashMapState has the side effect of recycling every value contained in the HashMapState.
     * If the values of the HashMapState should be retained, then call clear(), then recycle().
     */
    @Override public void recycle()
    {
        if (this.map != null)
        {
            for (K key : map.keySet())
            {
                V value = map.remove(key);

                if (value != null)
                {
                    value.recycle();
                }
            }

            this.clear();
        }

        super.recycle();
    }
}
