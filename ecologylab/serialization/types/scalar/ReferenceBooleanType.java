package ecologylab.serialization.types.scalar;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

@simpl_inherit
public class ReferenceBooleanType extends BooleanType
implements CrossLanguageTypeConstants
{
	public ReferenceBooleanType()
	{
		super(Boolean.class);
	}
}
