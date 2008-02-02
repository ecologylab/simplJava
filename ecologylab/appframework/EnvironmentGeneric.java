/**
 * 
 */
package ecologylab.appframework;

import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.net.ParsedURL;

/**
 * @author andruid
 *
 */
public class EnvironmentGeneric extends Generic
{

	protected static ParsedURL configDir;

	public static ParsedURL codeBase()
	   {
		   return Environment.the.get().codeBase();
	   }

	/**
	    * The config directory, as located relative to the codebase, with the jar file and perhaps the sources.
	    * This is where the tree of configuration assets is rooted.
	    * Examples of configuration assets include interface graphics, interface semantic descriptions (xml),
	    * dictionary, ...
	    * These are files that don't change often, and are needed for an application.
	    * 
	    * @return
	    */
	   public static ParsedURL configDir()
	   {
		   ParsedURL result	= configDir;
		   if (result == null)
		   {
			   result		= ParsedURL.getRelativeToCodeBase("config/", "Error forming config dir.");
			   configDir	= result;
		   }
		   return result;
	   }

	public static ParsedURL docBase()
	   {
	      return Environment.the.get().docBase();
	   }

	/**
	    * Obtain a path relative to the configDir().
	    * 
	    * @param relativePath
	    * @return
	    */
	   public static ParsedURL configPath(String relativePath)
	   {
		   return configDir().getRelative(relativePath, "Error forming config directory path.");
	   }

	/**
	    * @return	The version of Java we're using (but not the specific release),
	    *		as in 1.2, 1.3, 1.4,...
	    */
	   public static float javaVersion()
	   {
	      return Environment.the.javaVersion();
	   }

	/**
	    * Check to see if we're running on what we consider to be a decent, usable version of Java.
	    * For 1.5, this means rel 4 or more; for 1.4, it means 1.42_04 or more.
	    * 
	    * @return	true if the Java we're running on is good; false if its crap.
	    */
	   public static boolean hasGoodJava()
	   {
		   return Environment.the.hasGoodJava();
	   }

	/**
	    * @return The version of Java we're using (with the specific release)
	    */
	   public static String javaVersionFull()
	   {
		   return System.getProperty("java.version");
	   }

	public static boolean hasXML()
	   {
	      return Environment.the.hasXML();
	   }

	/**
	    * Checks what platform we're on, and returns a suitable PURL that you would navigate to,
	    * to download the current Java.
	    * 
	    * @return	PURL of www.java.com, or www.apple.com/java.
	    */
	   public static ParsedURL downloadJavaPURL()
	   {
		   ParsedURL result;
		   switch (PropertiesAndDirectories.os())
		   {
		   case PropertiesAndDirectories.MAC:
			   result	= Generic.MAC_JAVA_PURL;
		   case PropertiesAndDirectories.WINDOWS:
		   case PropertiesAndDirectories.LINUX:
		   default:
			   result	= Generic.SUN_JAVA_PURL;
		   }
		   return result;
	   }

	public static void status(String msg)
	   {
	      Environment.the.get().status(msg);
	   }

	/**
	    * Open a document in a web browser.
	    * 
	    * @param purl	The address of the web document.
	    */
	   public static void navigate(ParsedURL purl)
	   {
	      Environment.the.get().navigate(purl, Environment.the.frame());
	   }


}
