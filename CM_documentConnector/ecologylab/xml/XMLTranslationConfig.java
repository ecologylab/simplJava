package ecologylab.xml;

import java.util.*;

/**
 * This class contains the package information for the classes that are converted
 * from Java to XML and back. This is done by maintaining a Hashtable containing
 * the name of the class as the key and its package as the value. If no entry is 
 * present in the hashtable, then the default package is returned. This is used when
 * an XML is converted back to its Java State-Object
 */
public class XMLTranslationConfig extends ElementState 
{
	/**
	 * The default package. If an entry for a class is not found in the hashtable,
	 * this package name is returned.
	 */
	public final static String defaultPackage = "cm.state";
	
	/**
	 * This boolean controls whether package names are added to the class names
	 * in the corresponding XML file. If its false, the package names are not added, otherwise
	 * every class name is prepended by its package name.
	 */
	public final static boolean addPackageNames = false;

	/**
	 * The hashtable containing the class name to package name mapping.
	 * the name of the class is the key and the name of package is the value.
	 */
	public static Hashtable classPackageMappings = new Hashtable();
	
	static	
	{
		classPackageMappings.put("KeyframeInfo", "cm.history");
		classPackageMappings.put("Keyframe", "cm.history");
		classPackageMappings.put("KeyframesIndexSet", "cm.history");
		classPackageMappings.put("KeyframeTimeStampSet", "cm.history");
		classPackageMappings.put("MemoryMapInfo", "cm.history");
		classPackageMappings.put("OperationInfo", "cm.history");
		classPackageMappings.put("OperationsIndexSet", "cm.history");
		classPackageMappings.put("OperationsTimeStampSet", "cm.history");
		classPackageMappings.put("TimeStamp", "cm.history");
		classPackageMappings.put("Subject", "studies");
		classPackageMappings.put("SubjectSet", "studies");
	}
	
	/**
	 * returns a package name for a corresponding class name
	 * @param className	the class for which the package names needs to be found out.
	 * @return	package name of the given class name
	 */
	public static String getPackageName(String className)
	{
		String packageName = (String)classPackageMappings.get(className);
		
		if(packageName != null)
		{
			return packageName + ".";
		}
		else
		{
			return defaultPackage + ".";
		}
	}
}