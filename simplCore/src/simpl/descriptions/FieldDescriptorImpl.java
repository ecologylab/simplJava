package simpl.descriptions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import simpl.annotations.dbal.simpl_scalar;

public class FieldDescriptorImpl implements FieldDescriptor {
	
	
	private IMetaInformationProvider metainfo;
	
	public FieldDescriptorImpl()
	{
		this.polymorphicFields = new ArrayList<ClassDescriptor>();
		this.othertags = new ArrayList<String>();
		this.metainfo = new MetaInformationCollection();
		
		
	}
	
	Class<?> declaringClass;

	@simpl_scalar
	FieldType fieldType;
	
	@simpl_scalar
	String name;
	
	ClassDescriptor declaringClassDescriptor;
	
	ClassDescriptor fieldClassDescriptor; 
	
	Collection<ClassDescriptor> polymorphicFields;

	public Collection<ClassDescriptor> getPolymoprhicFieldDescriptors()
	{
		return polymorphicFields;
	}
	
	public void addPolymoprhicFieldDescriptor(ClassDescriptor icd)
	{
		this.polymorphicFields.add(icd);
	}
	
	public ClassDescriptor getFieldClassDescriptor() {
		return fieldClassDescriptor;
	}
	public void setFieldClassDescriptor(ClassDescriptor fieldClassDescriptor) {
		this.fieldClassDescriptor = fieldClassDescriptor;
	}
	public ClassDescriptor getDeclaringClassDescriptor() {
		return declaringClassDescriptor;
	}
	public void setDeclaringClassDescriptor(
			ClassDescriptor declaringClassDescriptor) {
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
	public Collection<MetaInformation> getMetaInformation() {
		// TODO Auto-generated method stub
		return this.metainfo.getMetaInformation();
	}
	
	public void addMetaInformation(MetaInformation imo)
	{
		this.metainfo.addMetaInformation(imo);
	}

	private Collection<String> othertags;

	private EnumerationDescriptor enumerationDescriptor;
	
	public void addOtherTags(String s)
	{
		othertags.add(s);
	}
	
	@Override
	public Collection<String> getOtherTags() {
		// TODO Auto-generated method stub
		return this.othertags;
	}

	@Override
	public boolean containsMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metainfo.containsMetaInformation(name);
	}

	@Override
	public MetaInformation getMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metainfo.getMetaInformation(name);
	}

	@Override
	public EnumerationDescriptor getEnumerationDescriptor() {
		// TODO Auto-generated method stub
		return this.enumerationDescriptor;
	}
	
	public void setEnumerationDescriptor(EnumerationDescriptor ed)
	{
		this.enumerationDescriptor = ed;
	}
	
	

}
