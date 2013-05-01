package simpl.descriptions.testclasses;

import java.util.HashMap;
import java.util.Map;

import simpl.annotations.dbal.simpl_map;

public class declaredScalarCompositeInterfaceClass {

	@simpl_map
	Map<String,basicSuperClass> ourMap;
	
	public declaredScalarCompositeInterfaceClass()
	{
		this.ourMap = new HashMap<String, basicSuperClass>();
	}
	
}
