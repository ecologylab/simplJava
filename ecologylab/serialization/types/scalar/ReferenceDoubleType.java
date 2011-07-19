package ecologylab.serialization.types.scalar;

import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Entry for boxed Double.
 * 
 * @author andruid
 */
public class ReferenceDoubleType extends DoubleType implements CrossLanguageTypeConstants
{
	public ReferenceDoubleType()
	{
		super(Double.class);
	}
}
