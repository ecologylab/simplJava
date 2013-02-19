package simpl.descriptions.beiber;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.FieldType;

public class NewFieldDescriptor implements IFieldDescriptor {
	
	
	private IMetaInformationProvider metainfo;
	
	public NewFieldDescriptor()
	{
		this.polymorphicFields = new ArrayList<IClassDescriptor>();
		this.othertags = new ArrayList<String>();
		this.metainfo = new MetaInformationCollection();
		
		
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
	public Collection<IMetaInformation> getMetaInformation() {
		// TODO Auto-generated method stub
		return this.metainfo.getMetaInformation();
	}
	
	public void addMetaInformation(IMetaInformation imo)
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
	public IMetaInformation getMetaInformation(String name) {
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
