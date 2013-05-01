package simpl.descriptions.testclasses;

import java.util.LinkedList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;

public class declaredScalarInterfaceListDescription {

	@simpl_collection
	public List<String> interfaceList;
	
	public declaredScalarInterfaceListDescription()
	{
		interfaceList = new LinkedList<String>();
	}
}
