package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;

import ecologylab.serialization.ElementState;

public class MultiMap<T,S extends ElementState> {

	private HashMap<T,ArrayList<S>> map;
	
	public MultiMap()
	{
		map = new HashMap<T,ArrayList<S>>();
	}
	
	/**
	 * method to add the given key, value pair into the multimap
	 * 
	 * @param key
	 * @param value
	 * @return whether the item is successfully added to the collection
	 */
	public boolean put(T key,S value)
	{
		if(!map.containsKey(key))
		{
			ArrayList<S> collection = new ArrayList<S>(1);
			collection.add(value);
			map.put(key,collection);
			return true;
		}else
		{
			ArrayList<S> collection = map.get(key);
			if(!containsValue(collection,value))
			{
				collection.add(value);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * method deciding whether the given key, value is present
	 * 	
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean contains(T key,S value)
	{
		if(map.containsKey(key))
		{
			ArrayList<S> collection = map.get(key);
			return containsValue(collection,value);
		}else
		{
			return false;
		}
	}
	
	/**
	 * method return the size of the multimap
	 * 
	 * @return
	 */
	public int size()
	{
		return map.size();
	}
	
	/**
	 * method returning the first item in the list corresponds to the given key
	 * 	
	 * @param key
	 * @return
	 */
	public S get(T key)
	{
		if(map.containsKey(key))
		{
			return map.get(key).get(0);
		}
		return null;
	}
	
	/**
	 * method returns whether the given value is in the collection based on the equals operator
	 * 
	 * @param collection
	 * @param value
	 * @return
	 */
	private boolean containsValue(ArrayList<S> collection, S value)
	{
		if(value.getStrictObjectGraphRequired())
		{
			for(S item : collection)
			{
				if(item == value)
				{
					return true;
				}
			}
			return false;
		}else
		{
			for(S item : collection)
			{
				if(item.equals(value))
				{
					return true;
				}
			}
			return false;
		}
	}	
}
