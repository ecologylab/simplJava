package cm.generic;

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
	 * @see cm.generic.Environment#parameterBool(String)
	 */
	public boolean parameterBool(String name) {
		return false;
	}

	/**
	 * @see cm.generic.Environment#parameterInt(String)
	 */
	public int parameterInt(String paramName) {
		return 0;
	}

	/**
	 * @see cm.generic.Environment#parameterInt(String, int)
	 */
	public int parameterInt(String paramName, int defaultValue) {
		return 0;
	}

	/**
	 * @see cm.generic.Environment#parameterFloat(String, float)
	 */
	public float parameterFloat(String paramName, float defaultValue) {
		return 0;
	}

	/**
	 * @see cm.generic.Environment#codeBase()
	 * Change return value from URL to ParsedURL. 
	 */
	public ParsedURL codeBase() {
		return null;
	}

	/**
	 * @see cm.generic.Environment#docBase()
	 * Change return value from URL to ParsedURL. 
	 */
	public ParsedURL docBase() {
		return null;
	}
	
	/* Change return value from URL to ParsedURL. */
	public ParsedURL codeRelativeURL(String relativeURL)
	{
	   String err = "Cant find " + relativeURL +" relative to "
	      + codeBase.noAnchorNoQueryPageString()+" ";
	   /* get ParsedURL from url string and code base. */
	   ParsedURL purl = ParsedURL.getRelative(codeBase.url(), relativeURL, err);
	   return purl;
	}
	
/**
 * @return a ParsedURL relative to the code base.
 */   
	public ParsedURL docRelativeURL(String relativeURL)
	{
	   String err = "Cant find " + relativeURL + " relative to "
	      + docBase.noAnchorNoQueryPageString()+" ";
	   ParsedURL purl = ParsedURL.getRelative(docBase.url(), relativeURL, err);
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
