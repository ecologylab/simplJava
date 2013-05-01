package simpl.interpretation.testclasses;

import simpl.annotations.dbal.simpl_scalar;

public class classWithCompositeAsScalarAsScalar {

	@simpl_scalar
	public basicCompositeAsScalar ourScalar;
	
	public classWithCompositeAsScalarAsScalar()
	{
		this.ourScalar = new basicCompositeAsScalar();
	}
}


