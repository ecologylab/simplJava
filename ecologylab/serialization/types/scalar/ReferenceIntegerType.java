package ecologylab.serialization.types.scalar;

import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

@simpl_inherit
public class ReferenceIntegerType extends IntType
implements CrossLanguageTypeConstants
{
	public ReferenceIntegerType()
	{
		super(Integer.class);
	}

}
