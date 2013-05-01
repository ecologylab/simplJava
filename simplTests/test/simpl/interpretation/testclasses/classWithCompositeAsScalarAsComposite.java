package simpl.interpretation.testclasses;

import simpl.annotations.dbal.simpl_composite;

public class classWithCompositeAsScalarAsComposite {

	@simpl_composite
	public basicCompositeAsScalar ourComposite;
	
	public classWithCompositeAsScalarAsComposite()
	{
		ourComposite = new basicCompositeAsScalar();
	}
}
