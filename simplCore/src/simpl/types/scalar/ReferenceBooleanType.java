package simpl.types.scalar;

import simpl.annotations.dbal.simpl_inherit;
import simpl.types.CrossLanguageTypeConstants;

@simpl_inherit
public class ReferenceBooleanType extends BooleanType
implements CrossLanguageTypeConstants
{
	public ReferenceBooleanType()
	{
		super(Boolean.class);
	}
}
