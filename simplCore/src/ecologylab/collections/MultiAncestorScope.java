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
 * To reduce creating unnecessary objects, this class will not create a new HashMap until necessary
 * (i.e. when you put something into the local scope).
 * 
 * This class ignores null keys.
 * 
 * @author quyin
 * 
 * @param <T>
 *          The value type.
 */
@SuppressWarnings("unchecked")
public class MultiAncestorScope<T> implements Map<String, T>
{

  private static final HashMap<String, Object>   EMPTY_HASH_MAP;

  public static final MultiAncestorScope<Object> EMPTY_SCOPE;

  static
  {
    EMPTY_HASH_MAP = new HashMap<String, Object>();
    EMPTY_SCOPE = new MultiAncestorScope<Object>("EMPTY")
    {
      @Override
      public void addAncestor(Map<String, Object> ancestor)
      {
        // no op
      }

      @Override
      public Object put(String key, Object value)
      {
        // no op
        return value;
      }
    };
  }

  private String                                 id;

  private HashMap<String, T>                     local;

  private List<Map<String, T>>                   ancestors;

  public MultiAncestorScope()
  {
    this(null, new Map[] {});
  }

  public MultiAncestorScope(String id)
  {
    this(id, new Map[] {});
  }

  public MultiAncestorScope(Map<String, T>... ancestors)
  {
    this(null, ancestors);
  }

  public MultiAncestorScope(String id, Map<String, T>... ancestors)
  {
    super();
    this.id = id;
    addAncestors(ancestors);
  }

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  protected List<Map<String, T>> ancestors()
  {
    if (ancestors == null)
    {
      ancestors = new ArrayList<Map<String, T>>();
    }
    return ancestors;
  }

  /**
   * @return All ancestors using DFS. This is used to prevent infinite loops with ancestors.
   */
  protected List<Map<String, T>> allAncestors()
  {
    List<Map<String, T>> result = new ArrayList<Map<String, T>>();
    allAncestorsHelper(result, this);
    return result;
  }

  private void allAncestorsHelper(List<Map<String, T>> result, MultiAncestorScope<T> scope)
  {
    if (scope.ancestors != null)
    {
      for (Map<String, T> ancestor : scope.ancestors)
      {
        if (!result.contains(ancestor))
        {
          result.add(ancestor);
          if (ancestor instanceof MultiAncestorScope)
          {
            allAncestorsHelper(result, (MultiAncestorScope<T>) ancestor);
          }
        }
      }
    }
  }

  /**
   * @param map
   * @return If map is an (not necessarily immediate) ancestor of this scope.
   */
  public boolean isAncestor(Map<String, T> map)
  {
    if (ancestors != null)
    {
      List<Map<String, T>> allAncestors = allAncestors();
      return allAncestors.contains(map);
    }
    return false;
  }

