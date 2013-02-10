package simpl.types.scalar;

import simpl.annotations.dbal.simpl_inherit;
import simpl.types.CrossLanguageTypeConstants;

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
