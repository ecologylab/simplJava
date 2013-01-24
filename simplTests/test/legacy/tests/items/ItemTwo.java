package legacy.tests.items;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class ItemTwo extends ItemBase
{
	@simpl_scalar
	String testString;
	
	public ItemTwo()
	{
		
	}
	
	public ItemTwo(String testString, int var)
	{
		this.testString = testString;
		this.var = var;
	}
}
