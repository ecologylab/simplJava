package ecologylab.serialization.types.scalar;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.types.CrossLanguageTypeConstants;

/**
 * Entry for boxed Double.
 * 
 * @author andruid
 */
@simpl_inherit
public class ReferenceDoubleType extends DoubleType implements CrossLanguageTypeConstants
{
	public ReferenceDoubleType()
	{
		super(Double.class);
	}
}
