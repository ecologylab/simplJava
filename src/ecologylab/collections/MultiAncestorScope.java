package ecologylab.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ecologylab.generic.StringBuilderBaseUtils;

/**
 * A scope (map: String -&gt; T) with multiple ancestors.
 * <p>
 * NOTE that currently this class uses a LRU cache to cache look-ups for values from ancestors for
 * efficiency, thus removing a value from an ancestor may not work properly. if you need removing
 * please set CACHE_SIZE to zero or modify the class.
 * 
 * @author quyin
 * 
 * @param <T>
 *          The value type.
 */
@SuppressWarnings({ "serial", "unchecked" })
public class MultiAncestorScope<T> extends HashMap<String, T>
{

	public static int									CACHE_SIZE	= 16;

	/**
	 * ancestors of this scope.
	 */
	private List<Map<String, T>>			ancestors;

	/**
	 * for caching results from ancestors.
	 */
	private LinkedHashMap<String, T>	queryCache;

	public MultiAncestorScope()
	{
		super();
	}

	public MultiAncestorScope(int initialCapacity)
	{
		super(initialCapacity);
	}

	public MultiAncestorScope(int initialCapacity, float loadFactor)
	{
		super(initialCapacity, loadFactor);
	}

	public MultiAncestorScope(Map<String, T>... ancestors)
	{
		super();
		this.addAncestors(ancestors);
	}

	public MultiAncestorScope(int initialCapacity, Map<String, T>... ancestors)
	{
		super(initialCapacity);
		this.addAncestors(ancestors);
	}

	public MultiAncestorScope(int initialCapacity, float loadFactor, Map<String, T>... ancestors)
	{
		super(initialCapacity, loadFactor);
		this.addAncestors(ancestors);
	}

	/**
	 * get the value of the given key from this scope. will look up this scope first. if not found,
	 * look up ancestors one by one, in the order of being added.
	 */
	@Override
	public T get(Object key)
	{
		HashSet<Map<String, T>> visited = new HashSet<Map<String, T>>();
		return getHelper(key, visited);
	}
	
	/**
	 * helper method that uses a visited hash set to reduce the time complexity of map look-up.
	 * 
	 * @param key
	 * @param visited
	 * @return
	 */
	private T getHelper(Object key, HashSet<Map<String, T>> visited)
	{
		T result = super.get(key);
		if (result == null)
			result = this.getFromCache(key);
		if (result == null)
		{
			if (this.ancestors != null)
				for (Map<String, T> ancestor : this.ancestors)
					if (containsSame(visited, ancestor))
						continue;
					else
					{
						visited.add(ancestor);
						if (ancestor instanceof MultiAncestorScope)
							result = ((MultiAncestorScope<T>) ancestor).getHelper(key, visited);
						else
							result = ancestor.get(key);
						if (result != null)
						{
							this.putToCache(key, result);
							break;
						}
					}
		}
		return result;
	}

	private T getFromCache(Object key)
	{
		return this.queryCache == null ? null : this.queryCache.get(key);
	}
	
	/**
	 * get a List of values from this scope AND its ancestors. values ordered from near to far.
	 * 
	 * @param key
	 * @return
	 */
	public List<T> getAll(Object key)
	{
		List<T> results = new ArrayList<T>();
		HashSet<Map<String, T>> visited = new HashSet<Map<String, T>>();
		getAllHelper(key, visited, results);
		return results;
	}

	private void getAllHelper(Object key, HashSet<Map<String, T>> visited, List<T> results)
	{
		T result = super.get(key);
		if (result != null)
			results.add(result);
		if (this.ancestors != null)
			for (Map<String, T> ancestor : this.ancestors)
				if (containsSame(visited, ancestor))
					continue;
				else
				{
					visited.add(ancestor);
					if (ancestor instanceof MultiAncestorScope)
						((MultiAncestorScope<T>) ancestor).getAllHelper(key, visited, results);
					else
					{
						result = ancestor.get(key);
						if (result != null)
							results.add(result);
					}
				}
	}

	/**
	 * only put value into the scope when it is not null. this prevents shadowing values with the
	 * same key in ancestors.
	 * 
	 * @param key
	 * @param value
	 */
	public void putIfValueNotNull(String key, T value)
	{
		if (value != null)
			put(key, value);
	}

	private void putToCache(Object key, T value)
	{
		if (this.queryCache == null)
			this.queryCache = new LinkedHashMap<String, T>() {
				@Override
				protected boolean removeEldestEntry(java.util.Map.Entry<String, T> eldest)
				{
					if (this.size() > CACHE_SIZE)
						return true;
					return false;
				}
			};
		this.queryCache.put((String) key, value);
	}

	/**
	 * we need this because the ordinary contains() compares using equals() which is overridden by
	 * HashMap, thus not meeting our needs. We want to compare using == in some places in this class.
	 * 
	 * @param <T>
	 * @param theCollection
	 * @param theElement
	 * @return
	 */
	private static <T> boolean containsSame(Collection<T> theCollection, T theElement)
	{
		for (T element : theCollection)
			if (element == theElement)
				return true;
		return false;
	}

	/**
	 * 
	 * @return the list of ancestors.
	 */
	public List<Map<String, T>> getAncestors()
	{
		return this.ancestors;
	}

	/**
	 * add an ancestor to (the end of) the ancestor list.
	 * 
	 * @param ancestor
	 */
	public void addAncestor(Map<String, T> ancestor)
	{
		if (ancestor == null)
			return;
		if (this.ancestors == null)
			this.ancestors = new ArrayList<Map<String, T>>();
		else if (containsSame(this.ancestors, ancestor))
			return;
		this.ancestors.add(ancestor);
		if (this.queryCache != null)
			this.queryCache.clear();
	}

	/**
	 * add ancestors to (the end of) the ancestor list.
	 * 
	 * @param ancestors
	 */
	public void addAncestors(Map<String, T>... ancestors)
	{
		if (ancestors != null)
			for (Map<String, T> ancestor : ancestors)
				this.addAncestor(ancestor);
	}

	/**
	 * remove an ancestor from the ancestor list.
	 * 
	 * @param ancestor
	 */
	public void removeAncestor(Map<String, T> ancestor)
	{
		if (this.ancestors != null)
			this.ancestors.remove(ancestor);
		if (this.queryCache != null)
			this.queryCache.clear();
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
		this.queryCache = null;
		this.clear();
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
