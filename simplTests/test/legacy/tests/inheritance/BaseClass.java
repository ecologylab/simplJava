package legacy.tests.inheritance;

import simpl.annotations.dbal.simpl_other_tags;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;

@simpl_other_tags({"the_field"})
@simpl_tag("fred")
public class BaseClass
{
	@simpl_tag("new_tag_var")
	@simpl_other_tags("other_tag_var")
	@simpl_scalar
	int var = 3;
	
	public BaseClass()
	{
	}
}
