package simpl.descriptions;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;

import simpl.core.ISimplTypesScope;
import simpl.core.TranslationContext;
import simpl.descriptions.indexers.FieldDescriptorIndexer;
import simpl.deserialization.ISimplDeserializationHookContextual;
import simpl.deserialization.ISimplDeserializationHooks;

public class ClassDescriptorImpl implements ClassDescriptor,
ISimplDeserializationHooks
{
	private ArrayList<String> otherTags;
	private IMetaInformationProvider metainfo;
	private FieldDescriptorIndexer indexer;
	private List<GenericTypeVar> genericTypeVars;
	
	public List<GenericTypeVar> getGenericTypeVariables() {
		return genericTypeVars;
	}

	public void setGenericTypeVariables(List<GenericTypeVar> genericTypeVars) {
		this.genericTypeVars = genericTypeVars;
	}

	public ClassDescriptorImpl()
	{
		this.fields = new ArrayList<FieldDescriptor>();
		this.otherTags = new ArrayList<String>();
		this.metainfo = new MetaInformationCollection();
		this.indexer = new FieldDescriptorIndexer();
		
		this.genericTypeVars = new ArrayList<GenericTypeVar>();
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
		if(this.javaClass == null)
		{
			try {
				this.javaClass = Class.forName(this.name);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
		}
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
	private String tagName; 
	
	/**
	 * Gets a class descriptor representing the superclass for this class; 
	 * Can be null if @simpl_inherit is not used in the class declaration
	 */
	public ClassDescriptor getSuperClassDescriptor() {
		return superClassDescriptor;
	}

	/**
	 * Sets the superclass for this given class. Also updates the FieldDescriptors 
	 * to include superclass fields. 
	 * 
	 * This shouldn't be done multiple times; if it is, it'll throw a runtime exception. 
	 * @param superClassDescriptor
	 */
	public void setSuperClassDescriptor(ClassDescriptor superClassDescriptor) {
		if(superclassSet)
		{
			throw new RuntimeException("The superclass descriptor should not be set multiple times!");
		}
		else
		{
			this.superClassDescriptor = superClassDescriptor;
				
			for(FieldDescriptor fd : superClassDescriptor.allFieldDescriptors())
			{
				// Just baldly add the field; if we need to, we can reprocess the fieldDescriptor
				this.addField(fd);
			}
			
			superclassSet = true;
		}
	}
	
	boolean superclassSet=false;

	/**
	 * Gets a list of the FieldDescriptors that comprise the described class. 
	 */
	@Override
	public List<FieldDescriptor> allFieldDescriptors() {
		return this.indexer.getAllItems();
	}
	
	/**
	 * Adds a field to this class descriptor
	 * @param ifd Field to add
	 */
	public void addField(FieldDescriptor ifd)
	{
		this.indexer.Insert(ifd);
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
			return this.getJavaClass().isAssignableFrom(icd.getJavaClass());
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

	@Override
	public Object getInstance() {
		// TODO Auto-generated method stub
		try{
		return this.getJavaClass().newInstance();
		}
		catch(Exception e)
		{
			try{
			Constructor C = this.getJavaClass().getConstructor(null);
			C.setAccessible(true);
			return C.newInstance(null);
			}
			catch(Exception eek)
			{
				throw new RuntimeException(eek);
			}
			
		}
	}

	@Override
	public String getTagName() {
		// TODO Auto-generated method stub
		return this.tagName;
	}

	@Override
	public FieldDescriptorIndexer fields() {
		// TODO Auto-generated method stub
		return this.indexer;
	}

	boolean strictGraph = false;
	
	@Override
	public boolean getStrictObjectGraphRequired() {
		// TODO Auto-generated method stub
		return this.strictGraph;
	}
	
	public void setStrictObjectGraphRequired(boolean value)
	{
		this.strictGraph = value;
	}

	@Override
	public FieldDescriptor getScalarTextFD() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasScalarFD() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setTagName(String tagName) {
		// TODO Auto-generated method stub
		this.tagName = tagName;
	}

	@Override
	public FieldDescriptor getFieldDescriptorByTag(String fieldTag,
			ISimplTypesScope translationScope) {
		return this.fields().by("tag_name").get(fieldTag);
	}
	
	@Override
	public String toString()
	{
		return "ClassDescriptor["+this.getName()+"]"; 
	}
}
