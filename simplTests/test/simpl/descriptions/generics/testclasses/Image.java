package simpl.descriptions.generics.testclasses;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;


@simpl_inherit
public class Image extends Media
{

	@simpl_scalar
	int	width;

	@simpl_scalar
	int	height;

}
