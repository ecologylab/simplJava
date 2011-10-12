package ecologylab.serialization;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
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

	private SimplTypesScope[]																						inheritedTranslationScopes;

	/**
	 * Fundamentally, a TranslationScope consists of a set of class simple names. These are mapped to
	 * tag names (camel case conversion), and to Class objects. Because there are many packages,
	 * globally, there could be more than one class with one single name.
	 * <p/>
	 * Among other things, a TranslationScope tells us *which* package's version will be used, if
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

	private static HashMap<String, SimplTypesScope>											allTranslationScopes			= new HashMap<String, SimplTypesScope>();

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
	 * Create a new TranslationScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by copying in the translations from another, pre-existing
	 * "base" TranslationScope.
	 * 
	 * @param name
	 * @param inheritedTranslationScope
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedTranslationScope)
	{
		this(name);
		addTranslations(inheritedTranslationScope);
		SimplTypesScope[] inheritedTranslationScopes = new SimplTypesScope[1];
		inheritedTranslationScopes[0] = inheritedTranslationScope;
		this.inheritedTranslationScopes = inheritedTranslationScopes;
	}

	private SimplTypesScope(String name, SimplTypesScope inheritedTranslationScope,
			Class<?> translation)
	{
		this(name, inheritedTranslationScope);
		addTranslation(translation);
		addTranslationScope(name);
	}

	/**
	 * Create a new TranslationScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by creating the inherited TranslationScope ad then adding the
	 * new ClassDescriptor intothat
	 * 
	 * @param name
	 * @param inheritedTranslationScope
	 * @param translation
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedTranslationScope,
			ClassDescriptor translation)
	{
		this(name, inheritedTranslationScope);
		addTranslation(translation);
		addTranslationScope(name);
	}

	/**
	 * Create a new TranslationScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by copying in the translations from another, pre-existing
	 * "base" TranslationScope.
	 * 
	 * @param name
	 * @param baseTranslationSet
	 */
	private SimplTypesScope(String name, SimplTypesScope... inheritedTranslationScopes)
	{
		this(name);

		if (inheritedTranslationScopes != null)
		{
			this.inheritedTranslationScopes = inheritedTranslationScopes;
			int n = inheritedTranslationScopes.length;
			for (int i = 0; i < n; i++)
				addTranslations(inheritedTranslationScopes[i]);
		}
	}

	/**
	 * Create a new TranslationScope that defines how to translate xml tag names into class names of
	 * subclasses of ElementState. Begin by copying in the translations from another, pre-existing
	 * "base" TranslationScope.
	 * 
	 * @param name
	 * @param baseTranslationSet
	 */
	private SimplTypesScope(String name, Collection<SimplTypesScope> baseTranslationsSet)
	{
		this(name);
		for (SimplTypesScope thatTranslationScope : baseTranslationsSet)
			addTranslations(thatTranslationScope);
		inheritedTranslationScopes = (SimplTypesScope[]) baseTranslationsSet.toArray();
	}

	/**
	 * Create a new TranslationScope that defines how to translate xml tag names into class names of
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
		addTranslationScope(name);
	}

	/**
	 * Create a new TranslationScope that defines how to translate xml tag names into class names of
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
		addTranslationScope(name);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedTranslationScopes
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope[] inheritedTranslationScopes,
			Class<?>[]... translations)
	{
		this(name, inheritedTranslationScopes);
		addTranslations(translations);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedTranslationScopes
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope[] inheritedTranslationScopes,
			ClassDescriptor[]... translations)
	{
		this(name, inheritedTranslationScopes);
		addTranslations(translations);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
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

		addTranslationScope(name);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
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

		addTranslationScope(name);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedTranslationScope
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedTranslationScope,
			Class<?>[]... translations)
	{
		this(name, inheritedTranslationScope);
		addTranslations(translations);

		addTranslationScope(name);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available.
	 * 
	 * @param name
	 * @param inheritedTranslationScope
	 * @param translations
	 */
	private SimplTypesScope(String name, SimplTypesScope inheritedTranslationScope,
			ClassDescriptor[]... translations)
	{
		this(name, inheritedTranslationScope);
		addTranslations(translations);

		addTranslationScope(name);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available. Map XML Namespace
	 * declarations.
	 * 
	 * @param name
	 * @param nameSpaceDecls
	 * @param inheritedTranslationScopes
	 * @param translations
	 * @param defaultPackgeName
	 */
	private SimplTypesScope(String name, NameSpaceDecl[] nameSpaceDecls,
			SimplTypesScope[] inheritedTranslationScopes, Class<?>[] translations)
	{
		this(name, inheritedTranslationScopes, translations);
		addNameSpaceDecls(nameSpaceDecls);

		addTranslationScope(name);
	}

	/**
	 * Construct a new TranslationScope, with this name, using the baseTranslations first. Then, add
	 * the array of translations, then, make the defaultPackageName available. Map XML Namespace
	 * declarations.
	 * 
	 * @param name
	 * @param nameSpaceDecls
	 * @param inheritedTranslationScopes
	 * @param translations
	 */
	private SimplTypesScope(String name, NameSpaceDecl[] nameSpaceDecls,
			SimplTypesScope[] inheritedTranslationScopes, ClassDescriptor[] translations)
	{
		this(name, inheritedTranslationScopes, translations);
		addNameSpaceDecls(nameSpaceDecls);

		addTranslationScope(name);
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

		allTranslationScopes.put(name, this);
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

		allTranslationScopes.put(name, this);
	}

	/**
	 * Utility for composing <code>TranslationScope</code>s. Performs composition by value. That is,
	 * the entries are copied.
	 * 
	 * Unlike in union(), if there are duplicates, they will override identical entries in this.
	 * 
	 * @param inheritedTranslationScope
	 */
	private void addTranslations(SimplTypesScope inheritedTranslationScope)
	{
		if (inheritedTranslationScope != null)
		{
			// copy map entries from inherited maps into new maps
			updateMapWithValues(inheritedTranslationScope.entriesByClassSimpleName,
					entriesByClassSimpleName, "classSimpleName");
			updateMapWithValues(inheritedTranslationScope.entriesByClassName, entriesByClassName,
					"className");
			updateMapWithValues(inheritedTranslationScope.entriesByTag, entriesByTag, "tagName");

			HashMap<String, Class<?>> inheritedNameSpaceClassesByURN = inheritedTranslationScope.nameSpaceClassesByURN;
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
	 * inherited TranslationScopes. Will use defaultPackage name here and, recursivley, in inherited
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
	 * Seek the entry associated with the tag. Recurse through inherited TranslationScopes, if
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

		for (SimplTypesScope translationScope : allTranslationScopes.values())
		{
			for (ClassDescriptor<? extends FieldDescriptor> classDescriptor : translationScope.entriesByClassSimpleName
					.values())
			{
				classes.add(classDescriptor.getDescribedClass());
			}
		}
		return classes;
	}

	/**
	 * Use this TranslationScope to lookup a class that has the same simple name as the argument
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

	public String toString()
	{
		if (toStringCache == null)
		{
			toStringCache = "TranslationScope[" + name + "]";
		}
		return toStringCache;
	}

	/**
	 * Find the TranslationScope called <code>name</code>, if there is one.
	 * 
	 * @param name
	 * @return
	 */
	public static SimplTypesScope lookup(String name)
	{
		return (SimplTypesScope) allTranslationScopes.get(name);
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
	 * Find an existing TranslationScope by this name, or create a new one.
	 * 
	 * @param name
	 *          the name of the TranslationScope
	 * @param translations
	 *          a set of Classes to be used as a part of this TranslationScope
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
	 * Find an existing TranslationScope by this name, or create a new one. Inherit from the previous
	 * TranslationScope, by including all mappings from there.
	 * 
	 * If new translations are provided when the TranslationScope already exists in the static scope
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
	 * Find an existing TranslationScope by this name, or create a new one. Inherit from the previous
	 * TranslationScope, by including all mappings from there.
	 * 
	 * If new translations are provided when the TranslationScope already exists in the static scope
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
	 * Find an existing TranslationScope by this name, or create a new one. Build on a previous
	 * TranslationScope, by including all mappings from there. Add just a single new class.
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
	 * Find an existing TranslationScope by this name, or create a new one. Add just a single new
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
	 * Find an existing TranslationScope by this name, or create a new one. Build on the previous
	 * TranslationScope, by including all mappings from there.
	 * 
	 * @param name
	 *          the name of the TranslationScope to acquire.
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
	 * Find an existing TranslationScope by this name, or create a new one. Build on the previous
	 * TranslationScope, by including all mappings from there.
	 * 
	 * @param name
	 *          the name of the TranslationScope to acquire.
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
	 * Find an existing TranslationScope by this name, or create a new one. Build on a set of
	 * inherited TranslationScopes, by including all mappings from them.
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
	 * Find an existing TranslationScope by this name, or create a new one. Build on a set of
	 * inherited TranslationScopes, by including all mappings from them.
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
	 * Find an existing TranslationScope by this name, or create a new one. Build on a set of
	 * inherited TranslationScopes, by including all mappings from them.
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
		if (inheritedTranslationScopes != null)
		{
			for (SimplTypesScope inheritedTScope : inheritedTranslationScopes)
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

	private Collection<ClassDescriptor<? extends FieldDescriptor>>	classDescriptors;

	// FIXME -- implement this!
	public Collection<ClassDescriptor<? extends FieldDescriptor>> getClassDescriptors()
	{
		Collection<ClassDescriptor<? extends FieldDescriptor>> result = classDescriptors;
		if (result == null)
		{
			// result = entriesByClassSimpleName.values();
			result = entriesByTag.values(); // we use entriesByTag so that overriding works well.
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

	private void addTranslationScope(String name)
	{
		allTranslationScopes.put(name, this);
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
		return deserialize(file, new TranslationContext(), null, format);
	}

	public Object deserialize(File file, DeserializationHookStrategy deserializationHookStrategy,
			Format format) throws SIMPLTranslationException
	{
		return deserialize(file, new TranslationContext(), deserializationHookStrategy, format);
	}

	public Object deserialize(ParsedURL parsedURL,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		return deserialize(parsedURL, new TranslationContext(), deserializationHookStrategy, format);
	}

	public Object deserialize(ParsedURL parsedURL, TranslationContext translationContext,
			Format format) throws SIMPLTranslationException
	{
		return deserialize(parsedURL, translationContext, null, format);
	}

	public Object deserialize(ParsedURL parsedURL, Format format) throws SIMPLTranslationException
	{
		return deserialize(parsedURL, new TranslationContext(), null, format);
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
		return deserialize(inputStream, new TranslationContext(), deserializationHookStrategy, format);
	}

	public Object deserialize(InputStream inputStream, TranslationContext translationContext,
			Format format) throws SIMPLTranslationException
	{
		return deserialize(inputStream, translationContext, null, format);
	}

	public Object deserialize(InputStream inputStream, Format format)
			throws SIMPLTranslationException
	{
		return deserialize(inputStream, new TranslationContext(), null, format);
	}

	public Object deserialize(InputStream inputStream, TranslationContext translationContext,
			DeserializationHookStrategy deserializationHookStrategy, Format format)
			throws SIMPLTranslationException
	{
		PullDeserializer pullDeserializer = PullDeserializer.getDeserializer(this, translationContext,
				deserializationHookStrategy, format);
		return pullDeserializer.parse(inputStream);
	}

	public Object deserialize(URL url, Format format) throws SIMPLTranslationException
	{
		return deserialize(new ParsedURL(url), new TranslationContext(), null, format);
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
		StringPullDeserializer pullDeserializer = PullDeserializer.getStringDeserializer(this,
				new TranslationContext(), deserializationHookStrategy, stringFormat);
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
		return deserialize(charSequence, new TranslationContext(), null, stringFormat);
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

	public static SimplTypesScope augmentTranslationScope(SimplTypesScope translationScope)
	{
		ArrayList<Class<?>> allClasses = translationScope.getAllClasses();
		Collection<Class<?>> augmentedClasses = augmentTranslationScope(allClasses).values();

		Class<?>[] augmentedClassesArray = (Class<?>[]) augmentedClasses
				.toArray(new Class<?>[augmentedClasses.size()]);

		return new SimplTypesScope(translationScope.getName(), augmentedClassesArray);
	}

	private static HashMap<String, Class<?>> augmentTranslationScope(ArrayList<Class<?>> allClasses)
	{
		HashMap<String, Class<?>> augmentedClasses = new HashMap<String, Class<?>>();
		for (Class<?> thatClass : allClasses)
		{
			augmentTranslationScope(thatClass, augmentedClasses);
		}
		return augmentedClasses;
	}

	private static void augmentTranslationScope(Class<?> thatClass,
			HashMap<String, Class<?>> augmentedClasses)
	{
		if (augmentedClasses.put(thatClass.getSimpleName(), thatClass) != null)
			return;

		if (thatClass.getSuperclass() != ElementState.class)
		{
			augmentTranslationScope(thatClass.getSuperclass().asSubclass(ElementState.class),
					augmentedClasses);
		}

		ClassDescriptor<? extends FieldDescriptor> thatClassDescriptor = ClassDescriptor
				.getClassDescriptor(thatClass);

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = thatClassDescriptor
				.getFieldDescriptorsByFieldName();

		if (fieldDescriptors.size() > 0)
		{
			thatClassDescriptor.resolveUnresolvedScopeAnnotationFDs();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.isNested())
				{
					augmentTranslationScope(fieldDescriptor.getFieldType().asSubclass(ElementState.class),
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
								augmentTranslationScope(genericClass.asSubclass(ElementState.class),
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
								augmentTranslationScope(classDescriptor.getDescribedClass(), augmentedClasses);
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

	private static Class<?>[] getClassesArray(SimplTypesScope translationScope)
	{
		ArrayList<Class<?>> allClasses = translationScope.getAllClasses();
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
	 * Augment the given translationScope and return the augmented one
	 * 
	 * @param translationScope
	 * @return
	 */
	public static SimplTypesScope augmentTranslationScopeWithClassDescriptors(
			SimplTypesScope translationScope)
	{
		Collection<ClassDescriptor<? extends FieldDescriptor>> allClassDescriptors = translationScope
				.getClassDescriptors();

		ArrayList<ClassDescriptor<? extends FieldDescriptor>> allClasses = translationScope
				.getAllClassDescriptors();
		Collection<ClassDescriptor<? extends FieldDescriptor>> augmentedClasses = augmentTranslationScopeWithClassDescriptors(
				allClasses).values();

		ClassDescriptor<? extends FieldDescriptor>[] augmentedClassesArray = (ClassDescriptor[]) augmentedClasses
				.toArray(new ClassDescriptor[augmentedClasses.size()]);

		return new SimplTypesScope(translationScope.getName(), augmentedClassesArray);
	}

	/**
	 * augment the given the list of classes
	 * 
	 * @param allClasses
	 * @return
	 */
	private static HashMap<String, ClassDescriptor<? extends FieldDescriptor>> augmentTranslationScopeWithClassDescriptors(
			ArrayList<ClassDescriptor<? extends FieldDescriptor>> allClasses)
	{
		HashMap<String, ClassDescriptor<? extends FieldDescriptor>> augmentedClasses = new HashMap<String, ClassDescriptor<? extends FieldDescriptor>>();
		for (ClassDescriptor<? extends FieldDescriptor> thatClass : allClasses)
		{
			augmentTranslationScope(thatClass, augmentedClasses);
		}
		return augmentedClasses;
	}

	/**
	 * augment the given ClassDescriptor
	 * 
	 * @param thatClass
	 * @param augmentedClasses
	 */
	private static void augmentTranslationScope(ClassDescriptor<? extends FieldDescriptor> thatClass,
			HashMap<String, ClassDescriptor<? extends FieldDescriptor>> augmentedClasses)
	{
		if (augmentedClasses.put(thatClass.getDescribedClassSimpleName(), thatClass) != null)
			return;

		ClassDescriptor<? extends FieldDescriptor> superClass = thatClass.getSuperClass();
		if (superClass != null && !"ElementState".equals(superClass.getDescribedClassSimpleName()))
		{
			augmentTranslationScope(superClass, augmentedClasses);
		}

		HashMapArrayList<String, ? extends FieldDescriptor> fieldDescriptors = thatClass
				.getFieldDescriptorsByFieldName();

		if (fieldDescriptors.size() > 0)
		{
			thatClass.resolveUnresolvedScopeAnnotationFDs();

			for (FieldDescriptor fieldDescriptor : fieldDescriptors)
			{
				if (fieldDescriptor.isNested())
				{
					augmentTranslationScope(fieldDescriptor.getElementClassDescriptor(), augmentedClasses);
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
								augmentTranslationScope(ClassDescriptor.getClassDescriptor(genericClass
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
								augmentTranslationScope(classDescriptor, augmentedClasses);
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

		for (SimplTypesScope translationScope : allTranslationScopes.values())
		{
			for (ClassDescriptor<? extends FieldDescriptor> classDescriptor : translationScope.entriesByTag
					.values())
			{
				classes.add(classDescriptor);
			}
		}
		return classes;
	}

	/**
	 * Make a new TranslationScope from a subset of this, making sure that the class of all entries in
	 * the subset is either superClassCriterion or a subclass thereof.
	 * 
	 * @param newName
	 *          Name for new TranslationScope.
	 * @param superClassCriterion
	 *          Super class discriminant for all classes in the subset.
	 * 
	 * @return New or existing TranslationScope with subset of classes in this, based on
	 *         assignableCriterion.
	 */
	public SimplTypesScope getAssignableSubset(String newName, Class<?> superClassCriterion)
	{
		SimplTypesScope result = lookup(newName);
		if (result == null)
		{
			synchronized (newName)
			{
				result = lookup(newName);
				if (result == null)
				{
					result = new SimplTypesScope(newName);
					addTranslationScope(newName);
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
		if (allTranslationScopes.containsKey(name))
			warning("REPLACING another TranslationScope of the SAME NAME during deserialization!\t"
					+ name);
		allTranslationScopes.put(name, this);
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
		return serialize(object, stringFormat, translationContext);
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
}
