package simpl.types.scalar;

import simpl.annotations.dbal.simpl_inherit;
import simpl.types.CrossLanguageTypeConstants;

@simpl_inherit
public class ReferenceLongType extends LongType
implements CrossLanguageTypeConstants
{
	public ReferenceLongType()
	{
		super(Long.class);
	}

}
