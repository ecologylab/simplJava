package simpl.descriptions.testclasses;

import java.util.HashMap;

import simpl.annotations.dbal.simpl_map;

public class declaredScalarCompositeMapClass {

	@simpl_map
	public HashMap<String, basicSuperClass> ourMap;
	
	public declaredScalarCompositeMapClass()
	{
		ourMap = new HashMap<String, basicSuperClass>();
	}
	
}
