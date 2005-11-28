package ecologylab.generic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import ecologylab.generic.ParsedURL;

/**
 * An instance of Environment, which is an application, rather than an applet,
 * or a servlet.
 * The Environment mechanism is used to enable the provision of contextual
 * runtime services in a way that is independent of the deployment structure.
 * 
 * @author Andruid
 */
public class ApplicationEnvironment
extends Debug
implements Environment
{
	public static Properties properties;
	
	ParsedURL	codeBase;
	ParsedURL	docBase;

	
	public ApplicationEnvironment()
	{
	   properties = new Properties();
	}
	
	/**
	 * Create an ApplicationEnvironment.
	 * Load Properties from a properties file found in the config/ directory.
	 * If there is a 0th command line argument, that is the name of the
	 * properties file. Otherwise, the properties come from
	 * config/interface/params.txt.
	 *  
	 * @param propertiesFileRelativePath	Path to the properties file, relative to codeBase().
	 */
	public ApplicationEnvironment(Class baseClass, String args[])
	{
	   this(baseClass, paramaterFileRelativeFromArg0(args));
	}
	
	/**
	 * Create an ApplicationEnvironment.
	 * Load properties from the properties file specified here.
	 *  
	 * @param propertiesFileRelativePath	Path to the properties file, relative to codeBase().
	 */
	public ApplicationEnvironment(Class baseClass, String propertiesFileRelativePath) 
	{
		// setup codeBase
		Package basePackage		= baseClass.getPackage();
		String packageName		= basePackage.getName();
		String packageNameAsPath= packageName.replace('.', Files.sep);

		File path				= new File(System.getProperty("user.dir"));
		String pathString		= path.getAbsolutePath();
		
		println("looking for " + packageNameAsPath +" in " + pathString);

		int packageIndex		= pathString.lastIndexOf(packageNameAsPath);
		if (packageIndex != -1)
		{
			pathString			= pathString.substring(0, packageIndex);
			path				= new File(pathString);
		}

		codeBase				= new ParsedURL(path);

		println("Loading from codeBase " + path +"\n\tproperties " + propertiesFileRelativePath);
		loadProperties(path, propertiesFileRelativePath);
	}
	
	public void loadProperties(File path, String filename)
	{
		try 
		{
			properties = new Properties();

			File file = new File(path, filename);
			println("Property file located at:"+file);
			properties.load(new FileInputStream(file));
			Environment.the.set(this);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public String parameter(String name) {
		return properties.getProperty(name);
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
	public ParsedURL docBase() {
		ParsedURL purl = new ParsedURL(new File(System.getProperty("user.dir")));
		return purl;
	}
	

	public void go(ParsedURL purl, String frame)
	{
		int os		= PropertiesAndDirectories.os();
		String cmd	= "";
		switch (os)
		{
		case PropertiesAndDirectories.WINDOWS:
			cmd	= "C:\\Program Files\\Mozilla Firefox\\firefox " + purl;
			try {
					Process p = Runtime.getRuntime().exec(cmd);
				} catch (IOException e)
				{
					println("problems in go(); caught exception: ");
					e.printStackTrace();
				}
			break;
		default:
			println("go(ParsedURL) not supported for os " + os);
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
	public static String paramaterFileRelativeFromArg0(String[] args) 
	{
		String paramFileName	= 
			(args.length >= 1) ? args[0] : "interface/params.txt";
		 
		 String paramFileRelPath= "config/" + paramFileName;
		return paramFileRelPath;
	}

}
