package cm.generic;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import cm.generic.ParsedURL;

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
	public ApplicationEnvironment()
	{
	   Environment.the.set(this);
	}
    /**
     * @see cm.generic.Environment#runtimeEnv()
     */
    public int runtimeEnv()
    { return APPLICATION;}
    
    /**
     * @see cm.generic.Environment#showStatus(String)
     */
    public void showStatus(String s) 
    {
	System.out.println(s);
    }

	/**
	 * @see cm.generic.Environment#status(String)
	 */
	public void status(String msg) 
	{
	if (msg != null)
	    showStatus(msg);
	}

	/**
	 * @see cm.generic.Environment#parameter(String)
	 */
	public String parameter(String name) {
		return null;
	}

	/**
	 * @see cm.generic.Environment#codeBase()
	 * return the current working directory of the application
	 * which is "c:\web\code\java\cm"
	 */
	public ParsedURL codeBase() {
			ParsedURL purl = new ParsedURL(new File(System.getProperty("user.dir")));
			return purl;
	}

	/**
	 * @see cm.generic.Environment#docBase()
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
}
