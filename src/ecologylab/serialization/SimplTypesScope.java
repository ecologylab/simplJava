package ecologylab.serialization;

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

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.annotations.simpl_map;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;
import ecologylab.serialization.deserializers.pullhandlers.PullDeserializer;
import ecologylab.serialization.deserializers.pullhandlers.binaryformats.BinaryPullDeserializer;
import ecologylab.serialization.deserializers.pullhandlers.stringformats.StringPullDeserializer;
import ecologylab.serialization.formatenums.BinaryFormat;
import ecologylab.serialization.formatenums.Format;
import ecologylab.serialization.formatenums.StringFormat;
import ecologylab.serialization.serializers.FormatSerializer;
import ecologylab.serialization.serializers.stringformats.StringSerializer;
import ecologylab.serialization.types.ScalarType;
import ecologylab.serialization.types.TypeRegistry;

/**
 * A set of bindings between XML element names (tags) and associated simple (without package) class
 * names, and associated Java ElementState classes. Inheritance is supported.
 */
public final class SimplTypesScope extends ElementState
{
	/*
	 * Cyclic graph handling fields, switches and maps
	 */
	public enum GRAPH_SWITCH
	{
		ON, OFF
	}

	public static GRAPH_SWITCH																						graphSwitch								= GRAPH_SWITCH.OFF;

	private static final int																							GUESS_CLASSES_PER_TSCOPE	= 5;

	@simpl_scalar
	private/* final */String																							name;

	private SimplTypesScope[]																							inheritedTypesScopes;

	/**
	 * Fundamentally, a SimplTypesScope consists of a set of class simple names. These are mapped to
	 * tag names (camel case conversion), and to Class objects. Because there are many packages,
	 * globally, there could be more than one class with one single name.
	 * <p/>
	 * Among other things, a SimplTypesScope tells us *which* package's version will be used, if
	 * there are multiple possibilities. This is the case when internal and external versions of a
	 * message and its constituents are defined for a messaging API.
	 */
	private Scope<ClassDescriptor<? extends FieldDescriptor>>							entriesByClassSimpleName	= new Scope<ClassDescriptor<? extends FieldDescriptor>>();

	private Scope<ClassDescriptor<? extends FieldDescriptor>>							entriesByClassName				= new Scope<ClassDescriptor<? extends FieldDescriptor>>();

	@simpl_nowrap
	@simpl_map("class_descriptor")
	private Scope<ClassDescriptor<? extends FieldDescriptor>>							entriesByTag							= new Scope<ClassDescriptor<? extends FieldDescriptor>>();

	private HashMap<Integer, ClassDescriptor<? extends FieldDescriptor>>	entriesByTLVId						= new HashMap<Integer, ClassDescriptor<? extends FieldDescriptor>>();

	private Scope<ClassDescriptor<? extends FieldDescriptor>>							entriesByBibTeXType				= new Scope<ClassDescriptor<? extends FieldDescriptor>>();

	private final Scope<Class<?>>																					nameSpaceClassesByURN			= new Scope<Class<?>>();

	private static HashMap<String, SimplTypesScope>												allTypesScopes						= new HashMap<String, SimplTypesScope>();

	public static final String																						STATE											= "State";

	private boolean																												performFilters;

	static
	{
		TypeRegistry.init();
	}

	/**
	 * Default constructor only for use by translateFromXML().
	 */
	public SimplTypesScope()
	{

	}

	/**
	 * Building block called by other constructors for most basic name registration functionality.
	 * 
	 * @param name
	 */
	private SimplTypesScope(String name)
	{
		this.name = name;
	}

	/**
	 * Create a new SimplTypesScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by copying in the translations from another, pre-existing
	 * "base" SimplTypesScope.
	 * 
	 * @param name
	 * @param inheritedSimplTypesScope
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedSimplTypesScope)
	{
		this(name);
		addTranslations(inheritedSimplTypesScope);
		SimplTypesScope[] inheritedSimplTypesScopes = new SimplTypesScope[1];
		inheritedSimplTypesScopes[0] = inheritedSimplTypesScope;
		this.inheritedTypesScopes = inheritedSimplTypesScopes;
	}

	private SimplTypesScope(String name, SimplTypesScope inheritedSimplTypesScope,
			Class<?> translation)
	{
		this(name, inheritedSimplTypesScope);
		addTranslation(translation);
		addSimplTypesScope(name);
	}

	/**
	 * Create a new SimplTypesScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by creating the inherited SimplTypesScope ad then adding the
	 * new ClassDescriptor intothat
	 * 
	 * @param name
	 * @param inheritedSimplTypesScope
	 * @param translation
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedSimplTypesScope,
			ClassDescriptor translation)
	{
		this(name, inheritedSimplTypesScope);
		addTranslation(translation);
		addSimplTypesScope(name);
	}

	/**
	 * Create a new SimplTypesScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by copying in the translations from another, pre-existing
	 * "base" SimplTypesScope.
	 * 
	 * @param name
	 * @param baseTranslationSet
	 */
	private SimplTypesScope(String name, SimplTypesScope... inheritedSimplTypesScopes)
	{
		this(name);

		if (inheritedSimplTypesScopes != null)
		{
			this.inheritedTypesScopes = inheritedSimplTypesScopes;
			int n = inheritedSimplTypesScopes.length;
			for (int i = 0; i < n; i++)
				addTranslations(inheritedSimplTypesScopes[i]);
		}
	}

