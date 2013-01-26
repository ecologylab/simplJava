package legacy.tests.items;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;

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
