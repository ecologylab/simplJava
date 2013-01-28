package simpl.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import simpl.annotations.dbal.simpl_map;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.NameSpaceDecl;
import simpl.descriptions.indexers.ClassDescriptorIndexer;
import simpl.descriptions.indexers.EnumerationDescriptorIndexer;
import simpl.deserialization.ISimplDeserializationHooks;
import simpl.deserialization.PullDeserializer;
import simpl.deserialization.binaryformats.BinaryPullDeserializer;
import simpl.deserialization.stringformats.StringPullDeserializer;
import simpl.exceptions.SIMPLDescriptionException;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.BinaryFormat;
import simpl.formats.enums.Format;
import simpl.formats.enums.StringFormat;
import simpl.serialization.FormatSerializer;
import simpl.serialization.stringformats.StringSerializer;
import simpl.tools.XMLTools;
import simpl.types.ScalarType;
import simpl.types.TypeRegistry;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.generic.HashMapArrayList;
import ecologylab.net.ParsedURL;

/**
 * A set of bindings between XML element names (tags) and associated simple (without package) class
 * names, and associated Java ElementState classes. Inheritance is supported.
 */
public final class SimplTypesScope extends Debug implements ISimplDeserializationHooks
{
	/*
	 * Cyclic graph handling fields, switches and maps
	 */
	public enum GRAPH_SWITCH
	{
		ON, OFF
	}

	public static GRAPH_SWITCH graphSwitch = GRAPH_SWITCH.OFF;

	@simpl_scalar
	private String name;

	public void setName(String name)
	{
		this.name = name;
	}
	
	private SimplTypesScope[] inheritedTypesScopes;

	private ClassDescriptorIndexer classDescriptorIndexer;
	private EnumerationDescriptorIndexer enumerationDescriptorIndexer;
	
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
	private Scope<ClassDescriptor<? extends FieldDescriptor>> entriesByTag = new Scope<ClassDescriptor<? extends FieldDescriptor>>();

	
	/**
	 * Scope containing all enumerations by their cross platform name
	 */
	private Scope<EnumerationDescriptor> enumerationsBySimpleName = new Scope<EnumerationDescriptor>();
	
	/**
	 * Scope containing all enumerations by their tag name
	 */
	@simpl_nowrap
	@simpl_map("enumeration_descriptor")
	private Scope<EnumerationDescriptor> enumerationsByTag = new Scope<EnumerationDescriptor>();

	
	
	private static HashMap<String, SimplTypesScope> allTypesScopes = new HashMap<String, SimplTypesScope>();

	/**
	 * SimplTypesScope has some global static "AllTypesScopes" scopes which can
	 *  interfere with idempotent execution of tests.
	 * Run this method to reset the global scope before running test code to give you a clean slate.
	 */
	public static void ResetAllTypesScopes()
	{
		System.out.println("----------- RESETTING -----------");
		SimplTypesScope.allTypesScopes = new HashMap<String, SimplTypesScope>();
	}
	
	public static final String STATE = "State";

	private boolean performFilters;

	static
	{
		TypeRegistry.init();
	}

	/**
	 * Default constructor only for use by simpl().
	 */
	public SimplTypesScope()
	{
	}

	/**
	 * Map XML Namespace ElementState subclasses to URIs.
	 * 
	 * @param nameSpaceDecls
	 */
	private void addNameSpaceDecls(NameSpaceDecl[] nameSpaceDecls)
	{
		if (nameSpaceDecls != null)
			for (NameSpaceDecl nsd : nameSpaceDecls)
			{
				registerNameSpaceDecl(nsd);
			}
	}

	/**
	 * Enter a NameSpaceDecl into nameSpaceClassesByURN.
	 * 
	 * @param nsd
	 */
	private void registerNameSpaceDecl(NameSpaceDecl nsd)
	{
		//nameSpaceClassesByURN.put(nsd.urn, nsd.esClass);
	}

