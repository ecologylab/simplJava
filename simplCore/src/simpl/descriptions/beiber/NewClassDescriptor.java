package simpl.descriptions.beiber;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import simpl.core.TranslationContext;
import simpl.deserialization.ISimplDeserializationHookContextual;
import simpl.deserialization.ISimplDeserializationHooks;

public class NewClassDescriptor implements IClassDescriptor,
ISimplDeserializationHooks

{

	private ArrayList<String> otherTags;
	private IMetaInformationProvider metainfo;
	
	public NewClassDescriptor()
	{
		this.fields = new ArrayList<IFieldDescriptor>();
		this.otherTags = new ArrayList<String>();
		this.metainfo = new MetaInformationCollection();
	}
	
	public Collection<IMetaInformation> getMetaInformation()
	{
		return this.metainfo.getMetaInformation();
	}
	
	public void addMetaInformation(IMetaInformation imo)
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
	private List<IFieldDescriptor> fields;
	private IClassDescriptor superClassDescriptor; 
	
	/**
	 * Gets a class descriptor representing the superclass for this class; 
	 * Can be null if @simpl_inherit is not used in the class declaration
	 */
	public IClassDescriptor getSuperClassDescriptor() {
		return superClassDescriptor;
	}

	public void setSuperClassDescriptor(IClassDescriptor superClassDescriptor) {
		this.superClassDescriptor = superClassDescriptor;
	}

	/**
	 * Gets a list of the FieldDescriptors that comprise the described class. 
	 */
	public List<IFieldDescriptor> getFields() {
		return fields;
	}
	
	/**
	 * Adds a field to this class descriptor
	 * @param ifd Field to add
	 */
	public void addField(IFieldDescriptor ifd)
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
	public IMetaInformation getMetaInformation(String name) {
		// TODO Auto-generated method stub
		return this.metainfo.getMetaInformation(name);
	}
}
