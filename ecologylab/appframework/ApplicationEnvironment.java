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

			properties.load(new FileInputStream(new File(path, filename)));
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
	

	public void go(URL u, String frame)
	{
	}

	public int browser()
	{
	   return APPLICATION;
	}
}
