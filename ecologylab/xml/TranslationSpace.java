package ecologylab.xml;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ecologylab.generic.Debug;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.scalar.ScalarType;
import ecologylab.xml.types.scalar.TypeRegistry;

/**
 * A set of translations between XML element names (tags) and associated Java ElementState
 * class names.
 * This is done by maintaining a HashMap containing
 * the name of the class as the key and its package as the value. If no entry is 
 * present in the map, then the default package is returned. This is used when
 * an XML is converted back to its Java State-Object
 */
public final class TranslationSpace extends Debug
{
   private final String			name;
   /**
	* The default package. If an entry for a class is not found in the hashtable,
	* this package name is returned.
	*/
   private String				defaultPackageName;
   
   private TranslationSpace[]	inheritedTranslationSpaces;
   
   /**
    * Fundamentally, a TranslationSpace consists of a set of class simple names.
    * These are mapped to tag names (camel case conversion), and to Class objects.
    * Because there are many packages, globally, there could be more than one class
    * with one single name.
    * <p/>
    * Among other things, a TranslationSpace tells us *which* package's version will be used,
    * if there are multiple possibilities. This is the case when internal and external versions of
    * a message and its constituents are defined for a messaging API.
    */
   private HashMap<String, TranslationEntry>	entriesByClassSimpleName	= new HashMap<String, TranslationEntry>();
   private HashMap<String, TranslationEntry>	entriesByTag				= new HashMap<String, TranslationEntry>();
   
   private final HashMap<String, Class<? extends ElementState>>	nameSpaceClassesByURN = new HashMap<String, Class<? extends ElementState>>();
   
   private static HashMap<String, TranslationSpace>	allTranslationSpaces	= new HashMap<String, TranslationSpace>();
      
   /**
    * Create a new space that defines how to translate xml tag names into
    * class names of subclasses of ElementState.
    * 
    * @param name
    */
   private TranslationSpace(String name)
   {
	  this.name	= name;
	  allTranslationSpaces.put(name, this);
   }

   /**
    * Create a new TranslationSpace that defines how to translate xml tag names into
    * class names of subclasses of ElementState.
    * Begin by copying in the translations from another, pre-existing "base" TranslationSpace.
    * 
    * @param name
    * @param inheritedTranslationSpace
    */
   private TranslationSpace(String name, TranslationSpace inheritedTranslationSpace)
   {
	   this(name);
	   addTranslations(inheritedTranslationSpace);
	   TranslationSpace[] inheritedTranslationSpaces	= new TranslationSpace[1];
	   inheritedTranslationSpaces[0]					= inheritedTranslationSpace;
	   this.inheritedTranslationSpaces					= inheritedTranslationSpaces;
   }

   private TranslationSpace(String name, Class<? extends ElementState> translation, TranslationSpace inheritedTranslationSpaces)
   {
	   this(name, inheritedTranslationSpaces);
	   addTranslation(translation);
   }

   /**
    * Create a new TranslationSpace that defines how to translate xml tag names into
    * class names of subclasses of ElementState.
    * Begin by copying in the translations from another, pre-existing "base" TranslationSpace.
    * 
    * @param name
    * @param baseTranslationSet
    */
   private TranslationSpace(String name, TranslationSpace[] inheritedTranslationSpaces)
   {
	   this(name);

	   if (inheritedTranslationSpaces != null)
	   {
		   this.inheritedTranslationSpaces		= inheritedTranslationSpaces;
		   int n = inheritedTranslationSpaces.length;
		   for (int i = 0; i < n; i++)
			   addTranslations(inheritedTranslationSpaces[i]);
	   }
   }
   
   /**
    * Create a new TranslationSpace that defines how to translate xml tag names into
    * class names of subclasses of ElementState.
    * Begin by copying in the translations from another, pre-existing "base" TranslationSpace.
    * 
    * @param name
    * @param baseTranslationSet
    */
   private TranslationSpace(String name, ArrayList<TranslationSpace> baseTranslationsSet)
   {
	  this(name);
	  for (TranslationSpace thatTranslationSpace: baseTranslationsSet)
		  addTranslations(thatTranslationSpace);
	  inheritedTranslationSpaces		= (TranslationSpace[]) baseTranslationsSet.toArray();
   }
   
