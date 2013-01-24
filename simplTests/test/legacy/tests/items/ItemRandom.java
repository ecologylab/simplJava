package legacy.tests.items;

import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

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
