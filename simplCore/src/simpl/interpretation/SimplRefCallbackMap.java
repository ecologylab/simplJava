package simpl.interpretation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.UpdateClassDescriptorCallback;

public class SimplRefCallbackMap {
	
	Map<String, Collection<UpdateSimplRefCallback>> ourMap;
	
	public SimplRefCallbackMap()
	{
		ourMap = new HashMap<String, Collection<UpdateSimplRefCallback>>();
	}
	
	public void insertCallbacks(Collection<UpdateSimplRefCallback> collection)
	{
		for(UpdateSimplRefCallback ucd : collection)
		{
			this.insertCallback(ucd);
		}
	}
	
	public void insertCallback(UpdateSimplRefCallback callback)
	{
		if(!ourMap.containsKey(callback.getID()))
		{
			ourMap.put(callback.getID(), new LinkedList<UpdateSimplRefCallback>());
		}
		ourMap.get(callback.getID()).add(callback);
	}
	
	public boolean isEmpty()
	{
		return ourMap.keySet().isEmpty();
	}
	
	public void resolveUpdates(String someRef, Object composite)
	{
		for(UpdateSimplRefCallback ucd : ourMap.get(someRef))
		{
			ucd.resolveUpdate(composite);
		}
		
		ourMap.remove(someRef);
	}
	
	public Collection<String> getRefsPendingUpdate()
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

	}
	
}