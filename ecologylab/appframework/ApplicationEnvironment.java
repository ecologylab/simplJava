package cm.generic;

import java.net.URL;
import cm.generic.ParsedURL;

/**
 * @author madhur
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
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
	 */
	public ParsedURL codeBase() {
		return null;
	}

	/**
	 * @see cm.generic.Environment#docBase()
	 */
	public ParsedURL docBase() {
		return null;
	}

	public ParsedURL codeRelativeURL(String relativeURL)
	{
	   String err = "Cant find " + relativeURL +" relative to "
	      + codeBase.noAnchorNoQueryPageString()+" ";
	   ParsedURL purl = ParsedURL.getRelative(codeBase.url(), relativeURL, err);
	   return purl;
	}
	
/**
 * @return an URL relative to the code base.
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
	
	ParsedURL docBase, codeBase;
	public int browser()
	{
	   return APPLICATION;
	}
}
