package ecologylab.fundamental;

import ecologylab.serialization.annotations.simpl_scalar;

public class basicComposite {
	
	@simpl_scalar
	public Integer a;
	@simpl_scalar
	public int b;
	
	public basicComposite(){}

	public basicComposite(int i)
	{
		this.a = i;
		this.b = i+1;
	}
}
