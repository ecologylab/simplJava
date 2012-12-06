package ecologylab.logging;

//import org.apache.log4j.NDC;
//import org.apache.log4j.PropertyConfigurator;

public class BasicLoggerNames {

	//can later be divided into fundamental, semantics and so on.	
	public static String 	baseLogger			=		"BaseLogger";
	
	public static String 	htmlCacheLogger		=		"HTMLCacheLogger";
	
	public static String 	metadataCacheLogger	=		"MetadataCacheLogger";
//	
//	private static String	configurationFile	=		"/log4j.configuration";
//	
//	static {
//		PropertyConfigurator.configure(BaseLogger.class
//									.getResourceAsStream(configurationFile));
//	}
//	
//	public static void pushContext(String arg)
//	{
//		NDC.push(arg);
//	}
//	
//	public static void removeContext()
//	{
//		NDC.remove();
//	}
}
