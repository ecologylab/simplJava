package simpl.descriptions;

import java.util.LinkedList;

import simpl.annotations.dbal.simpl_collection;

public class declaredListDescription {
	public declaredListDescription()
	{
	}
	
	@simpl_collection
	public LinkedList<String> ourList;
	
}
