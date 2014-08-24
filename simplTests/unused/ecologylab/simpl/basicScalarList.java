package ecologylab.simpl;

import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_collection;

public class basicScalarList {

	@simpl_collection("collect")
	public List<Integer> ourList;
	
	public basicScalarList()
	{
		ourList = new ArrayList<Integer>();
	}
}
