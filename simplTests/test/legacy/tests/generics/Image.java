package legacy.tests.generics;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

@simpl_inherit
public class Image extends Media
{

	@simpl_scalar
	int	width;

	@simpl_scalar
	int	height;

}