   /**
    * Create a new space that defines how to translate xml tag names into
    * class names of subclasses of ElementState.
    * 
    * Set a new default package.
    * 
    * @param name
    * @param defaultPackgeName
    */
   private TranslationSpace(String name, String defaultPackgeName)
   {
	   this(name);
	   this.setDefaultPackageName(defaultPackgeName);
   }
   
   /**
     * Create a new space that defines how to translate xml tag names into
     * class names of subclasses of ElementState.
     * 
     * Set a new default package, and
 	* a set of defined translations.
     * 
     * @param name		Name of the TranslationSpace to be 
 	*					A key for use in the TranslationSpace registry.
  * @param translations		Set of initially defined translations for this.
  * @param defaultPackgeName
     */
    private TranslationSpace(String name, Class<? extends ElementState>[] translations, 
 					String defaultPackgeName)
    {
 	   this(name, translations, (TranslationSpace[]) null, defaultPackgeName);
    }
   
   /**
    * Construct a new TranslationSpace, with this name, using the baseTranslations first.
    * Then, add the array of translations, then, make the defaultPackageName available.
    * 
    * @param name
 * @param translations
 * @param inheritedTranslationSpaces
 * @param defaultPackgeName
    */
   private TranslationSpace(String name, Class<? extends ElementState>[] translations, TranslationSpace[] inheritedTranslationSpaces,
		   String defaultPackgeName)
   {
	   this(name, inheritedTranslationSpaces);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
   }
   /**
    * Construct a new TranslationSpace, with this name, using the baseTranslations first.
    * Then, add the array of translations, then, make the defaultPackageName available.
    * 
    * @param name
 * @param translations
 * @param defaultPackgeName
 * @param baseTranslations
    */
   private TranslationSpace(String name, Class<? extends ElementState>[] translations, ArrayList<TranslationSpace> inheritedTranslationsSet,
		   String defaultPackgeName)
   {
	   this(name, inheritedTranslationsSet);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
   }
   
   /**
    * Construct a new TranslationSpace, with this name, using the baseTranslations first.
    * Then, add the array of translations, then, make the defaultPackageName available.
    * 
    * @param name
    * @param translations
    * @param inheritedTranslations
    * @param defaultPackgeName
    */
   private TranslationSpace(String name, Class<? extends ElementState>[] translations, TranslationSpace inheritedTranslations,
		   String defaultPackgeName)
   {
	   this(name, inheritedTranslations);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
   }

   /**
    * Construct a new TranslationSpace, with this name, using the baseTranslations first.
    * Then, add the array of translations, then, make the defaultPackageName available.
    * Map XML Namespace declarations.
    *      
    * @param name
    * @param translations
    * @param inheritedTranslations
    * @param defaultPackgeName
    * @param nameSpaceDecls
    */
   private TranslationSpace(String name, Class<? extends ElementState>[] translations, TranslationSpace[] inheritedTranslations,
		   String defaultPackgeName, NameSpaceDecl[] nameSpaceDecls)
   {
	   this(name, translations, inheritedTranslations, defaultPackgeName);
	   addNameSpaceDecls(nameSpaceDecls);
   }
   
   /**
    * Map XML Namespace ElementState subclasses to URIs.
    * 
    * @param nameSpaceDecls
    */
   private void addNameSpaceDecls(NameSpaceDecl[] nameSpaceDecls)
   {
	   if (nameSpaceDecls != null)
		   for (NameSpaceDecl nsd: nameSpaceDecls)
		   {
			   registerNameSpaceDecl(nsd);
		   }
   }
   
