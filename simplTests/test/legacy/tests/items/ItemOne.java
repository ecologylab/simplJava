package legacy.tests.items;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class ItemOne extends ItemBase
{
	@simpl_scalar
	int testing;
	
	
	public ItemOne()
	{
		
	}
	
	public ItemOne(int testing, int var)
	{
		this.testing = testing;
		this.var = var;
	}
}
