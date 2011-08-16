package ecologylab.serialization.types.scalar;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

@simpl_inherit
public class ReferenceLongType extends LongType
implements CrossLanguageTypeConstants
{
	public ReferenceLongType()
	{
		super(Long.class);
	}

}
