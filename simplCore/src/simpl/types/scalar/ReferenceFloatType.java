package simpl.types.scalar;

import simpl.annotations.dbal.simpl_inherit;
import simpl.types.CrossLanguageTypeConstants;

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
