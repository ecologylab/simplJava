package simpl.types.scalar;

import java.util.Arrays;
import java.util.Collection;

import simpl.annotations.ScalarSupportFor;
import simpl.types.ScalarType;

/*
 * A scalar type class for all scalar type classes which deterministically contain their 
 * supported classes (via scalarSupport annotation) 
 */
public abstract class RegisteredScalarType extends ScalarType{
	public RegisteredScalarType()
	{
		Register();
	}
	
	private Collection<Class<?>> supportCache = null;
	
	@Override
	public Collection<Class<?>> getSupportedTypes() {
		if(supportCache == null)
		{
			ScalarSupportFor supports = getClass().getAnnotation(ScalarSupportFor.class);
			
			if(supports == null)
			{
				throw new RuntimeException("Scalar Type implementations must annotate with a ScalarSupportFor annotation!");
			}
			
			if(supports.value().length == 0)
			{
				throw new RuntimeException("SupportFor annotation must have at least one class in it!");
			}
		
			supportCache = Arrays.asList(supports.value());
		}
		return supportCache;
	}
}
