package simpl.descriptions.beiber;

import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.FieldType;

public class NewFieldDescriptor implements IFieldDescriptor {
	
	Class<?> declaringClass;

	@simpl_scalar
	FieldType fieldType;
	
	@simpl_scalar
	String name;
	
	IClassDescriptor declaringClassDescriptor;
	
	IClassDescriptor fieldClassDescriptor; 
	
	public IClassDescriptor getFieldClassDescriptor() {
		return fieldClassDescriptor;
	}
	public void setFieldClassDescriptor(IClassDescriptor fieldClassDescriptor) {
		this.fieldClassDescriptor = fieldClassDescriptor;
	}
	public IClassDescriptor getDeclaringClassDescriptor() {
		return declaringClassDescriptor;
	}
	public void setDeclaringClassDescriptor(
			IClassDescriptor declaringClassDescriptor) {
		this.declaringClassDescriptor = declaringClassDescriptor;
	}
	
	
	public Class<?> getDeclaringClass() {
		return declaringClass;
	}
	public void setDeclaringClass(Class<?> declaringClass) {
		this.declaringClass = declaringClass;
	}
	public FieldType getFieldType() {
		return fieldType;
	}
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