	/**
	 * Create a new SimplTypesScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by copying in the translations from another, pre-existing
	 * "base" SimplTypesScope.
	 * 
	 * @param name
	 * @param baseTranslationSet
	 */
	private SimplTypesScope(String name, Collection<SimplTypesScope> baseTranslationsSet)
	{
		this(name);
		for (SimplTypesScope thatSimplTypesScope : baseTranslationsSet)
			addTranslations(thatSimplTypesScope);
		inheritedTypesScopes = (SimplTypesScope[]) baseTranslationsSet.toArray();
	}

	/**
	 * Create a new SimplTypesScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState.
	 * 
	 * Set a new default package, and a set of defined translations.
	 * 
	 * @param name
	 *          Name of the TranslationSpace to be A key for use in the TranslationSpace registry.
	 * @param translations
	 *          Set of initially defined translations for this.
	 * @param defaultPackgeName
	 */
	private SimplTypesScope(String name, Class<?>... translations)
	{
		this(name, (SimplTypesScope[]) null, translations);
		addSimplTypesScope(name);
	}

	/**
	 * Create a new SimplTypesScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState.
	 * 
	 * Set a new default package, and a set of defined translations.
	 * 
	 * @param name
	 *          Name of the TranslationSpace to be A key for use in the TranslationSpace registry.
	 * @param translation
	 *          Set of initially defined translations for this.
	 */
	private SimplTypesScope(String name, ClassDescriptor... translation)
	{
		this(name, (SimplTypesScope[]) null, translation);
		addSimplTypesScope(name);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedSimplTypesScopes
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope[] inheritedSimplTypesScopes,
			Class<?>[]... translations)
	{
		this(name, inheritedSimplTypesScopes);
		addTranslations(translations);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedSimplTypesScopes
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope[] inheritedSimplTypesScopes,
			ClassDescriptor[]... translations)
	{
		this(name, inheritedSimplTypesScopes);
		addTranslations(translations);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param translations
	 * @param baseTranslations
	 */
	private SimplTypesScope(String name, Collection<SimplTypesScope> inheritedTranslationsSet,
			Class<?>[] translations)
	{
		this(name, inheritedTranslationsSet);
		addTranslations(translations);

		addSimplTypesScope(name);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedTranslationsSet
	 * @param translations
	 */
	private SimplTypesScope(String name, Collection<SimplTypesScope> inheritedTranslationsSet,
			ClassDescriptor[] translations)
	{
		this(name, inheritedTranslationsSet);
		addTranslations(translations);

		addSimplTypesScope(name);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedSimplTypesScope
	 * @param translations	A set of arrays of classes.
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedSimplTypesScope,
			Class<?>[]... translations)
	{
		this(name, inheritedSimplTypesScope);
		addTranslations(translations);

		addSimplTypesScope(name);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedSimplTypesScope
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedSimplTypesScope,
			ClassDescriptor[]... translations)
	{
		this(name, inheritedSimplTypesScope);
		addTranslations(translations);

		addSimplTypesScope(name);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available. Map XML Namespace
	 * declarations.
	 * 
	 * @param name
	 * @param nameSpaceDecls
	 * @param inheritedSimplTypesScopes
	 * @param translations
	 * @param defaultPackgeName
	 */
	private SimplTypesScope(String name, NameSpaceDecl[] nameSpaceDecls,
			SimplTypesScope[] inheritedSimplTypesScopes, Class<?>[] translations)
	{
		this(name, inheritedSimplTypesScopes, translations);
		addNameSpaceDecls(nameSpaceDecls);

		addSimplTypesScope(name);
	}

	/**
	 * Construct a new SimplTypesScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available. Map XML Namespace
	 * declarations.
	 * 
	 * @param name
	 * @param nameSpaceDecls
	 * @param inheritedSimplTypesScopes
	 * @param translations
	 */
	private SimplTypesScope(String name, NameSpaceDecl[] nameSpaceDecls,
			SimplTypesScope[] inheritedSimplTypesScopes, ClassDescriptor[] translations)
	{
		this(name, inheritedSimplTypesScopes, translations);
		addNameSpaceDecls(nameSpaceDecls);

		addSimplTypesScope(name);
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
		nameSpaceClassesByURN.put(nsd.urn, nsd.esClass);
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
	 * Add translations, where each translation is defined by an actual Class object. We can get both
	 * the class name and the package name from the Class object.
	 * 
	 * @param arrayOfClasses
	 */
	private void addTranslations(ClassDescriptor[]... arrayOfClasses)
	{
		if (arrayOfClasses != null)
		{
			int numClasses = arrayOfClasses.length;

			for (int i = 0; i < numClasses; i++)
			{
				if (arrayOfClasses[i] != null)
				{
					for (ClassDescriptor thatClass : arrayOfClasses[i])
					{
						addTranslation(thatClass);
					}
				}
			}
		}

		allTypesScopes.put(name, this);
	}

	/**
	 * Utility for composing <code>SimplTypesScope</code>s. Performs composition by value. That is,
	 * the entries are copied.
	 * 
	 * Unlike in union(), if there are duplicates, they will override identical entries in this.
	 * 
	 * @param inheritedTypesScope
	 */
	private void addTranslations(SimplTypesScope inheritedTypesScope)
	{
		if (inheritedTypesScope != null)
		{
			// copy map entries from inherited maps into new maps
			updateMapWithValues(inheritedTypesScope.entriesByClassSimpleName,
					entriesByClassSimpleName, "classSimpleName");
			updateMapWithValues(inheritedTypesScope.entriesByClassName, entriesByClassName,
					"className");
			updateMapWithValues(inheritedTypesScope.entriesByTag, entriesByTag, "tagName");

			HashMap<String, Class<?>> inheritedNameSpaceClassesByURN = inheritedTypesScope.nameSpaceClassesByURN;
			if (inheritedNameSpaceClassesByURN != null)
			{
				for (String urn : inheritedNameSpaceClassesByURN.keySet())
				{
					nameSpaceClassesByURN.put(urn, inheritedNameSpaceClassesByURN.get(urn));
				}
			}
		}
	}

	/**
	 * Update the Map with all the entries in the inherited Map.
	 * 
	 * @param inheritedMap
	 * @param warn
	 */
	private void updateMapWithValues(
			Map<String, ClassDescriptor<? extends FieldDescriptor>> inheritedMap,
			Map<String, ClassDescriptor<? extends FieldDescriptor>> newMap, String warn)
	{
		// XXX ANDRUID + ZACH -> concurrent modification exception can occur here (for loop) if
		// inheritedMap is modified elsewhere
		for (String key : inheritedMap.keySet())
		{
			ClassDescriptor<? extends FieldDescriptor> translationEntry = inheritedMap.get(key);
			updateMapWithEntry(newMap, key, translationEntry, warn);
		}
	}

	/**
	 * Update the Map with the entry.
	 * 
	 * @param newMap
	 * @param key
	 * @param translationEntry
	 *          Must be non-null.
	 * @param warn
	 */
	private void updateMapWithEntry(Map<String, ClassDescriptor<? extends FieldDescriptor>> newMap,
			String key, ClassDescriptor<? extends FieldDescriptor> translationEntry, String warn)
	{
		ClassDescriptor<? extends FieldDescriptor> existingEntry = newMap.get(key);

		// final boolean entryExists = existingEntry != null;
		// final boolean newEntry = existingEntry != translationEntry;

		final boolean entryExists = existingEntry != null;
		final boolean newEntry = !entryExists ? true
				: existingEntry.getDescribedClass() != translationEntry.getDescribedClass();

		if (newEntry)
		{
			if (entryExists) // look out for redundant entries
				warning("Overriding " + warn + " " + key + " with " + translationEntry);

			newMap.put(key, translationEntry);
		}
		// if (entryExists && newEntry) // look out for redundant entries
		// warning("Overriding " + warn + " " + key + " with " + translationEntry);
		//
		// if (/** !entryExists || **/ newEntry)
		// newMap.put(key, translationEntry);
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
		ClassDescriptor entry = ClassDescriptor.getClassDescriptor(classObj);
		String tagName = entry.getTagName();

		entriesByTag.put(entry.getTagName(), entry);
		entriesByClassSimpleName.put(entry.getDescribedClassSimpleName(), entry);
		entriesByClassName.put(classObj.getName(), entry);

		entriesByTLVId.put(entry.getTagName().hashCode(), entry);
		entriesByBibTeXType.put(entry.getBibtexType(), entry);

		ArrayList<String> otherTags = entry.otherTags();
		if (otherTags != null)
		{
			for (String otherTag : otherTags)
			{
				if ((otherTag != null) && (otherTag.length() > 0))
				{
					entriesByTag.put(otherTag, entry);
					entriesByTLVId.put(otherTag.hashCode(), entry);
				}
			}
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
	// * You could even call it a ... Doppelgänger, if you wanted to. 
	

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
		entriesByTag.remove(entry.getTagName());
		entriesByClassSimpleName.remove(entry.getDescribedClassSimpleName());
		entriesByClassName.remove(className);

		entriesByTLVId.remove(entry.getTagName().hashCode());
		entriesByBibTeXType.remove(entry.getBibtexType());

		ArrayList<String> otherTags = entry.otherTags();
		if (otherTags != null)
		{
			for (String otherTag : otherTags)
			{
				if ((otherTag != null) && (otherTag.length() > 0))
				{
					entriesByTag.remove(otherTag);
					entriesByTLVId.remove(otherTag.hashCode());
				}
			}
		}
	}
	

	/**
	 * Add a translation table entry for an ElementState derived sub-class. Assumes that the xmlTag
	 * can be derived automatically from the className, by translating case-based separators to
	 * "_"-based separators.
	 * 
	 * @param classObj
	 *          The object for the class.
	 */

	public void addTranslation(ClassDescriptor classObj)
	{
		ClassDescriptor entry = classObj;
		String tagName = entry.getTagName();

		entriesByTag.put(entry.getTagName(), entry);
		entriesByClassSimpleName.put(entry.getDescribedClassSimpleName(), entry);
		entriesByClassName.put(classObj.getName(), entry);

		entriesByTLVId.put(entry.getTagName().hashCode(), entry);
		entriesByBibTeXType.put(entry.getBibtexType(), entry);

		ArrayList<String> otherTags = entry.otherTags();
		if (otherTags != null)
			for (String otherTag : otherTags)
			{
				if ((otherTag != null) && (otherTag.length() > 0))
				{
					entriesByTag.put(otherTag, entry);
					entriesByTLVId.put(otherTag.hashCode(), entry);
				}
			}
	}

	/**
	 * Look-up a <code>Class</code> object for the xmlTag, using translations in this, and in
	 * inherited SimplTypesScopes. Will use defaultPackage name here and, recursivley, in inherited
	 * scopes, as necessary.
	 * 
	 * @param xmlTag
	 *          XML node name that we're seeking a Class for.
	 * @return Class object, or null if there is no associated translation.
	 */
	public Class<?> xmlTagToClass(String xmlTag)
	{
		ClassDescriptor entry = xmlTagToTranslationEntry(xmlTag);
		return entry.isEmpty() ? null : entry.getDescribedClass();
	}

	/**
	 * Seek the entry associated with the tag. Recurse through inherited SimplTypesScopes, if
	 * necessary.
	 * 
	 * @param xmlTag
	 * @return
	 */
	private ClassDescriptor xmlTagToTranslationEntry(String xmlTag)
	{
		return getClassDescriptorByTag(xmlTag);
		/*
		 * TranslationEntry entry = entriesByTag.get(xmlTag); if (entry == null) { String
		 * defaultPackageName = this.defaultPackageName; if (defaultPackageName != null) { String
		 * classSimpleName = XMLTools.classNameFromElementName(xmlTag); entry = new
		 * TranslationEntry(defaultPackageName, classSimpleName, xmlTag); if (entry.empty) { if
		 * (inheritedTranslationScopes != null) { // recurse through inherited, continuing to seek a
		 * translation for (TranslationScope inherited : inheritedTranslationScopes) { entry =
		 * inherited.xmlTagToTranslationEntry(xmlTag); if (entry != null) { // got one from an inherited
		 * TranslationScope // register translation for the inherited entry in this
		 * entriesByTag.put(xmlTag, entry); entriesByClassSimpleName.put(classSimpleName, entry); break;
		 * } } } } } else { // empty entry construction added by andruid 11/11/07 entry = new
		 * TranslationEntry(xmlTag); // new empty entry } } return entry;
		 */
	}

	/**
	 * Get the Class object associated with this tag, if there is one. Unlike xmlTagToClass, this call
	 * will not generate a new blank NameEntry.
	 * 
	 * @param tag
	 * @return
	 */
	public Class<?> getClassByTag(String tag)
	{
		ClassDescriptor entry = getClassDescriptorByTag(tag);

		return (entry == null) ? null : entry.getDescribedClass();
	}

	public ClassDescriptor<? extends FieldDescriptor> getClassDescriptorByTag(String tag)
	{
		return entriesByTag.get(tag);
	}
	
	public ClassDescriptor<? extends FieldDescriptor> getClassDescriptorByTlvId(int id)
	{
		return entriesByTLVId.get(id);
	}

	public ClassDescriptor getClassDescriptorByTLVId(int tlvId)
	{
		return entriesByTLVId.get(tlvId);
	}

	public ClassDescriptor getClassDescriptorByBibTeXType(String typeName)
	{
		return entriesByBibTeXType.get(typeName);
	}

	/**
	 * Get the Class object associated with the provided class name, if there is one. Unlike
	 * xmlTagToClass, this call will not generate a new blank NameEntry.
	 * 
	 * @param classSimpleName
	 *          Simple name of the class (no package).
	 * @return
	 */
	public Class<?> getClassBySimpleName(String classSimpleName)
	{
		ClassDescriptor entry = getClassDescriptorBySimpleName(classSimpleName);
		return (entry == null) ? null : entry.getDescribedClass();
	}

	public ClassDescriptor getClassDescriptorBySimpleName(String classSimpleName)
	{
		return entriesByClassSimpleName.get(classSimpleName);
	}

	public Class<?> getClassByName(String className)
	{
		ClassDescriptor entry = entriesByClassName.get(className);

		return (entry == null) ? null : entry.getDescribedClass();
	}

	public ClassDescriptor getClassDescriptorByClassName(String className)
	{
		return entriesByClassName.get(className);
	}

	public ArrayList<Class<?>> getAllClasses()
	{
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		Collection<ClassDescriptor<? extends FieldDescriptor>> classDescriptors = this
				.getClassDescriptors();

		for (SimplTypesScope typesScope : allTypesScopes.values())
		{
			for (ClassDescriptor<? extends FieldDescriptor> classDescriptor : typesScope.entriesByClassSimpleName
					.values())
			{
				classes.add(classDescriptor.getDescribedClass());
			}
		}
		return classes;
	}

	/**
	 * Use this SimplTypesScope to lookup a class that has the same simple name as the argument
	 * passed in here. It may have a different full name, that is, a different package, which could be
	 * quite convenient for overriding with subclasses.
	 * 
	 * @param thatClass
	 * @return
	 */
	public Class<?> getClassBySimpleNameOfClass(Class<?> thatClass)
	{
		return getClassBySimpleName(classSimpleName(thatClass));
	}

	/**
	 * Lookup the tag for the class in question, using this.
	 * 
	 * @param thatClass
	 * @return
	 */
	public String getTag(Class<?> thatClass)
	{
		return getTagBySimpleName(classSimpleName(thatClass));
	}

	public String getTagBySimpleName(String simpleName)
	{
		ClassDescriptor entry = entriesByClassSimpleName.get(simpleName);

		return (entry == null) ? null : entry.getTagName();
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
		return (SimplTypesScope) allTypesScopes.get(name);
	}

	/**
	 * Unlike other get() methods in this class, this one is not a factory, but a simple accessor. It
	 * performs a lookup, but does not construct.
	 * 
	 * @param name
	 * @return
	 */
	public static SimplTypesScope get(String name)
	{
		return lookup(name);
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one.
	 * 
	 * @param name
	 *          the name of the SimplTypesScope
	 * @param translations
	 *          a set of Classes to be used as a part of this SimplTypesScope
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name, Class... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, translations);
			}
		}
		return result;
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Inherit from the previous
	 * SimplTypesScope, by including all mappings from there.
	 * 
	 * If new translations are provided when the SimplTypesScope already exists in the static scope
	 * map, they are ignored.
	 * 
	 * @param name
	 * @param inheritedTranslations
	 * @param translations
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name, SimplTypesScope inheritedTranslations,
			Class[]... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, inheritedTranslations, translations);
			}
		}
		return result;
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Inherit from the previous
	 * SimplTypesScope, by including all mappings from there.
	 * 
	 * If new translations are provided when the SimplTypesScope already exists in the static scope
	 * map, they are ignored.
	 * 
	 * @param name
	 * @param inheritedTranslations
	 * @param translations
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name, SimplTypesScope inheritedTranslations,
			Class... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, inheritedTranslations, translations);
			}
		}
		return result;
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Build on a previous
	 * SimplTypesScope, by including all mappings from there. Add just a single new class.
	 * 
	 * @param name
	 * @param inheritedTranslations
	 * @param translation
	 * @return
	 */
	public static SimplTypesScope get(String name, SimplTypesScope inheritedTranslations,
			Class<?> translation)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, inheritedTranslations, translation);
			}
		}
		return result;
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Add just a single new
	 * class.
	 * 
	 * @param name
	 * @param translation
	 * @return
	 */
	public static SimplTypesScope get(String name, Class<?> translation)
	{
		return get(name, null, translation);
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Build on the previous
	 * SimplTypesScope, by including all mappings from there.
	 * 
	 * @param name
	 *          the name of the SimplTypesScope to acquire.
	 * @param translations
	 *          an array of translations to add to the scope.
	 * @param inheritedTranslations
	 *          a list of previous translation scopes to build upon.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name, SimplTypesScope[] inheritedTranslationsSet,
			Class... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, inheritedTranslationsSet, translations);
			}
		}
		return result;
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Build on the previous
	 * SimplTypesScope, by including all mappings from there.
	 * 
	 * @param name
	 *          the name of the SimplTypesScope to acquire.
	 * @param translations
	 *          an array of translations to add to the scope.
	 * @param inheritedTranslations
	 *          a list of previous translation scopes to build upon.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name, SimplTypesScope[] inheritedTranslationsSet,
			Class[]... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, inheritedTranslationsSet, translations);
			}
		}
		return result;
	}

	public static SimplTypesScope get(String name, SimplTypesScope inheritedTranslations0,
			SimplTypesScope inheritedTranslations1, Class... translations)
	{
		SimplTypesScope[] inheritedArray = new SimplTypesScope[2];
		inheritedArray[0] = inheritedTranslations0;
		inheritedArray[1] = inheritedTranslations1;
		return get(name, inheritedArray, translations);
	}

	public static SimplTypesScope get(String name, NameSpaceDecl[] nameSpaceDecls,
			Class... translations)
	{
		return get(name, nameSpaceDecls, null, translations);
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Build on a set of
	 * inherited SimplTypesScopes, by including all mappings from them.
	 * 
	 * @param name
	 * @param nameSpaceDecls
	 *          Array of ElementState class + URI key map entries for handling XML Namespaces.
	 * @param inheritedTranslationsSet
	 * @param translations
	 * @param defaultPackageName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name, NameSpaceDecl[] nameSpaceDecls,
			SimplTypesScope[] inheritedTranslationsSet, Class... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, nameSpaceDecls, inheritedTranslationsSet,
							translations);
			}
		}
		return result;
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Build on a set of
	 * inherited SimplTypesScopes, by including all mappings from them.
	 * 
	 * @param name
	 * @param inheritedTranslations
	 * @return
	 */
	public static SimplTypesScope get(String name, SimplTypesScope... inheritedTranslations)
	{
		return get(name, inheritedTranslations, null);
	}

	/**
	 * Find an existing SimplTypesScope by this name, or create a new one. Build on a set of
	 * inherited SimplTypesScopes, by including all mappings from them.
	 * 
	 * @param name
	 * @param inheritedTranslationsSet
	 * @param translations
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static SimplTypesScope get(String name,
			Collection<SimplTypesScope> inheritedTranslationsSet, Class... translations)
	{
		SimplTypesScope result = lookup(name);
		if (result == null)
		{
			synchronized (name)
			{
				result = lookup(name);
				if (result == null)
					result = new SimplTypesScope(name, inheritedTranslationsSet, translations);
			}
		}
		return result;
	}

	protected HashMap<String, ClassDescriptor<? extends FieldDescriptor>> entriesByClassSimpleName()
	{
		return entriesByClassSimpleName;
	}

	public HashMap<String, ClassDescriptor<? extends FieldDescriptor>> entriesByClassName()
	{
		return entriesByClassName;
	}

	public HashSet<String> addClassNamesToHashSet(HashSet<String> hashSet)
	{
		if (inheritedTypesScopes != null)
		{
			for (SimplTypesScope inheritedTScope : inheritedTypesScopes)
			{
				inheritedTScope.generateImports(hashSet);
			}
		}
		this.generateImports(hashSet);
		return hashSet;
	}

	protected void generateImports(HashSet<String> hashSet)
	{
		for (String className : entriesByClassName.keySet())
		{
			hashSet.add(className);
		}
	}

	private ArrayList<ClassDescriptor<? extends FieldDescriptor>>	classDescriptors;

	// FIXME -- implement this!
	public ArrayList<ClassDescriptor<? extends FieldDescriptor>> getClassDescriptors()
	{
		ArrayList<ClassDescriptor<? extends FieldDescriptor>> result = classDescriptors;
		if (result == null)
		{
			// result = entriesByClassSimpleName.values();
			result = new ArrayList<ClassDescriptor<? extends FieldDescriptor>>(entriesByTag.values()); // we use entriesByTag so that overriding works well.
			this.classDescriptors = result;
		}
		return result;
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
	 * Lookup a NameSpace ElementState subclass, with a URN as the key.
	 * 
	 * @param urn
	 * @return
	 */
	public Class<?> lookupNameSpaceByURN(String urn)
	{
		return nameSpaceClassesByURN.get(urn);
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

	private void addSimplTypesScope(String name)
	{
		allTypesScopes.put(name, this);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(file, translationContext, null, format);
		TranslationContextPool.get().release(translationContext);
		return obj;
	}

	public Object deserialize(File file, DeserializationHookStrategy deserializationHookStrategy,
			Format format) throws SIMPLTranslationException
	{
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(file, translationContext, deserializationHookStrategy, format);
		TranslationContextPool.get().release(translationContext);
		return obj;
	}

	public Object deserialize(ParsedURL parsedURL,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(parsedURL, translationContext, deserializationHookStrategy, format);
		TranslationContextPool.get().release(translationContext);
		return obj;
	}

	public Object deserialize(ParsedURL parsedURL, TranslationContext translationContext,
			Format format) throws SIMPLTranslationException
	{
		return deserialize(parsedURL, translationContext, null, format);
	}

	public Object deserialize(ParsedURL parsedURL, Format format) throws SIMPLTranslationException
	{
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(parsedURL, translationContext, null, format);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(inputStream, translationContext, deserializationHookStrategy, format,
				null);
		TranslationContextPool.get().release(translationContext);
		return obj;
	}

	public Object deserialize(InputStream inputStream,
			DeserializationHookStrategy deserializationHookStrategy, Format format, Charset charSet)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(inputStream, translationContext, deserializationHookStrategy, format,
				charSet);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(inputStream, translationContext, null, format, null);
		TranslationContextPool.get().release(translationContext);
		return obj;
	}

	public Object deserialize(InputStream inputStream, Format format, Charset charSet)
			throws SIMPLTranslationException
	{
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(inputStream, translationContext, null, format, charSet);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(new ParsedURL(url), translationContext, null, format);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		StringPullDeserializer pullDeserializer = PullDeserializer.getStringDeserializer(this,
				translationContext, deserializationHookStrategy, stringFormat);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		Object obj = deserialize(charSequence, translationContext, null, stringFormat);
		TranslationContextPool.get().release(translationContext);
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

	public static SimplTypesScope getBasicTranslations()
	{
		return get(BASIC_TRANSLATIONS, SimplTypesScope.class, FieldDescriptor.class,
				ClassDescriptor.class);
	}

	public static SimplTypesScope augmentTranslationScope(SimplTypesScope simplTypesScope)
	{
		ArrayList<Class<?>> allClasses = simplTypesScope.getAllClasses();
		Collection<Class<?>> augmentedClasses = augmentTranslationScope(allClasses).values();

		Class<?>[] augmentedClassesArray = (Class<?>[]) augmentedClasses
				.toArray(new Class<?>[augmentedClasses.size()]);

		return new SimplTypesScope(simplTypesScope.getName(), augmentedClassesArray);
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

	public void augment()
	{
		Class<?>[] augmentedClassesArray = getClassesArray(this);

		this.addTranslations(augmentedClassesArray);
	}

	private static Class<?>[] getClassesArray(SimplTypesScope simplTypesScope)
	{
		ArrayList<Class<?>> allClasses = simplTypesScope.getAllClasses();
		Collection<Class<?>> augmentedClasses = augmentTranslationScope(allClasses).values();

		Class<?>[] augmentedClassesArray = (Class<?>[]) augmentedClasses
				.toArray(new Class<?>[augmentedClasses.size()]);
		return augmentedClassesArray;
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
	 * Augment the given SimplTypesScope and return the augmented one
	 * 
	 * @param simplTypesScope
	 * @return
	 */
	public static SimplTypesScope augmentTranslationScopeWithClassDescriptors(
			SimplTypesScope simplTypesScope)
	{
		Collection<ClassDescriptor<? extends FieldDescriptor>> allClassDescriptors = simplTypesScope
				.getClassDescriptors();

		ArrayList<ClassDescriptor<? extends FieldDescriptor>> allClasses = simplTypesScope
				.getAllClassDescriptors();
		Collection<ClassDescriptor<? extends FieldDescriptor>> augmentedClasses = augmentSimplTypesScopeWithClassDescriptors(
				allClasses).values();

		ClassDescriptor<? extends FieldDescriptor>[] augmentedClassesArray = (ClassDescriptor[]) augmentedClasses
				.toArray(new ClassDescriptor[augmentedClasses.size()]);

		return new SimplTypesScope(simplTypesScope.getName(), augmentedClassesArray);
	}

	/**
	 * augment the given the list of classes
	 * 
	 * @param allClasses
	 * @return
	 */
	private static HashMap<String, ClassDescriptor<? extends FieldDescriptor>> augmentSimplTypesScopeWithClassDescriptors(
			ArrayList<ClassDescriptor<? extends FieldDescriptor>> allClasses)
	{
		HashMap<String, ClassDescriptor<? extends FieldDescriptor>> augmentedClasses = new HashMap<String, ClassDescriptor<? extends FieldDescriptor>>();
		for (ClassDescriptor<? extends FieldDescriptor> thatClass : allClasses)
		{
			augmentSimplTypesScope(thatClass, augmentedClasses);
		}
		return augmentedClasses;
	}

	/**
	 * augment the given ClassDescriptor
	 * 
	 * @param thatClass
	 * @param augmentedClasses
	 */
	private static void augmentSimplTypesScope(ClassDescriptor<? extends FieldDescriptor> thatClass,
			HashMap<String, ClassDescriptor<? extends FieldDescriptor>> augmentedClasses)
	{
		if (augmentedClasses.put(thatClass.getDescribedClassSimpleName(), thatClass) != null)
			return;

		ClassDescriptor<? extends FieldDescriptor> superClass = thatClass.getSuperClass();
		if (superClass != null && !"ElementState".equals(superClass.getDescribedClassSimpleName()))
		{
			augmentSimplTypesScope(superClass, augmentedClasses);
		}

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = thatClass
				.getFieldDescriptorsByFieldName();

		if (fieldDescriptors.size() > 0)
		{
			thatClass.resolvePolymorphicAnnotations();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.isNested())
				{
					augmentSimplTypesScope(fieldDescriptor.getElementClassDescriptor(), augmentedClasses);
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
								augmentSimplTypesScope(ClassDescriptor.getClassDescriptor(genericClass
										.asSubclass(ElementState.class)), augmentedClasses);
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
								augmentSimplTypesScope(classDescriptor, augmentedClasses);
							}
						}
					}
				}
			}
		}
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
	 * Make a new SimplTypesScope from a subset of this, making sure that the class of all entries in
	 * the subset is either superClassCriterion or a subclass thereof.
	 * 
	 * @param newName
	 *          Name for new SimplTypesScope.
	 * @param superClassCriterion
	 *          Super class discriminant for all classes in the subset.
	 * 
	 * @return New or existing SimplTypesScope with subset of classes in this, based on
	 *         assignableCriterion.
	 */
	public SimplTypesScope getAssignableSubset(String newName, Class<?> superClassCriterion)
	{
		SimplTypesScope result = lookup(newName);
		if (result == null)
		{
			synchronized (entriesByClassName)
			{
				result = lookup(newName);
				if (result == null)
				{
					result = new SimplTypesScope(newName);
					addSimplTypesScope(newName);
					for (ClassDescriptor classDescriptor : entriesByClassName.values())
					{
						Class<?> thatClass = classDescriptor.getDescribedClass();
						if (superClassCriterion.isAssignableFrom(thatClass))
							result.addTranslation(classDescriptor);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Make a new SimplTypesScope from a subset of this, making sure that the class of all entries in
	 * never either superClassCriterion or a subclass thereof.
	 * 
	 * @param newName
	 *          Name for new SimplTypesScope.
	 * @param superClassCriterion
	 *          Super class discriminant for all classes to remove from the subset.
	 * 
	 * @return New or existing SimplTypesScope with subset of classes in this, based on
	 *         assignableCriterion.
	 */
	public SimplTypesScope getSubtractedSubset(String newName, Class<?> superClassCriterion)
	{
		SimplTypesScope result = lookup(newName);
		if (result == null)
		{
			synchronized (entriesByClassName)
			{
				result = lookup(newName);
				if (result == null)
				{
					result = new SimplTypesScope(newName);
					addSimplTypesScope(newName);
					for (ClassDescriptor classDescriptor : entriesByClassName.values())
					{
						Class<?> thatClass = classDescriptor.getDescribedClass();
						if (!superClassCriterion.isAssignableFrom(thatClass))
							result.addTranslation(classDescriptor);
					}
				}
			}
		}
		return result;
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
			entriesByClassName.put(classDescriptor.getName(), classDescriptor);
			String simpleName = classDescriptor.getDescribedClassSimpleName();
			entriesByClassSimpleName.put(simpleName, classDescriptor);
		}
		if (allTypesScopes.containsKey(name))
			warning("REPLACING another SimplTypesScope of the SAME NAME during deserialization!\t"
					+ name);
		allTypesScopes.put(name, this);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		serialize(object, appendable, stringFormat, translationContext);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		StringBuilder sb = serialize(object, stringFormat, translationContext);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		serialize(object, stringBuilder, stringFormat, translationContext);
		TranslationContextPool.get().release(translationContext);
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
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		serialize(object, file, format, translationContext);
		TranslationContextPool.get().release(translationContext);
	}
}
