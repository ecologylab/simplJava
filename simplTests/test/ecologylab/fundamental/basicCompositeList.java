package ecologylab.fundamental;

import java.util.ArrayList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;


public class basicCompositeList {

	@simpl_collection("collect")
	public List<basicComposite> ourList;
	
	public basicCompositeList()
	{
		ourList = new ArrayList<basicComposite>();
	}
}
