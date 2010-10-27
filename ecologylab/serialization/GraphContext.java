package ecologylab.serialization;

import java.util.HashMap;

public class GraphContext
{
	public HashMap<Integer, ElementState>	marshalledObjects				= new HashMap<Integer, ElementState>();

	public HashMap<Integer, ElementState>	visitedElements					= new HashMap<Integer, ElementState>();

	public HashMap<Integer, ElementState>	needsAttributeHashCode	= new HashMap<Integer, ElementState>();

	public HashMap<String, ElementState>		unmarshalledObjects			= new HashMap<String, ElementState>();
	
	void GraphContext()
	{
		
	}
}