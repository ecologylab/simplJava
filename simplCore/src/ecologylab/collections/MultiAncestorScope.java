package ecologylab.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ecologylab.generic.StringBuilderBaseUtils;

/**
 * A scope (map: String -&gt; T) with multiple ancestors.
 * 
 * @author quyin
 * 
 * @param <T>
 *          The value type.
 */
@SuppressWarnings("unchecked")
public class MultiAncestorScope<T> implements Map<String, T>
{

  public static final int      DEFAULT_CAPACITY    = 16;

  public static final float    DEFAULT_LOAD_FACTOR = 0.75f;

  private HashMap<String, T>   local;

  private List<Map<String, T>> ancestors;

  public MultiAncestorScope()
  {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, new Map[] {});
  }

  public MultiAncestorScope(int initialCapacity)
  {
    this(initialCapacity, DEFAULT_LOAD_FACTOR, new Map[] {});
  }

  public MultiAncestorScope(int initialCapacity, float loadFactor)
  {
    this(initialCapacity, loadFactor, new Map[] {});
  }

  public MultiAncestorScope(Map<String, T>... ancestors)
  {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, ancestors);
  }

  public MultiAncestorScope(int initialCapacity, Map<String, T>... ancestors)
  {
    this(initialCapacity, DEFAULT_LOAD_FACTOR, ancestors);
  }

  public MultiAncestorScope(int initialCapacity, float loadFactor, Map<String, T>... ancestors)
  {
    super();
    local = new HashMap<String, T>(initialCapacity, loadFactor);
    addAncestors(ancestors);
  }

  public List<Map<String, T>> getAncestors()
  {
    return this.ancestors;
  }

  protected List<Map<String, T>> ancestors()
  {
    if (ancestors == null)
    {
      ancestors = new ArrayList<Map<String, T>>();
    }
    return ancestors;
  }

  public void addAncestor(Map<String, T> ancestor)
  {
    if (ancestor != null && ancestor != this)
    {
      this.ancestors().add(ancestor);
    }
  }

  public void addAncestors(Map<String, T>... ancestors)
  {
    if (ancestors != null)
    {
      for (Map<String, T> ancestor : ancestors)
      {
        this.addAncestor(ancestor);
      }
    }
  }

  public void removeAncestor(Map<String, T> ancestor)
  {
    if (ancestors != null)
    {
      ancestors.remove(ancestor);
    }
  }

  @Override
  public int size()
  {
    return local.size();
  }

  @Override
  public boolean isEmpty()
  {
    return local.isEmpty();
  }

  @Override
  public Set<Map.Entry<String, T>> entrySet()
  {
    return local.entrySet();
  }

  @Override
  public Set<String> keySet()
  {
    return local.keySet();
  }

  @Override
  public Collection<T> values()
  {
    return local.values();
  }

  /**
   * This will check BOTH the local scope AND ancestors.
   * 
   * Ancestors will be looked up in the order of being added.
   */
  @Override
  public boolean containsKey(Object key)
  {
    if (local.containsKey(key))
    {
      return true;
    }
    if (ancestors != null)
    {
      for (Map<String, T> ancestor : ancestors)
      {
        if (ancestor.containsKey(key))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This will check BOTH the local scope AND ancestors.
   * 
   * Ancestors will be looked up in the order of being added.
   */
  @Override
  public boolean containsValue(Object value)
  {
    if (local.containsValue(value))
    {
      return true;
    }
    if (ancestors != null)
    {
      for (Map<String, T> ancestor : ancestors)
      {
        if (ancestor.containsValue(value))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This will check BOTH the local scope AND ancestors.
   * 
   * Ancestors will be looked up in the order of being added.
   */
  @Override
  public T get(Object key)
  {
    if (containsKeyLocally(key))
    {
      return local.get(key);
    }
    if (ancestors != null)
    {
      for (Map<String, T> ancestor : ancestors)
      {
        if (ancestor.containsKey(key))
        {
          return ancestor.get(key);
        }
      }
    }
    return null;
  }

  /**
   * Get all values for the key from BOTH this scope AND ancestors.
   * 
   * Ancestors will be looked up in the order of being added.
   * 
   * @param key
   * @return
   */
  public List<T> getAll(Object key)
  {
    List<T> result = new ArrayList<T>();
    if (local.containsKey(key))
    {
      result.add(local.get(key));
    }
    if (ancestors != null)
    {
      for (Map<String, T> ancestor : ancestors)
      {
        if (ancestor.containsKey(key))
        {
          result.add(ancestor.get(key));
        }
      }
    }
    return result;
  }

  @Override
  public T put(String key, T value)
  {
    return local.put(key, value);
  }

  /**
   * Only put value into the scope when it is not null.
   * 
   * @param key
   * @param value
   */
  public void putIfValueNotNull(String key, T value)
  {
    if (value != null)
    {
      put(key, value);
    }
  }

  @Override
  public void putAll(Map<? extends String, ? extends T> m)
  {
    local.putAll(m);
  }

  @Override
  public T remove(Object key)
  {
    return local.remove(key);
  }

  @Override
  public void clear()
  {
    local.clear();
  }

  public boolean containsKeyLocally(Object key)
  {
    return local.containsKey(key);
  }

  public boolean containsValueLocally(T value)
  {
    return local.containsValue(value);
  }

  /**
   * Get the value of the given key only from this scope.
   * 
   * @param key
   * @return
   */
  public T getLocally(Object key)
  {
    return local.get(key);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = StringBuilderBaseUtils.acquire();
    sb.append(this.getClass().getSimpleName()).append("[size: ").append(this.size()).append("]: ")
        .append(super.toString());

    if (this.ancestors != null && this.ancestors.size() > 0)
    {
      for (Map<String, T> ancestor : this.ancestors)
      {
        if (ancestor != null)
        {
          String ancestorStr = ancestor.toString();
          sb.append("\n\t -> ");
          sb.append(ancestorStr.replace("\n", "\n\t"));
        }
      }
    }

    String result = sb.toString();
    StringBuilderBaseUtils.release(sb);
    return result;
  }

  public void reset()
  {
    this.ancestors = null;
    local.clear();
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // inheritance relation:
    // s1(1, 2) -> s2(3) -> s4(5)
    // \-> s3() /
    MultiAncestorScope<Integer> s1 = new MultiAncestorScope<Integer>();
    s1.put("one", 1);
    s1.put("two", 2);

    MultiAncestorScope<Integer> s2 = new MultiAncestorScope<Integer>(0, s1);
    s2.put("three", 3);

    MultiAncestorScope<Integer> s3 = new MultiAncestorScope<Integer>(0, s1);
    // s3.put("four", 4);

    MultiAncestorScope<Integer> s4 = new MultiAncestorScope<Integer>(0, s2, s3);
    s4.put("five", 5);

    System.out.println(s4);
    System.out.println(s4.get("five"));
    System.out.println(s4.get("two"));
  }

}
