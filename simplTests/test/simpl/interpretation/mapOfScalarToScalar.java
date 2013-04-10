package simpl.interpretation;

import java.util.HashMap;
import java.util.Map;

import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_scalar;

public class mapOfScalarToScalar {
	
	@simpl_map
	public Map<String, Integer> ourMap;
	
	@simpl_scalar
	public String myString;
	
	public mapOfScalarToScalar()
	{
		this.ourMap = new HashMap<String, Integer>();
	}
	
}
