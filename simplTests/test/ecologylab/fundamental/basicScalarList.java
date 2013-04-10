package ecologylab.fundamental;

import java.util.ArrayList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;


public class basicScalarList {

	@simpl_collection("collect")
	public List<Integer> ourList;
	
	public basicScalarList()
	{
		ourList = new ArrayList<Integer>();
	}
}
