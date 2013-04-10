package simpl.core;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.indexers.ClassDescriptorIndexer;
import simpl.descriptions.indexers.EnumerationDescriptorIndexer;
import simpl.deserialization.ISimplDeserializationHooks;
import simpl.exceptions.SIMPLDescriptionException;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;
import simpl.tools.XMLTools;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;
import ecologylab.appframework.types.AssetsState;
import ecologylab.appframework.types.prefs.MetaPrefSet;
import ecologylab.appframework.types.prefs.PrefSet;
import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.net.ParsedURL;

/**
 * A set of bindings between XML element names (tags) and associated simple (without package) class
 * names, and associated Java ElementState classes. Inheritance is supported.
 */
public final class SimplTypesScope extends Debug implements ISimplDeserializationHooks, ISimplTypesScope
{
	/*
	 * Cyclic graph handling fields, switches and maps
	 */
	public enum GRAPH_SWITCH
	{
		ON, OFF
	}

	public static GRAPH_SWITCH graphSwitch = GRAPH_SWITCH.ON;

	private static HashMap<String, ISimplTypesScope> allTypesScopes = new HashMap<String, ISimplTypesScope>();

	/**
	 * SimplTypesScope has some global static "AllTypesScopes" scopes which can
	 *  interfere with idempotent execution of tests.
	 * Run this method to reset the global scope before running test code to give you a clean slate.
	 */
	public static void ResetAllTypesScopes()
	{
		System.out.println("----------- RESETTING -----------");
		SimplTypesScope.allTypesScopes = new HashMap<String, ISimplTypesScope>();
	}
	
	static
	{
		TypeRegistry.init();
	}

	/**
	 * Find the SimplTypesScope called <code>name</code>, if there is one.
	 * 
	 * @param name
	 * @return
	 */
	public static ISimplTypesScope lookup(String name)
	{
		return allTypesScopes.get(name);
	}
	
	public static void registerSimplTypesScope(String name, ISimplTypesScope sts)
	{
		synchronized(allTypesScopes)
		{
			if(!allTypesScopes.containsKey(name))
			{
				// TODO: Concurrency? yo. 
				allTypesScopes.put(name, sts);
			}else{
				//throw new RuntimeException("OH NO EVERYTHING IS AMISS FOR: " + name);
			}
		}
	}
	
	/**
	 * This will switch on the graph serialization
	 */
	public static void enableGraphSerialization()
	{
		graphSwitch = GRAPH_SWITCH.ON;
	}

	/**
	 * This will switch off the graph serialization
	 */
	public static void disableGraphSerialization()
	{
		graphSwitch = GRAPH_SWITCH.OFF;
	}

	
	//// -----------------------------------------------------
	
	
	@simpl_scalar
	private String name;

	public void setName(String name)
	{
		this.name = name;
	}
	
	private List<SimplTypesScope> inheritedTypesScopes = new ArrayList<SimplTypesScope>();

	public ClassDescriptorIndexer classDescriptors;
	public EnumerationDescriptorIndexer enumerationDescriptors;
	
	/**
	 * Fundamentally, a SimplTypesScope consists of a set of class simple names. These are mapped to
	 * tag names (camel case conversion), and to Class objects. Because there are many packages,
	 * globally, there could be more than one class with one single name.
	 * <p/>
	 * Among other things, a SimplTypesScope tells us *which* package's version will be used, if
	 * there are multiple possibilities. This is the case when internal and external versions of a
	 * message and its constituents are defined for a messaging API.
	 */

	// TODO: Wrap this w/ the indexer to make it serialize right
	@simpl_nowrap
	@simpl_map("class_descriptor")
	private Scope<ClassDescriptor> entriesByTag = new Scope<ClassDescriptor>();

	/**
	 * Scope containing all enumerations by their tag name
	 */
	@simpl_nowrap
	@simpl_map("enumeration_descriptor")
	private Scope<EnumerationDescriptor> enumerationsByTag = new Scope<EnumerationDescriptor>();

	

