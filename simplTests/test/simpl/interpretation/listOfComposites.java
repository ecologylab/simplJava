package simpl.interpretation;

import java.util.ArrayList;
import java.util.List;

import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_scalar;

public class listOfComposites {

	@simpl_scalar
	public String myString;
	
	@simpl_collection
	public List<simplerInnerListComposite> listOfComposites;
	
	public listOfComposites()
	{
		this.listOfComposites = new ArrayList<simplerInnerListComposite>();
	}
}
