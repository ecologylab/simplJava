package simpl.interpretation;

import java.util.Map;

public class UnderstandingContext {
	
	Map<String, Object> graphObjects;
	
	public void registerID(String ID, Object obj)
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
