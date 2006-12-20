package ecologylab.appframework;

import java.io.File;
import java.io.IOException;
import java.util.Stack;

import ecologylab.generic.Debug;
import ecologylab.io.Files;
import ecologylab.net.ParsedURL;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.services.messages.Preference;
import ecologylab.services.messages.PreferencesSet;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;

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

	TranslationSpace		translationSpace;
	
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
	   this(null, applicationName, null);
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
	 * <p/>
	 * Treats the args array like a stack. If any args are missing (based on their format), they are skipped.
	 * <p/>
	 * The first arg we seek is codeBase. This is a path that ends in slash.
	 * It may be a local relative path, or a URL-based absolute path.
	 * <p/>
	 * The next possible arg is a preferences file. This ends with .xml.
	 * <p/>
	 * The next 2 possible args are integers, for graphicsDev and screenSize.
	 * 			graphics_device (screen number) to display window. count from 0.
	 * 			screenSize		used in TopLevel --
	 * 								1 - quarter; 2 - almost half; 3; near full; 4 full	 
	 * <p/>
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
	 * @param translationSpace		TranslationSpace used for translating preferences XML.
	 * 								If this is null, 
	 * {@link ecologylab.services.message.DefaultServicesTranslations ecologylab.services.message.DefaultServicesTranslations}
	 * 								will be used.
	 * @param args				The args array
	 */
	public ApplicationEnvironment(Class baseClass, String applicationName, TranslationSpace translationSpace,  String args[])
//			String preferencesFileRelativePath, String graphicsDev, String screenSize) 
	{
		this.translationSpace		= translationSpace;
		
		// this is the one and only singleton Environment
		Environment.the.set(this);
		
		PropertiesAndDirectories.setApplicationName(applicationName);

		if (translationSpace == null)
			translationSpace	= DefaultServicesTranslations.get();
		
		Stack<String> argStack	= new Stack<String>();
		
		for (int i = args.length - 1; i>=0; i--)
			argStack.push(args[i]);
		
		// look for launch method identifier in upper case
		String arg				= pop(argStack);
		if (arg != null)
		{
			String uc				= arg.toUpperCase();
			if (arg.equals(uc))
			{	// tells us how we were launched: e.g., JNLP, ECLIPSE, ...
				debug("launch type: " + arg);
			}
			else
				argStack.push(arg);
		}
		
		// look for codeBase path
		arg						= pop(argStack);
		// prepare for the possibility of preferences to load from a local codebase (to support eclipse launch)
		File localCodeBasePath	= null;
		
		if ((arg != null) && arg.endsWith("/"))
		{	// JNLP only! (as of now)
			// right now this only works for http://
			ParsedURL codeBase	= ParsedURL.getAbsolute(arg, "Setting up codebase");
			this.setCodeBase(codeBase);
			
			//TODO -- Megan, call parsing the user's preferences Asset here
		}
		else
		{	// NB: This gets executed even if arg was null!
			localCodeBasePath = deriveLocalFileCodeBase(baseClass);
			argStack.push(arg);

			// load local preferences from codeBase path, if appropriate
			// load default preferences
			PreferencesSet.loadPreferencesXML(translationSpace, localCodeBasePath, BASE_PREFERENCE_PATH);
			
			// now seek the path to an application specific xml preferences file
			arg						= pop(argStack);
			if (arg == null)
				return;

			// load preferences specific to this invocation
			if (arg.endsWith(".xml"))
			{
				PreferencesSet.loadPreferencesXML(translationSpace, localCodeBasePath, arg);
			}
			else
				argStack.push(arg);
		}
		
		arg						= pop(argStack);
		if (arg == null)
			return;
		try
		{
			Integer.parseInt(arg);
			setProperty("graphics_device", arg);
			
			arg						= pop(argStack);
			if (arg == null)
				return;
			Integer.parseInt(arg);
			setProperty("screen_size", arg);
		} catch (NumberFormatException e)
		{
			argStack.push(arg);
		}
		
		// could parse more args here
	}
	
	/**
	 * Get the user.dir property. Form a path from it, ending in slash.
	 * See if there is path within that that includes the package of baseClass.
	 * If so, remove that component from the path.
	 * 
	 * Form a File from this path, and a ParsedURL from the file.
	 * Set codeBase to this ParsedURL.
	 * 
	 * @param baseClass		Class of the subclass of this that is the main program that was executed.
	 * 
	 * @return				File that corresponds to the path of the local codeBase.
	 */
	private File deriveLocalFileCodeBase(Class baseClass)
	{
		// setup codeBase
		if (baseClass == null)
			baseClass			= this.getClass();
		
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
		return path;
	}
	public void setProperty(String propertyName, String propertyValue)
	{
		preferencesRegistry().registerObject(propertyName, propertyValue);
	}
    /**
     * @see ecologylab.appframework.Environment#runtimeEnv()
     */
    public int runtimeEnv()
    { return APPLICATION;}
    
    /**
     * @see ecologylab.appframework.Environment#showStatus(String)
     */
    public void showStatus(String s) 
    {
	System.out.println(s);
    }

	/**
	 * @see ecologylab.appframework.Environment#status(String)
	 */
	public void status(String msg) 
	{
	if (msg != null)
	    showStatus(msg);
	}

	/**
	 * @see ecologylab.appframework.Environment#parameter(String)
	 */
	public String parameter(String name)
	{
//		return properties.getProperty(name);
		return (String) preferencesRegistry().lookupObject(name);
	}

	/**
	 * @see ecologylab.appframework.Environment#codeBase()
	 * return the path to root of the
	 */
	public ParsedURL codeBase() 
	{
		return codeBase;
	}

	/**
	 * @see ecologylab.appframework.Environment#docBase()
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
			cmd	= (Preference.lookupBoolean("navigate_with_ie") || !firefoxFile.exists()) ? IE_PATH_WINDOWS : FIREFOX_PATH_WINDOWS; 
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
		if ((args == null) || (args.length == 0))
			return null;
		String arg0	= args[0];
		String lc	= arg0.toLowerCase();
		
		return lc.endsWith("xml") ? (PREFERENCES_SUBDIR_PATH + args[0]) : null;
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
		return Environment.the.preferencesRegistry();
	}
	/**
	 * Find a complex object set in preferences.
	 * 
	 * @param name
	 * @return
	 */
	public static ElementState lookupElementStatePreference(String name)
	{
		return (ElementState) preferencesRegistry().lookupObject(name);
	}
	
	static <T> T pop(Stack<T> stack)
	{
		return stack.isEmpty() ? null : stack.pop();
	}
	
	static <T> void push(Stack<T> stack, T stuff)
	{
		if (stuff != null)
			stack.push(stuff);
	}
	/**
	 * Translation space used to parse Preferences for this Application.
	 * @return
	 */
	public TranslationSpace translationSpace()
	{
		return translationSpace;
	}
}
