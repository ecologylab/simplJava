package ecologylab.generic;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;

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

	static String localHost = null;
	/**
	 * local host address (parse out only IP address)
	 * @return
	 */
	public static String localHost()
	{
		String localHost			= NetTools.localHost;
		if (localHost == null)
		{
			try
			{
				localHost			= InetAddress.getLocalHost().toString();
				//		localHost = localHost.replace('/','_');
				localHost			= localHost.substring(localHost.indexOf('/')+1);
				NetTools.localHost	= localHost;
			} catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		}
		return localHost;
	}
	
}
