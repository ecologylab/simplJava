package ecologylab.translators.javascript.test;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

public class ReferToSelf extends ElementState
{
	@simpl_scalar
	String someData;
	
	@simpl_composite
	ReferToSelf referenceToSelf;
	
	public ReferToSelf()
	{
		someData = "I have a null for referenceToSelf";
		referenceToSelf = null;
	}
	public ReferToSelf(int num)
	{
		someData = "I have a null for referenceToSelf";
		referenceToSelf = this;
	}
}
