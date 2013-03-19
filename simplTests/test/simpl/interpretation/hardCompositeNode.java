package simpl.interpretation;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;

public class hardCompositeNode {

	public hardCompositeNode()
	{
		
	}
	
	@simpl_composite
	public hardCompositeNode left;
	public hardCompositeNode right;
	
	@simpl_scalar
	public String myString;
}
