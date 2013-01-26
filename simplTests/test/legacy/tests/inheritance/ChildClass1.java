package legacy.tests.inheritance;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;

@simpl_inherit
public class ChildClass1 extends BaseClass
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)
	int ccvar1 = 1;
	
	public ChildClass1()
	{
	}
}
