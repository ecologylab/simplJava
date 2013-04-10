package ecologylab.collections;

import java.util.ArrayList;
import java.util.HashMap;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;


public class MultiMap<T, S extends Object>
{

	private HashMap<T, ArrayList<S>>	map;

	public MultiMap()
	{
		map = new HashMap<T, ArrayList<S>>();
	}

	/**
	 * method to add the given key, value pair into the multimap
	 * 
	 * @param key
	 * @param value
	 * @return whether the item is successfully added to the collection
	 */
	public boolean put(T key, S value)
	{
		if (!map.containsKey(key))
		{
			ArrayList<S> collection = new ArrayList<S>(1);
			collection.add(value);
			map.put(key, collection);
			return true;
		}
		else
		{
			ArrayList<S> collection = map.get(key);
			if (containsValue(collection, value) == -1)
			{
				collection.add(value);
				return true;
			}
		}
		return false;
	}

	/**
	 * method deciding whether the given key, value is present
	 * returns the ordered index in bucket array, else -1 
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public int contains(T key, S value)
	{
		if (map.containsKey(key))
		{
			ArrayList<S> collection = map.get(key);
			return containsValue(collection, value);
		}
		else
		{
			return -1;
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
		if (map.containsKey(key))
		{
			return map.get(key).get(0);
		}
		return null;
	}

	/**
	 * method returns whether the given value is in the collection based on the equals operator
	 * return -1 if not exists else the ordered index if it does. 
	 * 
	 * @param collection
	 * @param value
	 * @return
	 */
	private int containsValue(ArrayList<S> collection, S value)
	{
		ClassDescriptor classDescriptor = ClassDescriptors.getClassDescriptor(value.getClass());

		int index = 0;
		if (classDescriptor.getStrictObjectGraphRequired())
		{
			for (S item : collection)
			{
				if (item == value)
				{
					return index;
				}
				index ++;
			}
			return -1;
		}
		else
		{
			for (S item : collection)
			{
				if (item.equals(value))
				{
					return index;
				}
				index ++;
			}
			return -1;
		}
	}

	public void clear()
	{
		map.clear();
	}

}
