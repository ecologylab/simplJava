package simpl.interpretation;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;

public class OuterComposite {

	public OuterComposite()
	{
	}
	
	@simpl_composite
	public plainComposite ReferencedComp;
	
	@simpl_scalar
	public String myString;
}

