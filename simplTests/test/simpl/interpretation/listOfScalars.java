package simpl.interpretation;

import java.util.LinkedList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_scalar;

public class listOfScalars {
	public listOfScalars(){
		myList = new LinkedList<Integer>();
	}
	
	@simpl_collection
	public List<Integer> myList;

	@simpl_scalar
	public String myString;

}


