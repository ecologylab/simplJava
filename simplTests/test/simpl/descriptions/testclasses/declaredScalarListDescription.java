package simpl.descriptions.testclasses;

import java.util.LinkedList;

import simpl.annotations.dbal.simpl_collection;

public class declaredScalarListDescription {
	public declaredScalarListDescription()
	{
	}
	
	@simpl_collection
	public LinkedList<String> ourList;
	
}
