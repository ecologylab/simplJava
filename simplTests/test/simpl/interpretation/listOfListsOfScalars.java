package simpl.interpretation;

import java.util.List;
import java.util.ArrayList;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_scalar;

public class listOfListsOfScalars {

	@simpl_scalar
	public String myString;
	
	@simpl_collection
	public List<ArrayList<Integer>> listOfLists; // We have to spec the inner list type b/c we can't have List<List<>> in Java. No contravariance
	
	public listOfListsOfScalars()
	{
		this.listOfLists = new ArrayList<ArrayList<Integer>>();
	}
}