	/**
	 * Default constructor only for use by simpl().
	 */
	public SimplTypesScope()
	{
		this.classDescriptors = new ClassDescriptorIndexer();
		this.enumerationDescriptors = new EnumerationDescriptorIndexer();
	}

	/**
	 * Add a translation table entry for an ElementState derived sub-class. Assumes that the xmlTag
	 * can be derived automatically from the className, by translating case-based separators to
	 * "_"-based separators.
	 * 
	 * @param classObj
	 *          The object for the class.
	 */
	public void addTranslation(Class<?> classObj)
	{
		// Enumerated types behave a bit differently.
		if(classObj.isEnum())
		{
			EnumerationDescriptor ed;
			try
			{
				// Add if it isnt there already.
				ed = EnumerationDescriptor.get(classObj);
			}
			catch(SIMPLDescriptionException sde)
			{
				// We need to wrap this to not upset the API, for now. 
				throw new RuntimeException(sde);
			}
				
			this.enumerationDescriptors.Insert(ed);
		}
		else
		{
			// Add a class!
			ClassDescriptor entry = ClassDescriptors.getClassDescriptor(classObj);
			this.classDescriptors.Insert(entry);
		}
	}
	
	/**
	 * Given a mock translation*, which is effectively a mock object with overridden or removed functionality
	 * and annotated with @simpl_inherit_parent_tag 
	 * (For example: A translation with the deserializationPostHook implemented as an empty method) 
	 * 
	 * This method will: 
	 * 
	 * 	1. Remove the translation class the "shallow translation" is meant to replace
	 *  2. Add the shallow translation to the type scope.
	 *  
	 *  Currently used for deserialization w/o dealing with file dependencies. 
	 *  Could also be used for testing, etc.
	 * @param classObj The Mock Translation to use as an override.
	 */
	public void overrideWithMockTranslation(Class<?> classObj)
	{
		removeTranslation(correspondingClassFor(classObj));
		addTranslation(classObj);
	}
	// * You could even call it a ... Doppelganger, if you wanted to. 
	

	private Class<?> correspondingClassFor(Class<?> dummyObj)
	{
		ClassDescriptor entry = ClassDescriptors.getClassDescriptor(dummyObj);
		String tagName = entry.getTagName();
		
		ClassDescriptor corresp = entriesByTag.get(tagName);
		
		return corresp != null? corresp.getJavaClass() : dummyObj;
	}
	
	/**
	 * Removes a given translation from the Type Scope
	 * @param classObj The translation to remove. 
	 */
	public void removeTranslation(Class<?> classObj)
	{
		ClassDescriptor entry = ClassDescriptors.getClassDescriptor(classObj);
		
		this.classDescriptors.Remove(ClassDescriptors.getClassDescriptor(classObj));
	}
	
	@Override
	public String toString()
	{
		return "SimplTypesScope[" + name + "]";
	}

	

	/**
	 * Get the Scalar Type corresponding to the Class.
	 * 
	 * @param thatClass
	 * @return Type associated with thatClass
	 */
	ScalarType getType(Class<?> thatClass)
	{
		// todo fix ;)
		return null; 

	}

	/**
	 * 
	 * @return The unique name of this.
	 */
	public String getName()
	{
		return name;
	}

	
	
	/*
	private static void augmentSimplTypesScope(Class<?> thatClass,
			HashMap<String, Class<?>> augmentedClasses)
	{
		if (augmentedClasses.put(thatClass.getSimpleName(), thatClass) != null)
			return;

		if (thatClass.getSuperclass() != ElementState.class)
		{
			augmentSimplTypesScope(thatClass.getSuperclass().asSubclass(ElementState.class),
					augmentedClasses);
		}

		ClassDescriptor thatClassDescriptor = ClassDescriptors
				.getClassDescriptor(thatClass);

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = thatClassDescriptor
				.getFieldDescriptorsByFieldName();

		if (fieldDescriptors.size() > 0)
		{
			thatClassDescriptor.resolvePolymorphicAnnotations();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.isNested())
				{
					augmentSimplTypesScope(fieldDescriptor.getType().asSubclass(ElementState.class),
							augmentedClasses);
				}
				else
				{
					if (fieldDescriptor.isCollection() && !fieldDescriptor.isPolymorphic())
					{
						ArrayList<Class<?>> genericClasses = XMLTools.getGenericParameters(fieldDescriptor
								.getField());

						for (Class<?> genericClass : genericClasses)
						{
							if (genericClass != null && ElementState.class.isAssignableFrom(genericClass))
							{
								augmentSimplTypesScope(genericClass.asSubclass(ElementState.class),
										augmentedClasses);
							}
						}
					}
					else if (fieldDescriptor.isPolymorphic())
					{
						Collection<ClassDescriptor> polymorphDescriptors = fieldDescriptor
								.getPolymorphicClassDescriptors();

						if (polymorphDescriptors != null)
						{
							for (ClassDescriptor classDescriptor : polymorphDescriptors)
							{
								augmentSimplTypesScope(classDescriptor.getDescribedClass(), augmentedClasses);
							}
						}
					}
				}
			}
		}
	}
	*/

