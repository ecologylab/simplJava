package simpl.descriptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class holds a collection of callbacks to update class descriptor references.
 * Allows us to avoid recursing beyond a single level...
 * Makes things a bit more verbose at spots, but also cleans up a lot of danging logic from the past. 
 *
 */
class ClassDescriptorCallbackMap
{		
	Map<Class<?>, Collection<UpdateClassDescriptorCallback>> ourMap;
	
	public ClassDescriptorCallbackMap()
	{
		ourMap = new HashMap<Class<?>, Collection<UpdateClassDescriptorCallback>>();
	}
	
	public void insertUCDs(Collection<UpdateClassDescriptorCallback> collection)
	{
		this._insertUCDs(ourMap, collection);
	}
	
	public void insertUDC(UpdateClassDescriptorCallback callback)
	{
		this._insertUCD(ourMap, callback);
	}
	
	public boolean isEmpty()
	{
		return ourMap.keySet().isEmpty();
	}
	
	public void resolveUpdates(Class<?> someClass, ClassDescriptor descriptor)
	{
		for(UpdateClassDescriptorCallback ucd : ourMap.get(someClass))
		{
			ucd.updateWithCD(descriptor);
		}
		
		ourMap.remove(someClass);
	}
	
	public Collection<Class<?>> getClassesPendingUpdate()
	{
		return ourMap.keySet();
	}
	
	private void _insertUCDs(Map<Class<?>, Collection<UpdateClassDescriptorCallback>> ourMap, Collection<UpdateClassDescriptorCallback> ucds)
	{
		for(UpdateClassDescriptorCallback ucd : ucds)
		{
			_insertUCD(ourMap, ucd);
		}
	}
	
	private void _insertUCD(Map<Class<?>, Collection<UpdateClassDescriptorCallback>> ourMap, UpdateClassDescriptorCallback ucd)
	{
		if(!ourMap.containsKey(ucd.getClassToUpdate()))
		{
			ourMap.put(ucd.getClassToUpdate(), new LinkedList<UpdateClassDescriptorCallback>());
		}
		ourMap.get(ucd.getClassToUpdate()).add(ucd);
	}
	
}