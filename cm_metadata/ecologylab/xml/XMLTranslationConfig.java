package ecologylab.xml;

import java.util.*;

public class XMLTranslationConfig extends ElementState 
{
	public final static String defaultPackage = "cm.state";

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
	}
	
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