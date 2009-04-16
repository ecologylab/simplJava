package ecologylab.xml.types.element;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import ecologylab.xml.ElementState;

public class StringDoubleMap extends ElementState implements Map<String,Double>
{
	
	private HashMap<String,Double> hashMap = new HashMap<String,Double>();
	
	@xml_collection("feature") ArrayList<StringDoubleEntry> entries;
	
	public StringDoubleMap()
	{
	}

	public StringDoubleMap(int size)
	{
		hashMap = new HashMap<String,Double>(size);
	}
	
	public StringDoubleMap(Map<String, Double> map)
	{
		hashMap = new HashMap<String, Double>(map);
	}

	public void clear()
	{
		hashMap.clear();
		
	}

	public boolean containsKey(Object key)
	{
		return hashMap.containsKey(key);
	}

	public boolean containsValue(Object value)
	{
		return hashMap.containsValue(value);
	}

	public Set<java.util.Map.Entry<String, Double>> entrySet()
	{
		return hashMap.entrySet();
	}

	public Double get(Object key)
	{
		return hashMap.get(key);
	}

	public boolean isEmpty()
	{
		return hashMap.isEmpty();
	}

	public Set<String> keySet()
	{
		return hashMap.keySet();
	}

	public Double put(String key, Double value)
	{
		return hashMap.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Double> t)
	{
		hashMap.putAll(t);
	}

	public Double remove(Object key)
	{
		return hashMap.remove(key);
	}

	public int size()
	{
		return hashMap.size();
	}

	public Collection<Double> values()
	{
		return hashMap.values();
	}
	
	@Override
	protected void preTranslationProcessingHook()
	{
		Set<Entry<String, Double>> entrySet = this.entrySet();
		entries = new ArrayList<StringDoubleEntry>(entrySet.size());
		for (Entry<String, Double> e : entrySet)
			entries.add(new StringDoubleEntry(e));
	}
	
	@Override
	protected void postTranslationProcessingHook()
	{
		hashMap.clear();
		for (StringDoubleEntry e : entries)
			hashMap.put(e.getKey(), e.getValue());
	}
}
