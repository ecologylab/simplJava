package ecologylab.xml;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * This class contains the package information for the classes that are converted
 * from Java to XML and back. This is done by maintaining a Hashtable containing
 * the name of the class as the key and its package as the value. If no entry is 
 * present in the hashtable, then the default package is returned. This is used when
 * an XML is converted back to its Java State-Object
 */
public class NameSpace extends IO 
{
	/**
	 * The default package. If an entry for a class is not found in the hashtable,
	 * this package name is returned.
	 */
	private String defaultPackageName = "cm.state.";
	
	/**
	 * This boolean controls whether package names are added to the class names
	 * in the corresponding XML file. If its false, the package names are not added, otherwise
	 * every class name is prepended by its package name.
	 */
	private boolean emitPackageNames = false;
	
	private Hashtable stateClasses	=	new Hashtable();
	
	/**
	 * The hashtable containing the class name to package name mapping.
	 * the name of the class is the key and the name of package is the value.
	 */
	private static final HashMap classPackageMappings = new HashMap();
	
	public NameSpace()	
	{
		// !!! these lines need to be moved to the studies package !!!
		addTranslation("studies", "Subject");
		addTranslation("studies", "SubjectSet");
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
	   addTranslation(packageName, className, 
					  XmlTools.xmlTagFromClassName(className, null,
												"State", false));
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
		classPackageMappings.put(className, packageName+".");
	}
	/**
	 * returns a package name for a corresponding class name
	 * @param className	the class for which the package names needs to be found out.
	 * @return	package name of the given class name
	 */
	public String getPackageName(String className)
	{
		String packageName = (String) classPackageMappings.get(className);
		
		return (packageName != null) ? packageName : defaultPackageName;
	}
	
	/**
	 * Set the default package name for XML tag to ElementState sub-class translations.
	 * 
	 * @param packageName	The new default package name.
	 */
	public void setDefaultPackageName(String packageName)
	{
		defaultPackageName	= packageName + ".";
	}
	/**
	    * creates a <code>Class</code> object from a given element name (aka tag) in the xml.
	    * Also keeps it in the hashtable, so that when requested for the same class again
	    * it doesnt have to create one.
	    * @param xmlTag	name of the state class along with its package name
	    * @return 						a <code>Class</code> object for the given state class
	    */
	   public Class xmlTagToElementStateClass(String xmlTag)
	   {
	   		Class stateClass 	= 	null;
	   		String stateName	=	xmlTag;
			String packageName	=	"";
	   		
			if (emitPackageNames)
	   		{
	   			int packageNameIndex=	xmlTag.indexOf("-");
		   		if(packageNameIndex != -1)
		   		{
		   			packageName += 	xmlTag.substring(0, packageNameIndex) + ".";
		   			stateName	=   xmlTag.substring(packageNameIndex+1); 		
		   		}
	   		}
	//		String packageName	=	NameSpace.getFullName(stateName);		  		
	   		stateClass			=	(Class) stateClasses.get(stateName);   	   		
	   		   		  		   		
	   		if (stateClass == null)
	   		{
				String className= XmlTools.classNameFromElementName(stateName);
				if (!emitPackageNames)
				{
					packageName = getPackageName(className);
					className	= packageName + className;
				}
				else
				{
					className	= packageName;   						
					className  += XmlTools.classNameFromElementName(stateName);
				}
				try
				{				
					stateClass	= Class.forName(className + "State");		
					stateClasses.put(stateName,stateClass);				
				}
				catch (Exception e1)
				{
					try
					{
						stateClass	=	Class.forName(className);
						stateClasses.put(stateName, stateClass);
					}
					catch (Exception e2)
					{
						e2.printStackTrace();
					}
				}
	   		}
			return stateClass;
	   }
    /**
	 * @return	true if package names should be emitted as part of tags while translating to XML.
	 * 			This corresponds to quite verbose XML.
	 */
	public boolean emitPackageNames()
	   {
	   		return emitPackageNames;
	   }
}