	/**
	 * Method returning all the class descriptors corresponds to all the translation Scopes
	 * 
	 * @return
	 */
	public Collection<ClassDescriptor> getAllClassDescriptors()
	{
		return this.classDescriptors.getAllItems();
	}

	
	/**
	 * Rebuild structures after serializing only some fields.
	 */
	@Override
	public void deserializationPostHook(TranslationContext translationContext, Object object)
	{
		for (ClassDescriptor classDescriptor : entriesByTag.values())
		{
			this.classDescriptors.Insert(classDescriptor);
		}
		
		if (allTypesScopes.containsKey(name))
			warning("REPLACING another SimplTypesScope of the SAME NAME during deserialization!\t"
					+ name);
		
		SimplTypesScope.registerSimplTypesScope(this.getName(), this);
	}

	@Override
	public void deserializationInHook(TranslationContext translationContext) {}

	@Override
	public void deserializationPreHook(TranslationContext translationContext) {}

	public void inheritFrom(SimplTypesScope sts) {

		this.classDescriptors.mergeIn(sts.classDescriptors);
		this.enumerationDescriptors.mergeIn(sts.enumerationDescriptors);
		this.inheritedTypesScopes.add(sts);
	}

	@Override
	public ClassDescriptor getClassDescriptorByTag(String tag) {
		return this.classDescriptors.by("tagname").get(tag);
	}

	@Override
	public EnumerationDescriptor getEnumerationDescriptorByTag(String tag) {
	
		return this.enumerationDescriptors.by.TagName.get(tag);
	}
	
	@Override
	public boolean containsDescriptorForTag(String tag)
	{
		return(this.getClassDescriptorByTag(tag) != null ||
				this.getEnumerationDescriptorByTag(tag) != null);
	}

	public static ISimplTypesScope get(String scopeName) {
		// TODO Auto-generated method stub
		return allTypesScopes.get(scopeName);
	}

	public static void serialize(PrefSet prfs, PrintStream out, StringFormat xml) {
		// TODO Auto-generated method stub
		
	}

	public Object deserialize(File file, Format xml) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object deserialize(ParsedURL purl, Format xml) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object deserialize(String prefXML, StringFormat xml) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void serialize(Object assetsState, File assetsXmlFile,
			Format xml) {
		// TODO Auto-generated method stub
		
	}

	public static StringBuilder serialize(Object si, StringFormat format) {
		// TODO Auto-generated method stub
		return null;
	}

	public Class<?> getClassByTag(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EnumerationDescriptor> getAllEnumerationDescriptors() {
		// TODO Auto-generated method stub
		return this.enumerationDescriptors.getAllItems();
	}

	@Override
	public void inheritFrom(ISimplTypesScope sts) {

		// Insert all values from STS into here. :) 
		// Should probably leverage mergeInto; will refactor late.r 
		for(ClassDescriptor cd :sts.getAllClassDescriptors())
		{
			this.classDescriptors.Insert(cd);
		}
		
		for(EnumerationDescriptor ed: sts.getAllEnumerationDescriptors())
		{
			this.enumerationDescriptors.Insert(ed);
		}
		
		
		assert(true);

	}
}
