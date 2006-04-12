package ecologylab.generic;

import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * Reusable static methods that do nifty network stuff.
 * 
 * @author andruid
 * @author blake
 *
 */
public class NetTools extends Debug
{
/**
 * Free resources as possible on the URLConnection passed in.
 * 
 * This is accomplished by calling disconnect() if it turns out to be an instance of
 * HttpURLConnection.
 * 
 * @param urlConnection	a reference to a URLConnection.
 * 
 * @return	true if the URLConnection reference passed in is not null.
 */
	public static boolean  disconnect(URLConnection urlConnection)
	{
		boolean result	= urlConnection != null;
        if (result && (urlConnection instanceof HttpURLConnection))
        {
       	 HttpURLConnection	httpConnection	= (HttpURLConnection) urlConnection;
       	 httpConnection.disconnect(); // free resources!
        }
        return result;
	}

}
