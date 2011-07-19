package ecologylab.serialization.types.scalar;

import ecologylab.serialization.types.CrossLanguageTypeConstants;

public class ReferenceBooleanType extends BooleanType
implements CrossLanguageTypeConstants
{
	public ReferenceBooleanType()
	{
		super(Boolean.class);
	}
}
