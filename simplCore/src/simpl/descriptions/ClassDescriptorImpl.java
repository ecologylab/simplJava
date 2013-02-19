package simpl.descriptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import simpl.core.TranslationContext;
import simpl.deserialization.ISimplDeserializationHookContextual;
import simpl.deserialization.ISimplDeserializationHooks;

public class ClassDescriptorImpl implements ClassDescriptor,
ISimplDeserializationHooks

{

	private ArrayList<String> otherTags;
	private IMetaInformationProvider metainfo;
	
	public ClassDescriptorImpl()
	{
		this.fields = new ArrayList<FieldDescriptor>();
		this.otherTags = new ArrayList<String>();
		this.metainfo = new MetaInformationCollection();
	}
	
	public Collection<MetaInformation> getMetaInformation()
	{
		return this.metainfo.getMetaInformation();
	}
	
	public void addMetaInformation(MetaInformation imo)
	{
		this.metainfo.addMetaInformation(imo);
	}
	
	public Collection<String> getOtherTags()
	{
		return this.otherTags;
	}
	
	public void addOtherTag(String tag)
	{
		this.otherTags.add(tag);
	}
	
	public Class<?> getJavaClass() {
		return javaClass;
	}
	
	public void setJavaClass(Class<?> javaClass) {
		this.javaClass = javaClass;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	private Class<?> javaClass;
	private String name;
	private List<FieldDescriptor> fields;
	private ClassDescriptor superClassDescriptor;
	private String nameSpace;
	private String simpleName; 
	
	/**
	 * Gets a class descriptor representing the superclass for this class; 
	 * Can be null if @simpl_inherit is not used in the class declaration
	 */
	public ClassDescriptor getSuperClassDescriptor() {
		return superClassDescriptor;
	}

	public void setSuperClassDescriptor(ClassDescriptor superClassDescriptor) {
		this.superClassDescriptor = superClassDescriptor;
	}

	/**
	 * Gets a list of the FieldDescriptors that comprise the described class. 
	 */
	public List<FieldDescriptor> getFields() {
		return fields;
	}
	
	/**
	 * Adds a field to this class descriptor
	 * @param ifd Field to add
	 */
	public void addField(FieldDescriptor ifd)
	{
		this.fields.add(ifd);
	}

	@Override
	public void deserializationInHook(TranslationContext translationContext) {
		// this is empty for this class... 
	}

	@Override
	public void deserializationPostHook(TranslationContext translationContext,
			Object object) {
		// this is empty for this class... 
	}

	@Override
	public void deserializationPreHook(TranslationContext translationContext) {
		// We need to register this class descriptor, if it hasn't already been
		// so that all of our cycle code works nicely. 
		ClassDescriptors.registerClassDescriptor(this);
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
	public boolean isSuperClass(ClassDescriptor icd) {
		// This is a really bad check; will work for now. 
		if(icd != null)
		{
			String thisName = this.getName();
			String otherName = icd.getName();
			return thisName.equals(otherName);
					
		}else{
			return false;
		}
	}

	@Override
	public String getNamespace() {
		return this.nameSpace;
	}
	
	public void setNamespace(String namespace){
		this.nameSpace = namespace;
	}

	@Override
	public String getSimpleName() {
		return this.simpleName;
	}
	
	public void setSimpleName(String simpleName)
	{
		this.simpleName = simpleName;
	}
}