	/**
	 * Add translations, where each translation is defined by an actual Class object. We can get both
	 * the class name and the package name from the Class object.
	 * 
	 * @param classes
	 */
	private void addTranslations(Class<?>[]... arrayOfClasses)
	{
		if (arrayOfClasses != null)
		{
			int numClasses = arrayOfClasses.length;

			for (int i = 0; i < numClasses; i++)
			{
				if (arrayOfClasses[i] != null)
				{
					for (Class<?> thatClass : arrayOfClasses[i])
					{
						addTranslation(thatClass);
					}
				}
			}
		}

		allTypesScopes.put(name, this);
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
			if(!this.enumerationsBySimpleName.containsKey(classSimpleName(classObj)))
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
				
				this.enumerationsByTag.put(ed.getTagName(), ed);
				this.enumerationsBySimpleName.put(ed.getName(), ed);
		
			}
		}
		else
		{
			// Add a class!
			ClassDescriptor entry = ClassDescriptor.getClassDescriptor(classObj);
			this.classDescriptorIndexer.Insert(entry);
		}
	}
	
	/**
	 * Given a shallow translation*, which is effectively a mock object with overridden or removed functionality
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
	 * @param classObj The Shallow Translation to use as an override.
	 */
	public void overrideWithShallowTranslation(Class<?> classObj)
	{
		removeTranslation(correspondingClassFor(classObj));
		addTranslation(classObj);
	}
	// * You could even call it a ... Doppelganger, if you wanted to. 
	

	private Class<?> correspondingClassFor(Class<?> dummyObj)
	{
		ClassDescriptor<?> entry = ClassDescriptor.getClassDescriptor(dummyObj);
		String tagName = entry.getTagName();
		
		ClassDescriptor<?> corresp = entriesByTag.get(tagName);
		
		return corresp != null? corresp.getDescribedClass() : dummyObj;
	}
	
	/**
	 * Removes a given translation from the Type Scope
	 * @param classObj The translation to remove. 
	 */
	public void removeTranslation(Class<?> classObj)
	{
		ClassDescriptor<?> entry = ClassDescriptor.getClassDescriptor(classObj);
		
		for (SimplTypesScope simplTypesScope : allTypesScopes.values())
		{
			simplTypesScope.removeTranslation(entry, classObj.getName());
		}
		
		this.removeTranslation(entry, classObj.getName());
	}
	
	private void removeTranslation(ClassDescriptor<?> entry, String className)
	{
		this.classDescriptorIndexer.Remove(entry);
	}
	

	/**
	 * Add a translation table entry for an ElementState derived sub-class. Assumes that the xmlTag
	 * can be derived automatically from the className, by translating case-based separators to
	 * "_"-based separators.
	 * 
	 * @param classObj
	 *          The object for the class.
	 */
	public void addTranslation(ClassDescriptor<?> classObj)
	{		
		this.classDescriptorIndexer.Insert(classObj);
	}


	/**
	 * Derive the XML tag from the Class object, using camel case conversion, or the @simpl_tag
	 * annotation that may be present in a class declaration.
	 * 
	 * @param thatClass
	 * @return
	 */
	private static String determineXMLTag(Class<?> thatClass)
	{
		Annotation[] annotations = thatClass.getDeclaredAnnotations();
		for (Annotation annotation : annotations)
		{
			if (annotation.annotationType().equals(simpl_tag.class))
			{
				return simpl_tag.class.cast(annotation).value();
			}
		}
		return XMLTools.getXmlTagName(thatClass.getSimpleName(), "State");
	}

	private String	toStringCache;

	@Override
	public String toString()
	{
		if (toStringCache == null)
		{
			toStringCache = "SimplTypesScope[" + name + "]";
		}
		return toStringCache;
	}

	/**
	 * Find the SimplTypesScope called <code>name</code>, if there is one.
	 * 
	 * @param name
	 * @return
	 */
	public static SimplTypesScope lookup(String name)
	{
		return allTypesScopes.get(name);
	}

	/**
	 * Get the Scalar Type corresponding to the Class.
	 * 
	 * @param thatClass
	 * @return Type associated with thatClass
	 */
	<U> ScalarType<U> getType(Class<U> thatClass)
	{
		return TypeRegistry.getScalarType(thatClass);
	}

	/**
	 * 
	 * @return The unique name of this.
	 */
	public String getName()
	{
		return name;
	}

	public static final String	BASIC_TRANSLATIONS	= "basic_translations";

	public static void registerSimplTypesScope(String name, SimplTypesScope sts)
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

	
	public Object deserialize(File file, TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		PullDeserializer pullDeserializer = PullDeserializer.getDeserializer(this, translationContext,
				deserializationHookStrategy, format);
		return pullDeserializer.parse(file);
	}

	public Object deserialize(File file, TranslationContext translationContext, Format format)
			throws SIMPLTranslationException
	{
		return deserialize(file, translationContext, null, format);
	}

	public Object deserialize(File file, Format format) throws SIMPLTranslationException
	{
		TranslationContext translationContext= new TranslationContext();
		Object obj = deserialize(file, translationContext, null, format);
		return obj;
	}

	public Object deserialize(File file, DeserializationHookStrategy deserializationHookStrategy,
			Format format) throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(file, translationContext, deserializationHookStrategy, format);
		return obj;
	}

	public Object deserialize(ParsedURL parsedURL,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(parsedURL, translationContext, deserializationHookStrategy, format);
		return obj;
	}

	public Object deserialize(ParsedURL parsedURL, TranslationContext translationContext,
			Format format) throws SIMPLTranslationException
	{
		return deserialize(parsedURL, translationContext, null, format);
	}

	public Object deserialize(ParsedURL parsedURL, Format format) throws SIMPLTranslationException
	{
		TranslationContext translationContext =  new TranslationContext();
		Object obj = deserialize(parsedURL, translationContext, null, format);
		return obj;
	}

	public Object deserialize(ParsedURL parsedURL, TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		PullDeserializer pullDeserializer = PullDeserializer.getDeserializer(this, translationContext,
				deserializationHookStrategy, format);
		return pullDeserializer.parse(parsedURL);
	}

	public Object deserialize(InputStream inputStream,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(inputStream, translationContext, deserializationHookStrategy, format,
				null);
		return obj;
	}

	public Object deserialize(InputStream inputStream,
			DeserializationHookStrategy deserializationHookStrategy, Format format, Charset charSet)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(inputStream, translationContext, deserializationHookStrategy, format,
				charSet);
		return obj;
	}

	public Object deserialize(InputStream inputStream, TranslationContext translationContext,
			Format format) throws SIMPLTranslationException
	{
		return deserialize(inputStream, translationContext, null, format, null);
	}

	public Object deserialize(InputStream inputStream, TranslationContext translationContext,
			Format format, Charset charSet) throws SIMPLTranslationException
	{
		return deserialize(inputStream, translationContext, null, format, charSet);
	}

	public Object deserialize(InputStream inputStream, Format format)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(inputStream, translationContext, null, format, null);
		return obj;
	}

	public Object deserialize(InputStream inputStream, Format format, Charset charSet)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(inputStream, translationContext, null, format, charSet);
		return obj;
	}

