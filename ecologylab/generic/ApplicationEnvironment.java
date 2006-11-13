package ecologylab.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import ecologylab.generic.AssetsCache.Assets;
import ecologylab.net.ParsedURL;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.services.messages.PreferencesSet;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * An instance of Environment, which is an application, rather than an applet,
 * or a servlet.
 * The Environment mechanism is used to enable the provision of contextual
 * runtime configuration parameter services in a way that is 
 * independent of the deployment structure.
 * 
 * @author Andruid
 */
public class ApplicationEnvironment
extends Debug
implements Environment
{
	protected static final String PREFERENCES_SUBDIR_PATH		= "config/preferences/";

//	private static final String BASE_PREFERENCE_PATH = PREFERENCES_SUBDIR_PATH+"preferences.txt";
	private static final String BASE_PREFERENCE_PATH = PREFERENCES_SUBDIR_PATH+"preferences.xml";

	/**
	 * Holds preferences for use in servicing parameter(String) requests.
	 */
	private static ObjectRegistry	preferencesRegistry	= new ObjectRegistry();
	
	/**
	 * Used for forming codeBase relative ParsedURLs.
	 * A simulation of the property available in applets.
	 * The codebase is the address where the java code comes from.
	 */
	ParsedURL	codeBase;
	/**
	 * Used for forming codeBase relative ParsedURLs.
	 * A simulation of the property available in applets.
	 * The docbase is the address where the launching HTML file comes from.
	 */
	ParsedURL	docBase;

	
/**
 * Create an ApplicationEnvironment. Create an empty properties object for application parameters.
 * <p/>
 * @deprecated		This constructor is not recommended!
 * 					Build your application to extend this class.
 * 					Use one of the other constructors.
 */
	public ApplicationEnvironment()
	{
	   Environment.the.set(this);
	}
	/**
	 * Create an ApplicationEnvironment. Create an empty properties object for application parameters.
	 * <p/>
	 * No command line argument is processed. 
	 * Only default preferences are loaded, and processed with the default TranslationSpace.
	 *  
	 * @param applicationName	Name of the application. Used to set the applicationName, applicationDir, and .
	 */
	public ApplicationEnvironment(String applicationName)
	{
	   this(null, applicationName, null, null, null);
	}
	
	/**
	 * Create an ApplicationEnvironment.
	 * Load preferences from XML file founds in the config/preferences directory.
	 * Default preferences will be loaded from preferences.xml.
	 * If there is a 0th command line argument, that is the name of an additional
	 * preferences file.
	 *  
	 * @param applicationName
	 * @param translationSpace		TranslationSpace used for translating preferences XML.
	 * 								If this is null, 
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * 								will be used.
	 * @param args					Command line argument array, from public static void main(String[]).
	 */
	public ApplicationEnvironment(String applicationName, TranslationSpace translationSpace, String args[])
	{
	   this(null, applicationName, translationSpace, args);
	}
	/**
	 * Create an ApplicationEnvironment.
	 * Load preferences from XML files found in the config/preferences directory.
	 * Default preferences will be loaded from preferences.xml.
	 * If there is a 0th command line argument, that is the name of an additional
	 * preferences file.
	 * <p/>
	 * The default TranslationSpace, from
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * will be used.
	 *  
	 * @param applicationName
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * 								will be used.
	 * @param args					Command line argument array, from public static void main(String[]).
	 */
	public ApplicationEnvironment(String applicationName, String args[])
	{
	   this(applicationName, (TranslationSpace) null, args);
	}
	/**
	 * Create an ApplicationEnvironment.
	 * Get the base for finding the path to the "codeBase" by using the
	 * package path of the baseClass passed in.
	 * <p/>
	 * Load preferences from XML file founds in the codeBase/config/preferences directory.
	 * Default preferences will be loaded from preferences.xml.
	 * If there is a 0th command line argument, that is the name of an additional
	 * preferences file.
	 * <p/>
	 * Also, sets the Assets cacheRoot to the applicationDir().
	 *  
	 * @param baseClass				Used for computing codeBase property.
	 * @param applicationName
	 * @param translationSpace		TranslationSpace used for translating preferences XML.
	 * 								If this is null, 
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * 								will be used.
	 * @param args				The following command line parameters are recognized:
	 * 								0: parameter file name (found in user.dir/config/)
	 * 								1: graphics_device (screen number)
	 * 								2: screen_size (used in TopLevel --
	 * 									1 - quarter; 2 - almost half; 3; near full; 4 full)
	 */
	public ApplicationEnvironment(Class baseClass, String applicationName, 
			TranslationSpace translationSpace, String args[])
	{
	   this(baseClass, applicationName, preferencesFileRelativeFromArg0(args), translationSpace,
			   (((args == null) || (args.length < 2)) ? null : args[1]),
			   (((args == null) || (args.length < 3)) ? null : args[2]));
	}
	/**
	 * Create an ApplicationEnvironment.
	 * Get the base for finding the path to the "codeBase" by using the
	 * package path of the baseClass passed in.
	 * <p/>
	 * Load preferences from XML file founds in the codeBase/config/preferences directory.
	 * Default preferences will be loaded from preferences.xml.
	 * If there is a 0th command line argument, that is the name of an additional
	 * preferences file.
	 * <p/>
	 * Also, sets the Assets cacheRoot to the applicationDir().
	 * <p/>
	 * The default TranslationSpace, from
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * will be used.
	 *  
	 * @param baseClass				Used for computing codeBase property.
	 * @param applicationName
	 * @param args				The following command line parameters are recognized:
	 * 								0: parameter file name (found in user.dir/config/)
	 * 								1: graphics_device (screen number)
	 * 								2: screen_size (used in TopLevel --
	 * 									1 - quarter; 2 - almost half; 3; near full; 4 full)
	 */
	public ApplicationEnvironment(Class baseClass, String applicationName, String args[])
	{
	   this(baseClass, applicationName, null, args);
	}
	/**
	 * Create an ApplicationEnvironment.
	 * Get the base for finding the path to the "codeBase" by using the
	 * package path of the baseClass passed in.
	 * <p/>
	 * Load preferences from XML file founds in the codeBase/config/preferences directory.
	 * Default preferences will be loaded from preferences.xml.
	 * If there is a 0th command line argument, that is the name of an additional
	 * preferences file.
	 * <p/>
	 * Also, sets the Assets cacheRoot to the applicationDir().
	 * <p/>
	 * The default TranslationSpace, from
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * will be used.
	 * 
	 * @param baseClass			Used for computing codeBase property.
	 * @param propertiesFileRelativePath	Path to the properties file, relative to codeBase().
	 * @param graphicsDev		graphics_device (screen number) to display window. count from 0.
	 * @param screenSize		used in TopLevel --
	 * 								1 - quarter; 2 - almost half; 3; near full; 4 full
	 */
	public ApplicationEnvironment(Class baseClass, String applicationName, String propertiesFileRelativePath, 
			 String graphicsDev, String screenSize) 
	{
		this(baseClass, applicationName, propertiesFileRelativePath, 
			 null, graphicsDev,  screenSize);
	}
	/**
	 * Create an ApplicationEnvironment.
	 * Get the base for finding the path to the "codeBase" by using the
	 * package path of the baseClass passed in.
	 * <p/>
	 * Load preferences from XML file founds in the codeBase/config/preferences directory.
	 * Default preferences will be loaded from preferences.xml.
	 * If there is a 0th command line argument, that is the name of an additional
	 * preferences file.
	 * <p/>
	 * Also, sets the Assets cacheRoot to the applicationDir().
	 * <p/>
	 * The default TranslationSpace, from
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * will be used.
	 * 
	 * @param baseClass			Used for computing codeBase property.
	 * @param applicationName	Name of the application.
	 * @param propertiesFileRelativePath	Path to the properties file, relative to codeBase().
	 * @param translationSpace		TranslationSpace used for translating preferences XML.
	 * 								If this is null, 
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * 								will be used.
	 * @param graphicsDev		graphics_device (screen number) to display window. count from 0.
	 * @param screenSize		used in TopLevel --
	 * 								1 - quarter; 2 - almost half; 3; near full; 4 full
	 */
	public ApplicationEnvironment(Class baseClass, String applicationName, String propertiesFileRelativePath, 
			TranslationSpace translationSpace, String graphicsDev, String screenSize) 
	{
		//ElementState.setDeclarationStyle(ElementState.DeclarationStyle.PUBLIC);
		Environment.the.set(this);

		// setup codeBase
		if (baseClass == null)
			baseClass			= this.getClass();
		
		if (translationSpace == null)
			translationSpace	= DefaultServicesTranslations.get();
		
		Package basePackage		= baseClass.getPackage();
		String packageName		= basePackage.getName();
		String packageNameAsPath= packageName.replace('.', Files.sep);

		String pathName			= System.getProperty("user.dir") + Files.sep;
		File path				= new File(pathName);
		String pathString		= path.getAbsolutePath();
		
		//println("looking for " + packageNameAsPath +" in " + pathString);

		int packageIndex		= pathString.lastIndexOf(packageNameAsPath);
		if (packageIndex != -1)
		{
			pathString			= pathString.substring(0, packageIndex);
			path				= new File(pathString + Files.sep);
		}

		codeBase				= new ParsedURL(path);
		println("codeBase="+codeBase);
		// load general application propertioes
		//loadProperties(path, BASE_PREFERENCE_PATH);
		loadPreferencesXML(translationSpace, path, BASE_PREFERENCE_PATH);
		// load properties specific to this invocation
		if (propertiesFileRelativePath != null)
		{
//			loadProperties(path, propertiesFileRelativePath);
			loadPreferencesXML(translationSpace, path, propertiesFileRelativePath);
		}
//		else
//			properties			= new Properties();
		
		if (graphicsDev != null)
			setProperty("graphics_device", graphicsDev);

		if (screenSize != null)
			setProperty("screen_size", screenSize);
		
		PropertiesAndDirectories.setApplicationName(applicationName);
//		PropertiesAndDirectories.thisApplicationDir();
	}
	private void loadPreferencesXML(TranslationSpace translationSpace, File path, String prefFilePath)
	{
		File preferencesXMLFile	= new File(path, prefFilePath);
		if (preferencesXMLFile.exists())
		{
			try
			{
				debugA("Loading preferences from: " + preferencesXMLFile);
				PreferencesSet ps	= (PreferencesSet) ElementState.translateFromXML(preferencesXMLFile, translationSpace);
				ps.processPreferences();
			} catch (XmlTranslationException e)
			{
				e.printStackTrace();
			}
		}
		else
			debugA("Can't find preferences file: " + preferencesXMLFile);
	}
	
	
	public void setProperty(String propertyName, String propertyValue)
	{
//		properties.setProperty(propertyName, propertyValue);
		preferencesRegistry.registerObject(propertyName, propertyValue);
	}
    /**
     * @see ecologylab.generic.Environment#runtimeEnv()
     */
    public int runtimeEnv()
    { return APPLICATION;}
    
    /**
     * @see ecologylab.generic.Environment#showStatus(String)
     */
    public void showStatus(String s) 
    {
	System.out.println(s);
    }

	/**
	 * @see ecologylab.generic.Environment#status(String)
	 */
	public void status(String msg) 
	{
	if (msg != null)
	    showStatus(msg);
	}

	/**
	 * @see ecologylab.generic.Environment#parameter(String)
	 */
	public String parameter(String name)
	{
//		return properties.getProperty(name);
		return (String) preferencesRegistry.lookupObject(name);
	}

	/**
	 * @see ecologylab.generic.Environment#codeBase()
	 * return the path to root of the
	 */
	public ParsedURL codeBase() 
	{
		return codeBase;
	}

	/**
	 * @see ecologylab.generic.Environment#docBase()
	 * return the current working directory of the application
	 * which is "c:\web\code\java\cm"
	 */
	public ParsedURL docBase()
	{
		ParsedURL purl = new ParsedURL(new File(System.getProperty("user.dir")));
		return purl;
	}
	
	static final String FIREFOX_PATH_WINDOWS	= "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
	static final String IE_PATH_WINDOWS			= "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE";
	
	static File	firefoxFileCache;
	
	static File getFirefoxFile()
	{
		File result		= firefoxFileCache;
		if (result == null)
		{
			result		= new File(FIREFOX_PATH_WINDOWS);
			firefoxFileCache	= result;
		}
		return result;
	}
	
	public void go(ParsedURL purl, String frame)
	{
		int os		= PropertiesAndDirectories.os();
		String cmd	= "";
		switch (os)
		{
		case PropertiesAndDirectories.WINDOWS:
			File firefoxFile	= getFirefoxFile();
			cmd	= (Generic.parameterBool("navigate_with_ie") || !firefoxFile.exists()) ? IE_PATH_WINDOWS : FIREFOX_PATH_WINDOWS; 
			cmd	+= " " + purl; //" \"" + purl + "\"";
			Debug.println(cmd);
			try {
					Process p = Runtime.getRuntime().exec(cmd);
				} catch (IOException e)
				{
					println("ERROR in go(); caught exception: ");
					e.printStackTrace();
				}
			break;
		default:
			println("go(ParsedURL) not supported for os " + PropertiesAndDirectories.getOsName());
			break;				
		}
	}

	public int browser()
	{
	   return APPLICATION;
	}
    /**
     * Called at the end of an invocation. Calls System.exit(code).
     * 
     * @param	code -- 0 for normal. other values are application specific.
     */
    public void exit(int code)
    {
    	System.exit(code);
    }
    
    //---------------------- not Environment Code - other -----------------------------------
	/**
	 * Form the parameter file path.
	 * Use the 0th argument if there is one, to find a file in config.
	 * If not, use config/interface/paramts.txt.
	 * 
	 * @param args
	 * @return
	 */
	public static String preferencesFileRelativeFromArg0(String[] args) 
	{
		return ((args == null) || (args.length == 0)) ? null :  PREFERENCES_SUBDIR_PATH + args[0];
	}

	/**
	 * Set the codebase for the application.
	 * Should only be done at startup.
	 */
	public void setCodeBase(ParsedURL codeBase) 
	{
		this.codeBase = codeBase;
	}
	/**
	 * @return Returns the preferencesRegistry.
	 */
	public static ObjectRegistry preferencesRegistry()
	{
		return preferencesRegistry;
	}
	/**
	 * Find a complex object set in preferences.
	 * 
	 * @param name
	 * @return
	 */
	public static ElementState lookupElementStatePreference(String name)
	{
		return (ElementState) preferencesRegistry.lookupObject(name);
	}
}
