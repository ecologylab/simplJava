package legacy.tests.configuration;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;

@simpl_inherit
public class PrefInteger extends Pref
{
	@simpl_scalar
	public int	intValue;

	public PrefInteger()
	{

	}
}
