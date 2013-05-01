package simpl.descriptions.testclasses;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;

@simpl_inherit
public class inheritSuperClass extends basicSuperClass{
	@simpl_scalar
	public Integer baseField;
	
	public inheritSuperClass() {}
}
