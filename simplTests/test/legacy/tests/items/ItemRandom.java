package legacy.tests.items;

import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;

@simpl_tag("item_one")
public class ItemRandom extends ItemBase
{
	@simpl_scalar
	String randomString;
	
	public ItemRandom()
	{
		
	}
	
	public ItemRandom(String randomString, int var)
	{
		this.randomString = randomString;
		this.var = var;
	}
}
