/**
 * 
 */
package ecologylab.serialization;

import ecologylab.generic.Debug;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * @author quyin
 * 
 */
public class GenericTypeVar extends Debug
{
	/**
	 * the name of the generic type specification, e.g. T.
	 */
	@simpl_scalar
	String					name;

	/**
	 * the class bound.
	 */
	@simpl_composite
	ClassDescriptor	classDescriptor;

	@simpl_composite
	GenericTypeVar	boundsGenericTypeVar;

	public String getName()
	{
		return name;
	}

	public ClassDescriptor getClassDescriptor()
	{
		return classDescriptor;
	}

	public GenericTypeVar getBoundsGenericTypeVar()
	{
		return boundsGenericTypeVar;
	}

	public String toString()
	{
		// for debug
		return String.format("<%s : %s (%s)>",
				name,
				classDescriptor == null ? "" : classDescriptor.toString(),
				boundsGenericTypeVar == null ? "" : boundsGenericTypeVar.toString()
				);
	}
}