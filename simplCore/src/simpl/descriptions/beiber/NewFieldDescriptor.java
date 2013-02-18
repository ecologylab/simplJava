package simpl.descriptions.beiber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.FieldType;

public class NewFieldDescriptor implements IFieldDescriptor {
	
	
	public NewFieldDescriptor()
	{
		this.polymorphicFields = new ArrayList<IClassDescriptor>();
	}
	
	Class<?> declaringClass;

	@simpl_scalar
	FieldType fieldType;
	
	@simpl_scalar
	String name;
	
	IClassDescriptor declaringClassDescriptor;
	
	IClassDescriptor fieldClassDescriptor; 
	
	Collection<IClassDescriptor> polymorphicFields; 
	
	public Collection<IClassDescriptor> getPolymoprhicFieldDescriptors()
	{
		return polymorphicFields;
	}
	
	public void addPolymoprhicFieldDescriptor(IClassDescriptor icd)
	{
		this.polymorphicFields.add(icd);
	}
	
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

	@Override
	public Collection<String> getMetaInformation() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
