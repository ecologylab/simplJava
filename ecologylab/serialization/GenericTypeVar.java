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
		@simpl_scalar
		String					name;
		
		@simpl_composite	
		ClassDescriptor	classDescriptor;
		
		@simpl_scalar
		GenericTypeVar	genericTypeVar;
}
