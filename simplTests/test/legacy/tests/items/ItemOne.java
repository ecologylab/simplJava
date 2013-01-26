package legacy.tests.items;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;

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