   /**
    * Enter a NameSpaceDecl into nameSpaceClassesByURN.
    * @param nsd
    */
   private void registerNameSpaceDecl(NameSpaceDecl nsd)
   {
	   nameSpaceClassesByURN.put(nsd.urn, nsd.esClass);
   }
      
   /**
    * Add translations, where each translation is defined by an actual Class object.
    * We can get both the class name and the package name from the Class object.
    * 
    * @param classes
    */
   private void addTranslations(Class<? extends ElementState>[] classes)
   {
	   if (classes != null)
	   {
		   int numClasses	= classes.length;
		   for (int i=0; i<numClasses; i++)
		   {
			   Class<? extends ElementState> thatClass	= classes[i];
			   addTranslation(thatClass);
		   }
	   }
   }

   /**
    * Utility for composing <code>TranslationSpace</code>s.
    * Performs composition by value. That is, the entries are copied.
    * 
    * Unlike in union(), if there are duplicates, they will override identical entries in this.
    * 
    * @param inheritedTranslations
    */
   private void addTranslations(TranslationSpace inheritedTranslations)
   {
	   if (inheritedTranslations != null)
	   {
		   // copy map entries from inherited maps into new maps
		   updateMapWithValues(inheritedTranslations.entriesByClassSimpleName, entriesByClassSimpleName, "classSimpleName");
		   updateMapWithValues(inheritedTranslations.entriesByTag, entriesByTag, "tagName");
		   
		   HashMap<String, Class<? extends ElementState>> inheritedNameSpaceClassesByURN = inheritedTranslations.nameSpaceClassesByURN;
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
   private void updateMapWithValues(Map<String, TranslationEntry> inheritedMap, Map<String, TranslationEntry> newMap, String warn)
   {
	   for (String key : inheritedMap.keySet())
	   {
		   TranslationEntry translationEntry	= inheritedMap.get(key);
		   updateMapWithEntry(newMap, key, translationEntry, warn);
	   }
   }

   /**
    * Update the Map with the entry.
    * 
    * @param newMap
    * @param key
    * @param translationEntry
    * @param warn
    */
   private void updateMapWithEntry(Map<String, TranslationEntry> newMap, String key, TranslationEntry translationEntry, String warn)
   {
	   TranslationEntry existingEntry	= newMap.get(key);

	   final boolean entryExists		= existingEntry != null;
	   final boolean newEntry			= existingEntry != translationEntry;
	   if (entryExists && newEntry)	// look out for redundant entries
		   warning("Overriding " + warn + " " + key + " with " + translationEntry);

	   if (!entryExists || newEntry)
		   newMap.put(key, translationEntry);
   }
   /**
	* Add a translation table entry for an ElementState derived sub-class.
	* Assumes that the xmlTag can be derived automatically from the className,
	* by translating case-based separators to "_"-based separators.
	* 
	* @param classObj		The object for the class.
	*/
   public void addTranslation(Class<? extends ElementState> classObj)
   {
	   new TranslationEntry(classObj);
   }

   /**
    * Find a prior TranslationEntry for thatClass.
    * Add an alternative tag mapping for it.
    * 
    * @param thatClass
    * @param alternativeXmlTag
    */
   public void addTranslation(Class<? extends ElementState> thatClass, String alternativeXmlTag)
   {
	   String classSimpleName		= thatClass.getSimpleName();
	   TranslationEntry thatEntry	= entriesByClassSimpleName.get(classSimpleName);
	   entriesByTag.put(alternativeXmlTag, thatEntry);
   }
   
   /**
	* Set the default package name for XML tag to ElementState sub-class translations.
	* 
	* @param packageName	The new default package name.
	*/
   private void setDefaultPackageName(String packageName)
   {
	  defaultPackageName	= packageName;
   }
   /**
	* Look-up a <code>Class</code> object for the xmlTag, using translations in this,
	* and in inherited TranslationSpaces.
	* Will use defaultPackage name here and, recursivley, in inherited spaces, as necessary.
	* 
	* @param	xmlTag	XML node name that we're seeking a Class for.
	* @return 			Class object, or null if there is no associated translation.
	*/
   public Class<? extends ElementState>  xmlTagToClass(String xmlTag)
   {
	  TranslationEntry entry = xmlTagToTranslationEntry(xmlTag);
	  return entry.empty ? null : entry.thisClass;
   }

   /**
    * Seek the entry associated with the tag.
    * Recurse through inheritedTranslationSpaces, if necessary.
    * 
    * @param xmlTag
    * @return
    */
   private TranslationEntry xmlTagToTranslationEntry(String xmlTag)
   {
	   TranslationEntry entry		= entriesByTag.get(xmlTag);

	   if (entry == null)
	   {
		   String defaultPackageName	= this.defaultPackageName;
		   if (defaultPackageName != null)
		   {
			   String classSimpleName	= XMLTools.classNameFromElementName(xmlTag);
			   entry					= new TranslationEntry(defaultPackageName, classSimpleName, xmlTag);
			   if (entry.empty)
			   {
				   if (inheritedTranslationSpaces != null)
				   {   // recurse through inherited, continuing to seek a translation
					   for (TranslationSpace inherited : inheritedTranslationSpaces)
					   {
						   entry			= inherited.xmlTagToTranslationEntry(xmlTag);
						   if (entry != null)
						   {   // got one from an inherited TranslationSpace
							   // register translation for the inherited entry in this
							   entriesByTag.put(xmlTag, entry);
							   entriesByClassSimpleName.put(classSimpleName, entry);
							   break;
						   }
					   }
				   }
			   }
		   }
		   else
		   {
			   // empty entry construction added by andruid 11/11/07
			   entry					= new TranslationEntry(xmlTag);	// new empty entry
		   }
	   }
	   return entry;
   }

   /**
    * Get the Class object associated with this tag, if there is one.
    * Unlike xmlTagToClass, this call will not generate a new blank NameEntry.
    * @param tag
    * @return
    */
   Class<? extends ElementState> getClassByTag(String tag)
   {
	   TranslationEntry entry		= entriesByTag.get(tag);
	   
	   return (entry == null) ? null : entry.thisClass;
   }
   /**
    * Get the Class object associated with the provided class name, if there is one.
    * Unlike xmlTagToClass, this call will not generate a new blank NameEntry.
    * 
    * @param classSimpleName	Simple name of the class (no package).
    * @return
    */
   Class<? extends ElementState>  getClassBySimpleName(String classSimpleName)
   {
	   TranslationEntry entry		= entriesByClassSimpleName.get(classSimpleName);
	   
	   return (entry == null) ? null : entry.thisClass;
   }
   /**
    * Use this TranslationSpace to lookup a class that has the same simple name
    * as the argument passed in here. It may have a different full name, that is,
    * a different package, which could be quite convenient for overriding with 
    * subclasses.
    * 
    * @param thatClass
    * @return
    */
   Class<? extends ElementState>  getClassBySimpleNameOfClass(Class<? extends ElementState> thatClass)
   {
	   return getClassBySimpleName(classSimpleName(thatClass));
   }

   /**
    * Derive the XML tag from the Class object, using camel case conversion, or
    * the @xml_tag annotation that may be present in a class declaration.
    * 
    * @param thatClass
    * @return
    */
   private static String determineXMLTag(Class<? extends ElementState>  thatClass)
   {
       return thatClass.isAnnotationPresent(xml_tag.class) ? thatClass.getAnnotation(xml_tag.class).value() : 
         XMLTools.getXmlTagName(thatClass.getSimpleName(), "State");
   }
   
   public class TranslationEntry extends Debug
   {
	   public final String		packageName;
	   /**
	    * Should this be whole class name, or simple/short class name?
	    */
	   public final String		classSimpleName;

	   public final String		tag;

	   public final Class<? extends ElementState>	thisClass;

	   boolean					empty;

	   /**
	    * Construct an empty entry for tag.
	    * This means that the TranslationSpace will map tag to no class forever.
	    * 
	    * @param tag
	    */
	   public TranslationEntry(String tag)
	   {
		   this.tag				= tag;
		   this.packageName		= null;
		   this.classSimpleName	= null;
		   this.thisClass		= null;

		   this.empty			= true;

		   entriesByTag.put(tag, this);
	   }

	   /**
	    * Create the entry, deriving all from its class object, perhaps including an @xml_tag declaration.
	    */
	   TranslationEntry(Class<? extends ElementState> thisClass)
	   {
		   this(thisClass, determineXMLTag(thisClass));
	   }

	   /**
	    * Create the entry using Class object and a tag passed in.
	    * This is used for alternative mappings.
	    */
	   TranslationEntry(Class<? extends ElementState> thisClass, String tag)
	   {
		   this(thisClass.getPackage().getName(), 
				   thisClass.getSimpleName(), 
				   thisClass.getName(),
				   tag, 
				   thisClass);
	   }


	   /**
	    * Form an entry, using packageName, classSimpleName, and tag.
	    * It may turn out to be an empty one -- if class.forName() fails.
	    * 
	    * @param packageName
	    * @param classSimpleName
	    * @param tag
	    */
	   TranslationEntry(String packageName, String classSimpleName, String tag)
	   {
		   this(packageName, classSimpleName, (packageName == null) ? null : packageName + "." + classSimpleName, 
				   tag, null);
	   }

	   @SuppressWarnings("unchecked")
	   TranslationEntry(String packageName, String classSimpleName, String classWholeName,
			   String tag, Class<? extends ElementState> thisClass)
	   {
		   this.packageName		= packageName;
		   /*
		    * changed by andruid 5/5/07 the thinking is that there is only one possible simple class name per tag per
		    * TranslationSpace, so we don't ever need the whole class name. and by ussing the short one, we will be
		    * able to support fancy overriding properly, where you change (override!) the mapping of a simple name,
		    * because the package and whole name are differrent.
		    */
		   this.classSimpleName	= classSimpleName;

		   this.tag				= tag;

		   // look for a class if we dont' have one already
		   if ((thisClass == null) && (classWholeName != null))
		   {
			   try
			   {  
				   thisClass		= (Class<? extends ElementState>) Class.forName(classWholeName);
			   } catch (ClassNotFoundException e)
			   {
				   // maybe we need to use State
				   try
				   {
					   thisClass	= (Class<? extends ElementState>) Class.forName(classWholeName + "State");
				   } catch (ClassNotFoundException e2)
				   {
				   }
			   }
		   }
		   if (thisClass == null)
		   {
			   debug("WARNING: can't find class object, create empty entry.");

			   this.empty		= true;
		   }

		   this.thisClass			= thisClass;
		   registerTranslation(tag, classSimpleName);

		   if (thisClass != null)
		   {
			   ElementState.xml_other_tags otherTagsAnnotation 	= thisClass.getAnnotation(ElementState.xml_other_tags.class);
			   if (otherTagsAnnotation != null)
			   {
				   String[] otherTags	= XMLTools.otherTags(otherTagsAnnotation);
				   for (String otherTag : otherTags)
				   {
					   if ((otherTag != null) && (otherTag.length() > 0))
					   {
						   entriesByTag.put(otherTag, this);
					   }
				   }
			   }
		   }
	   }

	   /**
	    * @param tag
	    * @param classSimpleName
	    */
	   private void registerTranslation(String tag, String classSimpleName)
	   {
		   entriesByTag.put(tag, this);
		   entriesByClassSimpleName.put(classSimpleName, this);
		   /*
		    * i dont see why this is here. it looks wrong. -- andruid 5/6/07.
		if (classSimpleName.endsWith("State"))
		{
			int beforeState		= classSimpleName.length() - 5;
			String wholeClassNameNoState = 
				classSimpleName.substring(0, beforeState);
//			debug("create entry including " + wholeClassNameNoState);
			entriesByClassSimpleName.put(wholeClassNameNoState, this);
		}
		    */
	   }

	   public String getTag()
	   {
		   return tag;
	   }
	   public String toString()
	   {
		   StringBuilder buffy = new StringBuilder(50);
		   buffy.append("NameEntry[").append(classSimpleName).
		   append(" <").append(tag).append('>');
		   if (thisClass != null)
			   buffy.append(' ').append(thisClass);
		   buffy.append(']');
		   return XMLTools.toString(buffy);
	   }
   }

   public String toString()
   {
      return "TranslationSpace[" + name +"]";
   }
   /**
    * Find the TranslationSpace called <code>name</code>, if there is one.
    * 
    * @param name
    * @return
    */
   public static TranslationSpace lookup(String name)
   {
	   return (TranslationSpace) allTranslationSpaces.get(name);
   }
   /**
    * Find the TranslationSpace called <code>name</code>, if there is one.
    * Otherwise, create a new one with this name, and return it.
    * 
    * @param name
    * @return
    */
   public static TranslationSpace get(String name)
   {
	   TranslationSpace result	= lookup(name);
	   return (result != null) ? result : new TranslationSpace(name);
   }
   
   /**
    * Look for a TranslationSpace with this name.
    * If you find one, make sure it also has the same defaultPackageName (internal consistency check).
    * Throw a RuntimeExcpection if the consistency check fails.
    * 
    * @param 	name
    * @return	existing translations
    */
   private static TranslationSpace lookForExistingTS(String name)
   {
	   return lookup(name);
   }
   /**
    * Find the TranslationSpace called <code>name</code>, if there is one.
    * It must also have its defaultPackageName = to that passed in as the 2nd argument.
    * If there is no TranslationSpace with this name, create a new one, and set its defaultPackageName.
    * If there is one, but it has the wrong defaultPackageName, then throw a RuntimeException.
    * 
    * @param name
    * @return
    */
   public static TranslationSpace get(String name, String defaultPackageName)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result	= new TranslationSpace(name, defaultPackageName);
	   }
	   return result;
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * 
    * @param defaultPackageName
    * @param translations
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String defaultPackageName, Class[] translations)
   {
	   TranslationSpace result	= lookForExistingTS(defaultPackageName);
	   if (result == null)
	   {
		   result		= new TranslationSpace(defaultPackageName, translations, defaultPackageName);
	   }
	   return result;	   
   }
   
   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Inherit from the previous TranslationSpace, by including all mappings from there.
    * 
    * @param name
    * @param translations
    * @param inheritedTranslations
    * @param defaultPackageName
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations, TranslationSpace inheritedTranslations,
		   							  String defaultPackageName)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, translations, inheritedTranslations, defaultPackageName);
	   }
	   return result;	   
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a previous TranslationSpace, by including all mappings from there.
    * Add just a single new class.
    * 
    * @param name
    * @param translation
    * @param inheritedTranslations
    * @return
    */
   public static TranslationSpace get(String name, Class<? extends ElementState> translation, TranslationSpace inheritedTranslations)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, translation, inheritedTranslations);
	   }
	   return result;	   
   }
 
   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Add just a single new class.
    * 
    * @param name
    * @param translation
    * @return
    */
   public static TranslationSpace get(String name, Class<? extends ElementState> translation)
   {
	   return get(name, translation, null);
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on the previous TranslationSpace, by including all mappings from there.
    * 
    * @param name
    * @param translations
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations,
		   							  TranslationSpace inheritedTranslations)
   {
	   return get(name, translations, inheritedTranslations, name);
   }
 
   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on the previous TranslationSpace, by including all mappings from there.
    * 
    * @param name
    * @param translations
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations,
			  TranslationSpace... inheritedTranslations)
   {
	   return get(name, translations, inheritedTranslations, (String) null);
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a set of inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
 * @param translations
 * @param inheritedTranslationsSet
 * @param defaultPackageName
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations, TranslationSpace[] inheritedTranslationsSet,
		   							  String defaultPackageName)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, translations, inheritedTranslationsSet, defaultPackageName);
	   }
	   return result;	   
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a set of inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
 * @param translations
 * @param inheritedTranslationsSet
 * @param defaultPackageName
 * @param nameSpaceDecls				Array of ElementState class + URI key map entries for handling XML Namespaces.
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations, TranslationSpace[] inheritedTranslationsSet,
		   String defaultPackageName, NameSpaceDecl[] nameSpaceDecls)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, translations, inheritedTranslationsSet, defaultPackageName, nameSpaceDecls);
	   }
	   return result;	   
   }
   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a set of inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
    * @param defaultPackageName
    * @param inheritedTranslationsSet
    * @return
    */
   public static TranslationSpace get(String name, TranslationSpace[] inheritedTranslations, String defaultPackageName)
   {
	   return get(name, null, inheritedTranslations, defaultPackageName);	   
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a set of inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
    * @param inheritedTranslations
    * @return
    */
   public static TranslationSpace get(String name, TranslationSpace[] inheritedTranslations)
   {
	   return get(name, null, inheritedTranslations, null);	   
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on an inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
    * @param inheritedTranslations
    * @param defaultPackageName
    * @return
    */
   public static TranslationSpace get(String name, TranslationSpace inheritedTranslations, String defaultPackageName)
   {
	   return get(name, null, inheritedTranslations, defaultPackageName);	   
   }

   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a set of inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
    * @param defaultPackageName
    * @param inheritedTranslationsSet
    * @param translations
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, String defaultPackageName, ArrayList<TranslationSpace> inheritedTranslationsSet,
		   							  Class[] translations)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, translations, inheritedTranslationsSet, defaultPackageName);
	   }
	   return result;	   
   }
   /**
    * Find an existing TranslationSpace by this name, or create a new one.
    * Build on a set of inherited TranslationSpaces, by including all mappings from them.
    * 
    * @param name
 * @param translations
 * @param inheritedTranslationsSet
    * @return
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations,
		   							  ArrayList<TranslationSpace> inheritedTranslationsSet)
   {
	   TranslationSpace result	= lookForExistingTS(name);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, translations, inheritedTranslationsSet, name);
	   }
	   return result;	   
   }
   /**
    * Find the TranslationSpace called <code>name</code>, if there is one.
    * It must also have its defaultPackageName = to that passed in as the 2nd argument.
    * If there is no TranslationSpace with this name, create a new one, and set its defaultPackageName.
    * If there is one, but it has the wrong defaultPackageName, then throw a RuntimeException.
    * 
    * Add the translations to the TranslationSpace.
    * 
    * @param name
    * @return Either an existing or new TranslationSpace, with this defaultPackageName, and these translations.
    * A RuntimeException will be thrown if there was already such a TranslationSpace, but with different defaultPackageName.
    */
   @SuppressWarnings("unchecked")
   public static TranslationSpace get(String name, Class[] translations, String defaultPackageName)
   {
	  TranslationSpace result	= lookForExistingTS(name);
	  if (result == null)
	  {
		  result		= new TranslationSpace(name, translations, defaultPackageName);
	  }
	  return result;
   }

   protected HashMap<String, TranslationEntry> entriesByClassName()
   {
	   return entriesByClassSimpleName;
   }
   
	/**
	 * Get the Scalar Type corresponding to the Class.
	 * 
	 * @param thatClass
	 * @return	Type associated with thatClass
	 */
   <U> ScalarType<U> getType(Class<U> thatClass)
   {
	   return TypeRegistry.getType(thatClass);
   }
   	
   /**
    * Lookup a NameSpace ElementState subclass, with a URN as the key.
    * 
    * @param urn
    * @return
    */
   public Class<? extends ElementState> lookupNameSpaceByURN(String urn)
   {
	   return nameSpaceClassesByURN.get(urn);
   }

   /**
    * 
    * @return	The unique name of this.
    */
   public String getName()
   {
	   return name;
   }

}
