package ecologylab.simpl;

import java.util.ArrayList;
import java.util.List;

import ecologylab.serialization.annotations.simpl_collection;

public class basicCompositeList {

	@simpl_collection("collect")
	public List<basicComposite> ourList;
	
	public basicCompositeList()
	{
		ourList = new ArrayList<basicComposite>();
	}
}
