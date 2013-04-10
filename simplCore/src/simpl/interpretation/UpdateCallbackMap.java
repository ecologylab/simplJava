package simpl.interpretation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

// This is particularly gnarly generic type definition. WORTH IT. 
public class UpdateCallbackMap<CallbackKey, CallbackType extends IUpdateCallback<CallbackKey, UpdateObjectType>, UpdateObjectType> 
{
	Map<CallbackKey, Collection<CallbackType>> ourMap;
	
	/**
	 * Creates a new SimplRefCallbackMap initialized with an empty map of callbacks.
	 */
	public UpdateCallbackMap()
	{
		ourMap = new HashMap<CallbackKey, Collection<CallbackType>>();
	}
	
	/**
	 * Inserts a collection of callbacks into the SimplRefCallbackMap 
	 * @param collection Collection to insert; Each callback will get sorted by the ID it relies upon. 
	 */
	public void insertCallbacks(Collection<CallbackType> collection)
	{
		for(CallbackType ucd : collection)
		{
			this.insertCallback(ucd);
		}
	}
	
	/**
	 * Insert a single callback into the callback collection; sorts it by the ID it relies upon 
	 * @param callback
	 */
	public void insertCallback(CallbackType callback)
	{
		if(!ourMap.containsKey(callback.getUpdateKey()))
		{
			ourMap.put(callback.getUpdateKey(), new LinkedList<CallbackType>());
		}
		size++;
		ourMap.get(callback.getUpdateKey()).add(callback);
	}
	
	public boolean isEmpty()
	{
		return ourMap.keySet().isEmpty();
	}
	
	public void resolveCallbacks(CallbackKey callbackKey, UpdateObjectType updateValue)
	{
		for(CallbackType ucd : ourMap.get(callbackKey))
		{
			ucd.runUpdateCallback(updateValue);
			size--;
		}

		ourMap.remove(callbackKey);
	}
	
	public Collection<CallbackKey> getPendingUpdateKeys()
	{
		return ourMap.keySet();
	}
	
	int size = 0;

	public int size()
	{
		return size;
	}
}
