package ecologylab.serialization.types.scalar;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Entry for boxed Float.
 * 
 * @author andruid
 */
@simpl_inherit
public class ReferenceFloatType extends FloatType
implements CrossLanguageTypeConstants
{
	public ReferenceFloatType()
	{
		super(Float.class);
	}
}
