/**
 * 
 */
package ecologylab.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class OverlappingAnyKeySets<K extends Object, O extends Object> extends
		OverlappingSets<O>
{
	Map<K, Long>	keyToNumberMapping;

	/**
	 * 
	 */
	public OverlappingAnyKeySets(K[] keys)
	{
		super();

		keyToNumberMapping = new HashMap<K, Long>();

		long i = 0;

		for (K key : keys)
		{
			keyToNumberMapping.put(key, ((long) Math.pow(2, i)));

			i++;
		}
	}
	
	/**
	 * @param playerNames
	 */
	public OverlappingAnyKeySets(List<K> keys)
	{
		super();
		
		keyToNumberMapping = new HashMap<K, Long>();
		
		long i = 0;
		
		for (K key : keys)
		{
			keyToNumberMapping.put(key, ((long) Math.pow(2, i)));
			i++;
		}
	}

	public void put(K key, O value)
	{
		super.put(keyToNumberMapping.get(key), value);
	}
	
	public void put(K[] keys, O value)
	{
		super.put(computeNumericalKey(keys), value);
	}

	/**
	 * @param keys
	 * @return
	 */
	private long computeNumericalKey(K[] keys)
	{
		long numKey = 0;
		
		for (K key : keys)
		{
			numKey = numKey | keyToNumberMapping.get(key);
		}
		return numKey;
	}
	
	public void put(Collection<K> keys, O value)
	{
		long numKey = 0;
		
		for (K key : keys)
		{
			numKey = numKey | keyToNumberMapping.get(key);
		}
		
		super.put(numKey, value);
	}
	
	public List<O> getSet(K... keys)
	{
		return super.getSet(computeNumericalKey(keys));
	}

	public static void main(String[] args)
	{
		String[] keys =
		{ "andruid", "bill", "zach", "ross" };

		OverlappingAnyKeySets<String, String> t = new OverlappingAnyKeySets<String, String>(
				keys);
		
		t.put("andruid", "a");
		t.put(keys, "vowels");
		
		String[] t1 = {"zach", "ross"};
		String[] t2 = {"zach", "ross", "andruid"};
		
		t.put(t1, "two");
		t.put(t2, "three");
		
		t.put(t1, "2");
		t.put(t1, "2two");

		System.out.println(t.toString());
	}
}
