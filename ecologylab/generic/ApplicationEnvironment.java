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
public class ApplicationEnvironment implements Environment
{
	public static Properties properties;
	
	public ApplicationEnvironment()
	{
	   properties = new Properties();
	}
	
	public ApplicationEnvironment(String filename) {
		properties = new Properties();
		loadProperties(filename);
	}
	
	public void loadProperties(String filename)
	{
		try {
			properties.load(new FileInputStream(new File(filename)));
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
	 * return the current working directory of the application
	 * which is "c:\web\code\java\cm"
	 */
	public ParsedURL codeBase() {
			ParsedURL purl = new ParsedURL(new File(System.getProperty("user.dir")));
			return purl;
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
	/* Change type from URL to ParsedURL. */
	ParsedURL docBase, codeBase;
	public int browser()
	{
	   return APPLICATION;
	}
	
	/**
	 * Returns the environment's current 
	 * max memory settings (based on command-line parameters in the 
	 * java control panel)
	 * 
	 * @return The maximum amount of memory that can be allocated to the JVM
	 * in megabytes.
	 */
	public static int getMaxMemory()
	{
		long maxMemory		= Runtime.getRuntime().maxMemory();
	    // report in megabytes
	    return				  (int)(maxMemory / (1024 * 1024));
	}
}
