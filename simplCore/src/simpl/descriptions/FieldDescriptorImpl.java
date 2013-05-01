package simpl.descriptions;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ISimplTypesScope;
import simpl.core.ScalarUnmarshallingContext;
import simpl.core.SimplTypesScope;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.ListType;
import simpl.types.MapType;
import simpl.types.ScalarType;

public class FieldDescriptorImpl implements FieldDescriptor {
	
	boolean isEnum;
	boolean isScalar;
	
	private IMetaInformationProvider metainfo;
	
	
	public FieldDescriptorImpl()
	{
		this.polymorphicFields = new HashSet<ClassDescriptor>();
		this.othertags = new ArrayList<String>();
		this.metainfo = new MetaInformationCollection();
		this.genericTypeVars = new ArrayList<GenericTypeVar>();
	}
	
	private List<GenericTypeVar> genericTypeVars;
	
	public List<GenericTypeVar> getGenericTypeVariables() {
		return genericTypeVars;
	}

	public void setGenericTypeVariables(List<GenericTypeVar> genericTypeVars) {
		this.genericTypeVars = genericTypeVars;
	}
	
	Class<?> declaringClass;

	@simpl_scalar
	FieldType fieldType;
	
	ScalarType scalarType;
	
	@simpl_scalar
	String name;
	
	ClassDescriptor declaringClassDescriptor;
	
	ClassDescriptor fieldClassDescriptor; 
	
	Collection<ClassDescriptor> polymorphicFields;

	public Collection<ClassDescriptor> getPolymorphicDescriptors()
	{
		if(!this.polymorphicScopesResolved)
		{
			Collection<ClassDescriptor> classes = new LinkedList<ClassDescriptor>();
			for(String scope : this.polymorphicScopes)
			{
				classes.addAll(resolvePolymorphicScope(scope));
			}
			
			for(ClassDescriptor icd: classes)
			{
				this.addPolymoprhicFieldDescriptor(icd);
			}
			
			this.polymorphicScopesResolved = true;
			this.polymorphicFields.addAll(classes);
		}
		return polymorphicFields;

	}
	
	private Collection<ClassDescriptor> resolvePolymorphicScope(String scopeName)
	{
		Collection<ClassDescriptor> classDescriptors = new LinkedList<ClassDescriptor>();

		ISimplTypesScope s = SimplTypesScope.get(scopeName);

		if(s == null)
		{
			throw new RuntimeException("Simpl Types Scope named ["
					+scopeName == null ? "NULL" : scopeName +
							"] is not created. Please make sure scope has been created " +
					"and that static initialization happens in the proper order.");
		}
		else
		{
			if(this.isEnum())
			{
				throw new RuntimeException("Polymorphic enumerations do not exist!");
			}
			else
			{
				int added = 0;
				for(ClassDescriptor icd : s.getAllClassDescriptors())
				{
					if(this.getFieldClassDescriptor().isSuperClass(icd))
					{
						classDescriptors.add(icd);
						added = added + 1;
					}
				}

				if(added == 0)
				{
					throw new RuntimeException("No simplClasses added to polymorphic field descriptor; did you mean to reference a sts with types that were a supertype of the declared class? Check your code and STS and try again.");
				}
			}
		}
		
		return classDescriptors;
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

	@Override
	public FieldType getType() {
		// TODO Auto-generated method stub
		return this.fieldType;
	}

	@Override
	public boolean isEnum() {
		return this.enumerationDescriptor != null;
	}

	@Override
	public boolean isPolymorphic() {
		// TODO Auto-generated method stub
		return !this.getPolymorphicDescriptors().isEmpty();
	}

	@Override
	public boolean isScalar() {
		// TODO Auto-generated method stub
		return this.getScalarType() != null;//todo
	}
	
	public String getTagName()
	{
		return this.name;
	}

	@Override
	public ScalarType getScalarType() {
		// TODO Auto-generated method stub
		return this.scalarType;
	}
	
	public void setScalarType(ScalarType st)
	{
		this.scalarType = st;
	}

	@Override
	public Field getField() {
		// TODO Auto-generated method stub
		try{
			return this.getDeclaringClass().getDeclaredField(this.getName());
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean isCollection() {
		// TODO Auto-generated method stub
		return false;//TODO IMPL
	}

	@Override
	public Object getWrappedFD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCollectionTag(String tagName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setValue(Object context, Object value) {
		// TODO Auto-generated method stub
		
	}
	
	ListType ourListType;

	public void setListType(ListType listType)
	{
		this.ourListType = listType;
	}
	
	@Override
	public ListType getListType() {
		// TODO Auto-generated method stub
		return this.ourListType;
	}

	MapType ourMapType;
	
	public void setMapType(MapType mapType) {
		// TODO Auto-generated method stub
		this.ourMapType = mapType;
	}

	@Override
	public MapType getMapType()
	{
		return this.ourMapType;
	}

	@Override
	public void setFieldToScalarDefault(Object context,
			ScalarUnmarshallingContext scalarContext) throws SIMPLTranslationException {
		ScalarType st = this.scalarType;
		st.setFieldValue(st.marshal(st.getDefaultValue()), this.getField(), context);
		
	}

	@Override
	public ClassDescriptor getChildClassDescriptor(String tagName) {
		// TODO Auto-generated method stub
		return this.fieldClassDescriptor;
	}

	@Override
	public boolean isWrapped() {
		throw new RuntimeException("Not implementedyet!");
	}
	
	
	boolean polymorphicScopesResolved = false;
	
	private ArrayList<String> polymorphicScopes = new ArrayList<String>();
	
	public void addPolymorphicScope(String scopeName)
	{
		this.polymorphicScopes.add(scopeName);
	}

	@Override
	public Collection<String> getPolymorphicScopes() {
		// TODO Auto-generated method stub
		return this.polymorphicScopes;
	}
	
	@Override
	public String toString()
	{
		return "Field: " + this.getName() + " declared in: " + this.getDeclaringClassDescriptor().toString();
	}
}
