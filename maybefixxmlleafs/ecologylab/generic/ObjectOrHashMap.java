/**
 * 
 */
package ecologylab.generic;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * An attempt to make an object for storing other objects. If only one object
 * exists in the collection, then it is stored here, otherwise it's stored in a
 * hashmap where it can be randomly accessed. This is meant primarily for
 * applications where you're likely to have a LOT of single entires, but may
 * occasionally have a very large number (for example, in a hashmap that maps IP
 * address to connection).
 * 
 * ObjectOrHashMap stores a single key value pair, until more than one such pair
 * has been specified, then it switches to using a HashMap instead of the pair.
 * To avoid constructing too many HashMaps, it never destroys the internal
 * HashMap once it has been created. It is assumed that ObjectOrHashMap will
 * generally be used in another Collection and when it is removed from that
 * Collection, it will be destroyed.
 * 
 * @author Zach
 * 
 */
public class ObjectOrHashMap<K, V> implements Map<K, V>
{
    private class OOHMEntry implements Map.Entry<K, V>
    {
        private K         key;

        private V         value;

        private Map<K, V> parent;

        public OOHMEntry(K key, V value, Map<K, V> parent)
        {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        /**
         * @see java.util.Map.Entry#getKey()
         */
        public K getKey()
        {
            return key;
        }

        /**
         * @see java.util.Map.Entry#getValue()
         */
        public V getValue()
        {
            return value;
        }

        /**
         * @see java.util.Map.Entry#setValue(java.lang.Object)
         */
        public V setValue(V value)
        {
            this.value = value;

            return this.parent.put(key, value);
        }
    }

    private K     singleKey;

    private V     singleValue;

    HashMap<K, V> mappings;
    
    public ObjectOrHashMap()
    {
        
    }
    
    public ObjectOrHashMap(K key, V value)
    {
        this.singleKey = key;
        this.singleValue = value;
    }

    private HashMap<K, V> mappings()
    {
        HashMap<K, V> currentMap = mappings;

        if (currentMap == null)
        {
            currentMap = new HashMap<K, V>();
            mappings = currentMap;
        }

        return currentMap;
    }

    /**
     * @see java.util.Map#clear()
     */
    public void clear()
    {
        singleKey = null;
        singleValue = null;
        if (mappings != null)
            mappings.clear();
    }

    /**
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key)
    {
        return singleKey == null ? mappings().containsKey(key) : singleKey
                .equals(key);
    }

    /**
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value)
    {
        return singleValue == null ? mappings().containsValue(value)
                : singleValue.equals(value);
    }

    /**
     * @see java.util.Map#entrySet()
     */
    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        if (singleValue != null)
        { // just one; we need to make the set
            Set<Map.Entry<K, V>> set = new LinkedHashSet<Map.Entry<K, V>>(1);
            set.add(new OOHMEntry(singleKey, singleValue, this));

            return set;
        }
        else
        {
            return mappings().entrySet();
        }
    }

    /**
     * @see java.util.Map#get(java.lang.Object)
     */
    public V get(Object key)
    {
        return singleValue == null ? mappings().get(key) : singleValue;
    }

    /**
     * @see java.util.Map#isEmpty()
     */
    public boolean isEmpty()
    {
        // check single value, then mappings, then if mappings exist, we check
        // its emptiness
        return singleValue == null || mappings == null || mappings.isEmpty();
    }

    /**
     * @see java.util.Map#keySet()
     */
    public Set<K> keySet()
    {
        if (singleKey == null)
        {
            return mappings().keySet();
        }
        else
        {
            Set<K> set = new LinkedHashSet<K>(1);
            set.add(singleKey);
            return set;
        }
    }

    /**
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public V put(K key, V value)
    {
        if (singleKey != null)
        {
            mappings().put(singleKey, singleValue);

            this.singleKey = null;
            this.singleValue = null;
        }
        // we already know it's instantiated b/c we just did it
        return mappings.put(key, value);
    }

    /**
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends K, ? extends V> t)
    {
        if (singleKey != null)
        {
            mappings().put(singleKey, singleValue);
        }

        mappings.putAll(t);
    }

    /**
     * @see java.util.Map#remove(java.lang.Object)
     */
    public V remove(Object key)
    {
        if (singleKey != null && singleKey.equals(key))
        {
            singleKey = null;
            
            V retVal = singleValue;
            singleValue = null;
            
            return retVal;
        }
        else if (mappings != null)
        {
            return mappings.remove(key);
        }
        else
        {
            return null;
        }
    }

    /**
     * @see java.util.Map#size()
     */
    public int size()
    {
        return singleKey == null ? (mappings == null ? 0 : mappings.size()) : 1;
    }

    /**
     * @see java.util.Map#values()
     */
    public Collection<V> values()
    {
        System.out.println("getting values.");
        
        if (mappings != null)
        {
//            System.out.println("mappings are not null.");
            
            return mappings.values();
        }
        else
        {
  //          System.out.println("mappings are null.");
            
            Collection<V> coll = new LinkedHashSet<V>(1);
            
            if (singleValue != null)
                coll.add(singleValue);
            
            return coll;
        }
    }

}
