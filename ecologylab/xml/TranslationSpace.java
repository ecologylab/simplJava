package ecologylab.xml;

import java.util.HashMap;
import java.util.Iterator;

import ecologylab.generic.Debug;

/**
 * A set of translations between XML element names (tags) and associated Java ElementState
 * class names.
 * This is done by maintaining a HashMap containing
 * the name of the class as the key and its package as the value. If no entry is 
 * present in the map, then the default package is returned. This is used when
 * an XML is converted back to its Java State-Object
 */
public class TranslationSpace extends Debug
{
   protected String			name;
   /**
	* The default package. If an entry for a class is not found in the hashtable,
	* this package name is returned.
	*/
   private String			defaultPackageName	= "cf.state";
   
   /**
	* This boolean controls whether package names are added to the class names
	* in the corresponding XML file. If its false, the package names are not added, otherwise
	* every class name is prepended by its package name.
	*/
   private boolean			emitPackageNames	= false;
   
   private HashMap<String, NameEntry>			entriesByClassName	= new HashMap<String, NameEntry>();
   private HashMap<String, NameEntry>			entriesByTag		= new HashMap<String, NameEntry>();
   
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
    * @param baseTranslations
    */
   protected TranslationSpace(String name, TranslationSpace baseTranslations)
   {
	  this(name);
	  addTranslations(baseTranslations);
   }
   
   /**
    * Create a new space that defines how to translate xml tag names into
    * class names of subclasses of ElementState.
    * 
    * Set a new default packge.
    * 
    * @param name
    * @param defaultPackgeName
    */
   protected TranslationSpace(String name, String defaultPackgeName)
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
    * @param defaultPackgeName
	* @param translations		Set of initially defined translations for this.
    */
   protected TranslationSpace(String name, String defaultPackgeName, 
					String[][] translations)
   {
	   this(name);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
   }
   
   /**
    * Construct a new TranslationSpace, with this name, using the baseTranslations first.
    * Then, add the array of translations, then, make the defaultPackageName available.
    * 
    * @param name
    * @param defaultPackgeName
    * @param baseTranslations
    * @param translations
    */
   protected TranslationSpace(String name, String defaultPackgeName, TranslationSpace baseTranslations,
		   String[][] translations)
   {
	   this(name, baseTranslations);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
   }
   
