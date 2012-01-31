package ecologylab.platformspecifics;

import java.lang.reflect.Field;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;

public interface IFundamentalPlatformSpecifics {
	
	// in ecologylab.serialization.ClassDescriptor;
	void deriveSuperGenericTypeVariables(ClassDescriptor classDescriptor);
	
	// in ecologylab.serialization.FieldDescriptor;
	void deriveGenericTypeVariables(FieldDescriptor fieldDescriptor);
	Class<?> getTypeArgClass(Field field, int i, FieldDescriptor fiedlDescriptor);
}