  public void addAncestor(Map<String, T> ancestor)
  {
    if (ancestor != null && ancestor != this && !isAncestor(ancestor))
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

  public void removeImmediateAncestor(Map<String, T> ancestor)
  {
    if (ancestors != null)
    {
      ancestors.remove(ancestor);
    }
  }

  @Override
  public int size()
  {
    return local == null ? 0 : local.size();
  }

  @Override
  public boolean isEmpty()
  {
    return local == null ? true : local.isEmpty();
  }

  @Override
  public Set<Entry<String, T>> entrySet()
  {
    Set<? extends Entry<String, ? extends Object>> result =
        local == null ? EMPTY_HASH_MAP.entrySet() : local.entrySet();
    return (Set<Entry<String, T>>) result;
  }

  @Override
  public Set<String> keySet()
  {
    return local == null ? EMPTY_HASH_MAP.keySet() : local.keySet();
  }

  @Override
  public Collection<T> values()
  {
    Collection<? extends Object> result =
        local == null ? EMPTY_HASH_MAP.values() : local.values();
    return (Collection<T>) result;
  }

  /**
   * This will check BOTH the local scope AND ancestors.
   * 
   * Ancestors will be looked up in the order of being added.
   */
  @Override
  public boolean containsKey(Object key)
  {
    if (key != null)
    {
      if (local != null && local.containsKey(key))
      {
        return true;
      }
      if (ancestors != null)
      {
        List<Map<String, T>> allAncestors = allAncestors();
        for (Map<String, T> ancestor : allAncestors)
        {
          if (ancestor instanceof MultiAncestorScope)
          {
            if (((MultiAncestorScope<T>) ancestor).containsKeyLocally(key))
            {
              return true;
            }
          }
          else
          {
            if (ancestor.containsKey(key))
            {
              return true;
            }
          }
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
    if (local != null && local.containsValue(value))
    {
      return true;
    }
    if (ancestors != null)
    {
      List<Map<String, T>> allAncestors = allAncestors();
      for (Map<String, T> ancestor : allAncestors)
      {
        if (ancestor instanceof MultiAncestorScope)
        {
          if (((MultiAncestorScope<T>) ancestor).containsValueLocally((T) value))
          {
            return true;
          }
        }
        else
        {
          if (ancestor.containsValue(value))
          {
            return true;
          }
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
    if (key != null)
    {
      if (containsKeyLocally(key))
      {
        return local.get(key);
      }
      if (ancestors != null)
      {
        List<Map<String, T>> allAncestors = allAncestors();
        for (Map<String, T> ancestor : allAncestors)
        {
          if (ancestor instanceof MultiAncestorScope)
          {
            if (((MultiAncestorScope<T>) ancestor).containsKeyLocally(key))
            {
              return ((MultiAncestorScope<T>) ancestor).getLocally(key);
            }
          }
          else
          {
            if (ancestor.containsKey(key))
            {
              return ancestor.get(key);
            }
          }
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
    if (key != null)
    {
      if (local != null && local.containsKey(key))
      {
        result.add(local.get(key));
      }
      if (ancestors != null)
      {
        List<Map<String, T>> allAncestors = allAncestors();
        for (Map<String, T> ancestor : allAncestors)
        {
          if (ancestor instanceof MultiAncestorScope)
          {
            if (((MultiAncestorScope<T>) ancestor).containsKeyLocally(key))
            {
              T value = ((MultiAncestorScope<T>) ancestor).getLocally(key);
              result.add(value);
            }
          }
          else
          {
            if (ancestor.containsKey(key))
            {
              T value = ancestor.get(key);
              result.add(value);
            }
          }
        }
      }
    }
    return result;
  }

  @Override
  public T put(String key, T value)
  {
    if (key != null)
    {
      if (local == null)
      {
        local = new HashMap<String, T>();
      }
      return local.put(key, value);
    }
    return null;
  }

  /**
   * Only put value into the scope when it is not null.
   * 
   * @param key
   * @param value
   */
  public T putIfValueNotNull(String key, T value)
  {
    if (key != null && value != null)
    {
      return put(key, value);
    }
    return null;
  }

  @Override
  public void putAll(Map<? extends String, ? extends T> m)
  {
    if (local == null)
    {
      local = new HashMap<String, T>();
    }
    local.putAll(m);
  }

  @Override
  public T remove(Object key)
  {
    return local == null ? null : key == null ? null : local.remove(key);
  }

  @Override
  public void clear()
  {
    if (local != null)
    {
      local.clear();
    }
  }

  public boolean containsKeyLocally(Object key)
  {
    return local == null ? false : key == null ? null : local.containsKey(key);
  }

  public boolean containsValueLocally(T value)
  {
    return local == null ? false : local.containsValue(value);
  }

  /**
   * Get the value of the given key only from this scope.
   * 
   * @param key
   * @return
   */
  public T getLocally(Object key)
  {
    return local == null ? null : key == null ? null : local.get(key);
  }

  @Override
  public String toString()
  {
    StringBuilder sb = StringBuilderBaseUtils.acquire();
    sb.append(getClass().getSimpleName())
        .append("[").append(id == null ? "noid" : id)
        .append(",size=").append(size()).append("]: ")
        .append(local == null ? "{}" : local);
    if (ancestors != null && ancestors.size() > 0)
    {
      for (Map<String, T> ancestor : ancestors)
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
    id = null;
    local = null;
    ancestors = null;
  }

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    // inheritance relation:
    // s4 -> s2 -----------> s1
    // \---> s3(nolocal) -/
    MultiAncestorScope<Integer> s1 = new MultiAncestorScope<Integer>("s1");
    s1.put("one", 1);
    s1.put("two", 2);

    MultiAncestorScope<Integer> s2 = new MultiAncestorScope<Integer>("s2", s1);
    s2.put("three", 3);

    MultiAncestorScope<Integer> s3 = new MultiAncestorScope<Integer>("s3", s1);

    MultiAncestorScope<Integer> s4 = new MultiAncestorScope<Integer>("s4", s2, s3);
    s4.put("five", 5);

    System.out.println(s4);
    System.out.println(s4.get("five"));
    System.out.println(s4.get("two"));
  }

}
