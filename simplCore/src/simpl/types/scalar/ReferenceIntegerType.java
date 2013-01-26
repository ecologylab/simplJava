package simpl.types.scalar;

import simpl.annotations.dbal.simpl_inherit;
import simpl.types.CrossLanguageTypeConstants;

@simpl_inherit
public class ReferenceIntegerType extends IntType
implements CrossLanguageTypeConstants
{
	public ReferenceIntegerType()
	{
		super(Integer.class);
	}

}
