package ecologylab.platformspecifics;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.GenericTypeVar;

public interface IFundamentalPlatformSpecifics {
	
	// in ecologylab.serialization.ClassDescriptor;
	void deriveSuperGenericTypeVariables(ClassDescriptor classDescriptor);
	
	// in ecologylab.serialization.FieldDescriptor;
	void deriveGenericTypeVariables(FieldDescriptor fieldDescriptor);
	Class<?> getTypeArgClass(Field field, int i, FieldDescriptor fiedlDescriptor);
	
	// in ecologylab.serialization.GenericTypeVar;
	void checkBoundParameterizedTypeImpl (GenericTypeVar g, Type bound);
	void checkTypeWildcardTypeImpl(GenericTypeVar g, Type type);
	void checkTypeParameterizedTypeImpl(GenericTypeVar g, Type type);
}