public Object deserialize(InputStream inputStream, TranslationContext translationContext,
		DeserializationHookStrategy deserializationHookStrategy, Format format, Charset charSet)
		throws SIMPLTranslationException
{
	PullDeserializer pullDeserializer = PullDeserializer.getDeserializer(this, translationContext,
			deserializationHookStrategy, format);
	if(charSet != null)
	{
		return pullDeserializer.parse(inputStream, charSet);
	}
	else
	{
		return pullDeserializer.parse(inputStream);
	}
}

	public Object deserialize(URL url, Format format) throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(new ParsedURL(url), translationContext, null, format);
		return obj;
	}

	public Object deserialize(CharSequence charSequence, TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		StringPullDeserializer pullDeserializer = PullDeserializer.getStringDeserializer(this,
				translationContext, deserializationHookStrategy, stringFormat);
		return pullDeserializer.parse(charSequence);
	}

	public Object deserialize(CharSequence charSequence,
			DeserializationHookStrategy deserializationHookStrategy, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		StringPullDeserializer pullDeserializer = PullDeserializer.getStringDeserializer(this,
				translationContext, deserializationHookStrategy, stringFormat);
		return pullDeserializer.parse(charSequence);
	}

	public Object deserialize(CharSequence charSequence, TranslationContext translationContext,
			StringFormat stringFormat) throws SIMPLTranslationException
	{
		return deserialize(charSequence, translationContext, null, stringFormat);
	}

	public Object deserialize(CharSequence charSequence, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		Object obj = deserialize(charSequence, translationContext, null, stringFormat);
		return obj;
	}

	public Object deserialize(byte[] byteArray, TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, BinaryFormat binaryFormat)
			throws SIMPLTranslationException
	{
		BinaryPullDeserializer binaryPullDeserializer = PullDeserializer.getBinaryDeserializer(this,
				translationContext, deserializationHookStrategy, binaryFormat);
		return binaryPullDeserializer.parse(byteArray);
	}

	private static HashMap<String, Class<?>> augmentTranslationScope(ArrayList<Class<?>> allClasses)
	{
		HashMap<String, Class<?>> augmentedClasses = new HashMap<String, Class<?>>();
		for (Class<?> thatClass : allClasses)
		{
			augmentSimplTypesScope(thatClass, augmentedClasses);
		}
		return augmentedClasses;
	}

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

		ClassDescriptor<? extends FieldDescriptor> thatClassDescriptor = ClassDescriptor
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
					augmentSimplTypesScope(fieldDescriptor.getFieldType().asSubclass(ElementState.class),
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
							for (ClassDescriptor<? extends FieldDescriptor> classDescriptor : polymorphDescriptors)
							{
								augmentSimplTypesScope(classDescriptor.getDescribedClass(), augmentedClasses);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * @return the performFilters
	 */
	public boolean isPerformFilters()
	{
		return performFilters;
	}

	/**
	 * @param performFilters
	 *          the performFilters to set
	 */
	public void setPerformFilters(boolean performFilters)
	{
		this.performFilters = performFilters;
	}



	/**
	 * Method returning all the class descriptors corresponds to all the translation Scopes
	 * 
	 * @return
	 */
	public ArrayList<ClassDescriptor<? extends FieldDescriptor>> getAllClassDescriptors()
	{
		ArrayList<ClassDescriptor<? extends FieldDescriptor>> classes = new ArrayList<ClassDescriptor<? extends FieldDescriptor>>();

		for (SimplTypesScope simplTypesScope : allTypesScopes.values())
		{
			for (ClassDescriptor<? extends FieldDescriptor> classDescriptor : simplTypesScope.entriesByTag
					.values())
			{
				classes.add(classDescriptor);
			}
		}
		return classes;
	}

	
	/**
	 * This will switch on the graph serialization
	 */
	public static void enableGraphSerialization()
	{
		graphSwitch = GRAPH_SWITCH.ON;
	}

	/**
	 * This will switch on the graph serialization
	 */
	public static void disableGraphSerialization()
	{
		graphSwitch = GRAPH_SWITCH.OFF;
	}

	/**
	 * Rebuild structures after serializing only some fields.
	 */
	@Override
	public void deserializationPostHook(TranslationContext translationContext, Object object)
	{
		for (ClassDescriptor classDescriptor : entriesByTag.values())
		{
			this.classDescriptorIndexer.Insert(classDescriptor);
		}
		
		if (allTypesScopes.containsKey(name))
			warning("REPLACING another SimplTypesScope of the SAME NAME during deserialization!\t"
					+ name);
		
		SimplTypesScope.registerSimplTypesScope(this.getName(), this);
	}

	/**
	 * 
	 * @param object
	 * @param outputStream
	 * @param bibtex
	 * @throws SIMPLTranslationException 
	 */
	public static void serialize(Object object, OutputStream outputStream, Format format) throws SIMPLTranslationException
	{
		FormatSerializer serializer = FormatSerializer.getSerializer(format);
		serializer.serialize(object, outputStream);
	}
	
	/**
	 * 
	 * @param object
	 * @param outputStream
	 * @param format
	 * @param translationContext
	 * @throws SIMPLTranslationException 
	 */
	public static void serialize(Object object, OutputStream outputStream, Format format, TranslationContext translationContext) throws SIMPLTranslationException
	{
		FormatSerializer serializer = FormatSerializer.getSerializer(format);
		serializer.serialize(object, outputStream, translationContext);
	}

	/**
	 * Static method for serializing an object. accepts translation context which a user can supply to
	 * pass in additional information for the serialization method to use
	 * 
	 * @param object
	 * @param stringBuilder
	 * @param format
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static StringBuilder serialize(Object object, StringFormat stringFormat,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		StringSerializer stringSerializer = FormatSerializer.getStringSerializer(stringFormat);
		return stringSerializer.serialize(object, translationContext);
	}

	/**
	 * Static method for serializing an object. accepts translation context which a user can supply to
	 * pass in additional information for the serialization method to use
	 * 
	 * @param object
	 * @param stringBuilder
	 * @param format
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static void serialize(Object object, StringBuilder stringBuilder,
			StringFormat stringFormat, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		StringSerializer stringSerializer = FormatSerializer.getStringSerializer(stringFormat);
		stringSerializer.serialize(object, stringBuilder, translationContext);
	}

	/**
	 * Static method for serializing an object. accepts translation context which a user can supply to
	 * pass in additional information for the serialization method to use
	 * 
	 * @param object
	 * @param appendable
	 * @param format
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static void serialize(Object object, Appendable appendable, StringFormat stringFormat,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		StringSerializer stringSerializer = FormatSerializer.getStringSerializer(stringFormat);
		stringSerializer.serialize(object, appendable, translationContext);
	}

	public static void serializeOut(Object object, String message, StringFormat stringFormat)
	{
		System.out.print(message);
		System.out.print(':');
		try
		{
			serialize(object, System.out, stringFormat);
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Static method for serializing an object to the defined format. TranslationContext is
	 * automatically initialized to handle graphs if enabled
	 * 
	 * @param object
	 * @param appendable
	 * @param format
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static void serialize(Object object, Appendable appendable, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		serialize(object, appendable, stringFormat, translationContext);
	}

	/**
	 * Static method for serializing an object to the defined format. TranslationContext is
	 * automatically initialized to handle graphs if enabled
	 * 
	 * @param object
	 * @param stringBuilder
	 * @param stringFormat
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static StringBuilder serialize(Object object, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		StringBuilder sb = serialize(object, stringFormat, translationContext);
		return sb;
	}

	/**
	 * Static method for serializing an object to the defined format. TranslationContext is
	 * automatically initialized to handle graphs if enabled
	 * 
	 * @param object
	 * @param stringBuilder
	 * @param stringFormat
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static void serialize(Object object, StringBuilder stringBuilder, StringFormat stringFormat)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		serialize(object, stringBuilder, stringFormat, translationContext);
	}

	/**
	 * Static method for serializing an object. accepts translation context which a user can supply to
	 * pass in additional information for the serialization method to use
	 * 
	 * @param object
	 * @param appendable
	 * @param format
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static void serialize(Object object, File file, Format format,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		FormatSerializer formatSerializer = FormatSerializer.getSerializer(format);
		formatSerializer.serialize(object, file, translationContext);
	}

	/**
	 * Static method for serializing an object to the defined format. TranslationContext is
	 * automatically initialized to handle graphs if enabled
	 * 
	 * @param object
	 * @param stringBuilder
	 * @param stringFormat
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public static void serialize(Object object, File file, Format format)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = new TranslationContext();
		serialize(object, file, format, translationContext);
	}

	@Override
	public void deserializationInHook(TranslationContext translationContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deserializationPreHook(TranslationContext translationContext) {
		// TODO Auto-generated method stub
		
	}

	public void inheritFrom(SimplTypesScope sts) {
		// TODO Auto-generated method stub
		// todo: Merge into the indexer. :3 
		
	}
}
