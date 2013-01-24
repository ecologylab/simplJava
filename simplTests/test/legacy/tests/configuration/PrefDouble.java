package legacy.tests.configuration;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class PrefDouble extends Pref
{
	@simpl_scalar
	public double	doubleValue;

	public PrefDouble()
	{

	}
}
