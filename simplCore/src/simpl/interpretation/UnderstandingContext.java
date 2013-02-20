package simpl.interpretation;

import java.util.HashMap;
import java.util.Map;

public class UnderstandingContext {
	
	
	public UnderstandingContext()
	{
		graphObjects = new HashMap<String, Object>();
	}
	
	Map<String, Object> graphObjects;
	
	public void registerID(String ID, Object obj)
	{
		this.graphObjects.put(ID, obj);
	}
	
	public void updateID(String ID, Object obj)
	{
		this.graphObjects.put(ID, obj);
	}
	
	public boolean isIDRegistered(String ID)
	{
		return this.graphObjects.containsKey(ID);
	}
	
	public Object getRegisteredObject(String ID)
	{
		return this.graphObjects.get(ID);
	}
	
}
