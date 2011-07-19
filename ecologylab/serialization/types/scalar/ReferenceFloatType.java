package ecologylab.serialization.types.scalar;

import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Entry for boxed Float.
 * 
 * @author andruid
 */
public class ReferenceFloatType extends FloatType
implements CrossLanguageTypeConstants
{
	public ReferenceFloatType()
	{
		super(Float.class);
	}
}
