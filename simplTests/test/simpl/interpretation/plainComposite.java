package simpl.interpretation;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;

public class plainComposite {

	public plainComposite()
	{
	}
	
	@simpl_composite
	public myScalars myComposite;
	
	@simpl_scalar
	public String myString;
}
