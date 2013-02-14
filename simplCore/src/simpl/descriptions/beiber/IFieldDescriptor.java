package simpl.descriptions.beiber;

import java.util.Collection;

import simpl.descriptions.FieldType;

public interface IFieldDescriptor {
	Class<?> getDeclaringClass();
	IClassDescriptor getDeclaringClassDescriptor();
	FieldType getFieldType();
	IClassDescriptor getFieldClassDescriptor();
	Collection<IClassDescriptor> getPolymoprhicFieldDescriptors();
	String getName();
}
