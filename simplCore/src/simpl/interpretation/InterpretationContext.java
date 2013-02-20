package simpl.interpretation;

import java.util.HashMap;
import java.util.Map;

public class InterpretationContext {

	
	Map<Object, String> interpretedObjects;
	
	public InterpretationContext()
	{
		this.interpretedObjects = new HashMap<Object, String>();
	}

	private Integer currentID = 1;
	
	public String fetchSimplID()
	{	
		currentID = currentID + 1;
		return currentID.toString();
	}
	
	public boolean isObjectRegistered(Object o)
	{
		return this.interpretedObjects.containsKey(o);
	}
	
	public String getRefForObject(Object o)
	{
		return this.interpretedObjects.get(o);
	}
	
	public String registerObject(Object o)
	{
		String newID = fetchSimplID();
		this.interpretedObjects.put(o, newID);
		return newID;
	}
}