   /**
    * Construct a new TranslationSpace, with this name, using the baseTranslations first.
    * Then, add the array of translations, then, make the defaultPackageName available.
    * 
    * @param name
    * @param defaultPackgeName
    * @param baseTranslations
    * @param translations
    */
   protected TranslationSpace(String name, String defaultPackgeName, TranslationSpace baseTranslations,
		   Class[] translations)
   {
	   this(name, baseTranslations);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
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
    * @param defaultPackgeName
	* @param translations		Set of initially defined translations for this.
    */
   protected TranslationSpace(String name, String defaultPackgeName, 
					Object[] translations)
   {
	   this(name);
	   this.setDefaultPackageName(defaultPackgeName);
	   addTranslations(translations);
   }
   
   /**
    * Create a new TranslationSpace, with the same name and default package name, and
	* a set of defined translations.
    * 
    * @param defaultPackgeName		Name of the TranslationSpace to be created --
	*					A key for use in the TranslationSpace registry.
    * 					Also the defaultPackgeName for translations using this.
    * 
	* @param translations		Set of initially defined translations for this.
    */
   public TranslationSpace(String defaultPackgeName,
					String[][] translations)
   {
	   this(defaultPackgeName, defaultPackgeName, translations);
   }
/**
 * Add a set of  translation table entry for an ElementState derived sub-class.
 * Assumes that the xmlTag can be derived automatically from the className,
 * by translating case-based separators to "_"-based separators.
 * 
 * @param translations		Set of new translations.
 */
   public void addTranslations(String[][] translations)
   {
	  if (translations != null)
	  {
		  int		numTranslations	= translations.length;
		  for (int i=0; i< numTranslations; i++)
		  {
			 String[] thatTranslation		= translations[i];
			 addTranslation(thatTranslation[0], thatTranslation[1]);
		  }
	  }
   }
   
   /**
    * Add translations, where each translation is defined by an actual Class object.
    * We can get both the class name and the package name from the Class object.
    * 
    * @param classes
    */
   public void addTranslations(Class[] classes)
   {
	   if (classes != null)
	   {
		   int numClasses	= classes.length;
		   for (int i=0; i<numClasses; i++)
		   {
			   Class thatClass	= classes[i];
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
    * @param otherTranslations
    */
   public void addTranslations(TranslationSpace otherTranslations)
   {
	   if (otherTranslations != null)
	   {
		   Iterator translationEntriesIterator = otherTranslations.entriesByClassIterator();
		   while (translationEntriesIterator.hasNext())
		   {
			   NameEntry nameEntry = (NameEntry) translationEntriesIterator.next();
			   addTranslation(nameEntry.classObj);
			   if (!entriesByClassName().containsKey(nameEntry.className))	// look out for redundant entries
				   debug("WARNING: overriding with " + nameEntry);
		   }
	   }
   }
   /**
    * Add translations, where each translation can be heterogeneously either a Class object,
    * or an array with 2 String elements, package name and class name.
    * 
    * @param translations
    */
   public void addTranslations(Object[] translations)
   {
	   if (translations != null)
	   {
		   int		numTranslations	= translations.length;
		   for (int i=0; i< numTranslations; i++)
		   {
			   Object thatTranslation		= translations[i];
			   if (thatTranslation instanceof String[])
			   {
				   String[] thatStringTrans	= (String[]) thatTranslation;
				   addTranslation(thatStringTrans[0], thatStringTrans[1]);
			   }
			   else if (thatTranslation instanceof Class)
			   {
				   addTranslation((Class) thatTranslation);
			   }
			  }
		  }
	   }

   /**
	* Add a translation table entry for an ElementState derived sub-class.
	* Assumes that the xmlTag can be derived automatically from the className,
	* by translating case-based separators to "_"-based separators.
	* 
	* @param packageName	Package that the class lives in.
	* @param className		Name of the class.
	*/
   public void addTranslation(String packageName, String className)
   {
   	  new NameEntry(packageName, className);
   }
   /**
	* Add a translation table entry for an ElementState derived sub-class.
	* Assumes that the xmlTag can be derived automatically from the className,
	* by translating case-based separators to "_"-based separators.
	* 
	* @param classObj		The object for the class.
	*/
  public void addTranslation(Class classObj)
  {
  	  new NameEntry(classObj.getPackage().getName(), classObj);
  }

   /**
	* Add a translation table entry for an ElementState derived sub-class.
	* Use this signature when the xmlTag cannot be generated automatically from the className.
	* 
	* @param packageName	Package that the class lives in.
	* @param className		Name of the class.
	* @param xmlTag		XML tag that the class maps to.
	*/
   public void addTranslation(String packageName, String className,
							  String xmlTag)
   {
//	  debugA("addTranslation: "+ className " : " + packageName);
	  new NameEntry(packageName, className, xmlTag, null);
   }
   /**
	* Set the default package name for XML tag to ElementState sub-class translations.
	* 
	* @param packageName	The new default package name.
	*/
   public void setDefaultPackageName(String packageName)
   {
	  defaultPackageName	= packageName;
   }
   /**
	* creates a <code>Class</code> object from a given element name (aka tag) in the xml.
	* Also keeps it in the hashtable, so that when requested for the same class again
	* it doesnt have to create one.
	* @param xmlTag	name of the state class along with its package name
	* @return 						a <code>Class</code> object for the given state class
	*/
   public Class xmlTagToClass(String xmlTag)
   {
	  NameEntry entry		= (NameEntry) entriesByTag.get(xmlTag);

	  if (entry == null)
	  {
		 String className	= XmlTools.classNameFromElementName(xmlTag);
		 String packageName = defaultPackageName;
		 entry				= new NameEntry(packageName, className, xmlTag, null);
	  }
	  else if (entry.empty)
	  {
//		 debug("using memorized no mapping for " + xmlTag);
		 return null;
	  }
	  return entry.classObj;
   }
   /**
    * Get the Class object associated with this tag, if there is one.
    * Unlike xmlTagToClass, this call will not generate a new blank NameEntry.
    * @param tag
    * @return
    */
   Class getClassByTag(String tag)
   {
	   NameEntry entry		= (NameEntry) entriesByTag.get(tag);
	   
	   return (entry == null) ? null : entry.classObj;
   }
/**
 * Find an appropriate XML tag name, based on the type of the object passed.
 * 
 * @param object	Object whose type becomes the basis for the tag name we derive.
 */   
   public String objectToXmlTag(Object object)
   {
	  return classToXmlTag(object.getClass());
   }
   /**
    * Find an appropriate XML tag name, based on the class object passed.
    * 
    * @param classObj	The type which becomes the basis for the tag name we derive.
    */   
   public String classToXmlTag(Class classObj)
   {
	  String className	= classObj.getName();
	  NameEntry entry	= (NameEntry) entriesByClassName.get(className);
	  if (entry == null)
	  {
	  	 synchronized (this) 
	  	 {
	  	 	 entry	= (NameEntry) entriesByClassName.get(className);
	  	 	 if (entry == null)
	  	 	 {
				 String packageName = classObj.getPackage().getName();
				 int index			= className.lastIndexOf('.') + 1;
				 className			= className.substring(index);
				 entry				= new NameEntry(packageName, className);
	  	 	 }
	  	 }
	  }
	  return entry.getTag();
   }
   
   /**
	* @return	true	If package names should be emitted as part of tags 
	*  while translating to XML.
	*					This corresponds to quite verbose XML.
	*/
   public boolean emitPackageNames()
   {
	  return emitPackageNames;
   }
   
   /**
    * Combine translations from newTranslationSpace into this one.
    * 
    * @param newTranslationSpace
    */
   //TODO should this create a new TranslationSpace or add to the existing one???
   public TranslationSpace union(TranslationSpace newTranslationSpace)
   {
	   if (newTranslationSpace == null)
	   {
		   error("Can't union with null newTranslationSpace.");
		   return null;
	   }
	   Iterator translationEntriesIterator = newTranslationSpace.entriesByClassIterator();
	   while (translationEntriesIterator.hasNext())
	   {
		   NameEntry nameEntry = (NameEntry) translationEntriesIterator.next();
		   if (!entriesByClassName().containsKey(nameEntry.className))	// look out for redundant entries
			   addTranslation(nameEntry.classObj);
		   else
			   debug("WARNING: union() not overriding " + nameEntry);
	   }
	   return this;
   }
   /**
    * Get the values in the entriesByClass HashMap, and form an Iterator for acessing them.
    * 
    * @return
    */
   private Iterator entriesByClassIterator()
   {
	   return entriesByClassName().values().iterator();
   }

   public class NameEntry extends Debug
   {
	  public final String		packageName;
	  public final String		className;
	  public final String		tag;

	  public final String		dottedPackageName;
	  public final String		tagWithPackage;
	  public final Class		classObj;

	  boolean					empty;
	  
	  /**
	   * Create the entry by package name and class name.
	   */
	  	  public NameEntry(String packageName, String className)
	  	  {
	  		 this(packageName, className,
	  			  XmlTools.getXmlTagName(className, "State", false), null);
	  	  }
	  	  /**
	  	   * Create the entry by package name and class name.
	  	   */
	  	  public NameEntry(String packageName, Class classObj)
	  	  {
	  		  this(packageName, getClassName(classObj),
	  				  XmlTools.getXmlTagName(getClassName(classObj), "State", false), classObj);
	  	  }
	  public NameEntry(String packageName, String className, 
					   String tag, Class classObj)
	  {
	  	 String wholeClassName	= packageName + "." + className;
		 this.packageName		= packageName;
		 this.className			= wholeClassName;
		 this.tag				= tag;
		 String dottedPackageName		= packageName + ".";
		 this.dottedPackageName	= dottedPackageName;
		 this.tagWithPackage	= dottedPackageName + tag;
		 if (classObj == null)
		 {
			 try
			 {  
				classObj			= Class.forName(wholeClassName);
			 } catch (ClassNotFoundException e)
			 {
				// maybe we need to use State
				try
				{
				   //debug("trying " + wholeClassName+"State");
				   classObj			= Class.forName(wholeClassName+"State");
				} catch (ClassNotFoundException e2)
				{
				   debug("WARNING: can't find class object, create empty entry.");
				   //Thread.dumpStack();
	
	//			   this.classObj			= classObj;
				   this.empty				= true;
	//			   return;
				}
			 }
		 }
		 this.classObj			= classObj;

		 entriesByTag.put(tag, this);
		 entriesByClassName.put(wholeClassName, this);
		 if (wholeClassName.endsWith("State"))
		 {
			int beforeState		= wholeClassName.length() - 5;
			String wholeClassNameNoState = 
			   wholeClassName.substring(0, beforeState);
//			debug("create entry including " + wholeClassNameNoState);
			entriesByClassName.put(wholeClassNameNoState, this);
		 }
//		 else
//			debug("create entry");
	  }
	  public String getTag()
	  {
		 return emitPackageNames() ? tagWithPackage : tag;
	  }
	  public String toString()
	  {
		  StringBuilder buffy = new StringBuilder(50);
		 buffy.append("NameEntry[").append(className).
			append(" <").append(tag).append('>');
		 if (classObj != null)
			buffy.append(' ').append(classObj);
		 buffy.append(']');
		 return XmlTools.toString(buffy);
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
   
   private static TranslationSpace lookupAndCheckDefaultPackage(String name, String defaultPackageName)
   {
	   TranslationSpace result	= lookup(name);
	   if (result != null)
	   {
		  if (defaultPackageName != null)
		  {
			 String resultDefaultPackageName = result.defaultPackageName;
			 if (!resultDefaultPackageName.equals(defaultPackageName))
				throw new RuntimeException("TranslationSpace ERROR: Existing TranslationSpace " + name +
					   " has defaultPackageName="+resultDefaultPackageName +", not " +defaultPackageName);
		  }
	   }
	   else
		   println("Returning existing TranslationSpace; " + name);
	   return result;
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
	   TranslationSpace result	= lookupAndCheckDefaultPackage(name, defaultPackageName);
	   if (result == null)
	   {
		   result	= new TranslationSpace(name, defaultPackageName);
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
   public static TranslationSpace get(String name, String defaultPackageName,
							   String[][] translations)
   {
	   TranslationSpace result	= lookupAndCheckDefaultPackage(name, defaultPackageName);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, defaultPackageName, translations);
	   }
	   return result;
   }
   
   public static TranslationSpace get(String name, String defaultPackageName, TranslationSpace baseTranslations,
		   Class[] translations)
   {
	   TranslationSpace result	= lookupAndCheckDefaultPackage(name, defaultPackageName);
	   if (result == null)
	   {
		   result		= new TranslationSpace(name, defaultPackageName, baseTranslations, translations);
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
   public static TranslationSpace get(String name, String defaultPackageName,
							   Class[] translations)
   {
	  TranslationSpace result	= lookupAndCheckDefaultPackage(name, defaultPackageName);
	  if (result == null)
	  {
		  result		= new TranslationSpace(name, defaultPackageName, translations);
	  }
	  return result;
   }

   /**
    * Find the TranslationSpace called <code>defaultPackageName</code>, if there is
	* one. If there is no TranslationSpace with this name, create a new one, and 
	* set its defaultPackageName.
	* 
    * Add the translations to the TranslationSpace.
    * 
    * @param 	translations	An array of tag name to class name entries.
    *
    * @return	A new TranslationSpace, or the existing one with the name defaultPackageName, and these translations.
    */
   public static TranslationSpace get(String defaultPackageName,
							   String[][] translations)
   {
	  return get(defaultPackageName, defaultPackageName, translations);
   }
   
   protected HashMap entriesByClassName()
   {
	   return entriesByClassName;
   }
}
