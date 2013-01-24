package legacy.tests.configuration;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class PrefInteger extends Pref
{
	@simpl_scalar
	public int	intValue;

	public PrefInteger()
	{

	}
}
