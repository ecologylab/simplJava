package simpl.descriptions.beiber;

import simpl.descriptions.FieldType;

public interface IFieldDescriptor {
	Class<?> getDeclaringClass();
	IClassDescriptor getDeclaringClassDescriptor();
	FieldType getFieldType();
	IClassDescriptor getFieldClassDescriptor();
	String getName();
}
