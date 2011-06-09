package ecologylab.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;

import ecologylab.collections.CollectionTools;
import ecologylab.generic.Debug;

/**
 * Reusable static methods that do nifty network stuff.
 * 
 * @author andruid
 * @author blake
 * @author eunyee
 *
 */
public class NetTools extends Debug
{
	final static String SUPPORTED_CHARSETS[]	=
	{
		"us-ascii", "windows-1250", "windows-1251", "windows-1252", "windows-1253",
		"windows-1254", "windows-1257", "iso-8859-1", "iso-8859-2", "iso-8859-4",
		"iso-8859-5", "iso-8859-7", "iso-8859-9", "iso-8859-13", "iso-8859-15",
		"ISO-8859-1", "ISO_8859-1", "ISO-8859-2", "ISO-8859-4",
		"ISO-8859-5", "ISO-8859-7", "ISO-8859-9", "ISO-8859-13", "ISO-8859-15",
		"koi8-r", "utf-8", "utf-16", "utf-16be", "utf-16le",
		"UTF-8", "UTF-16", "UTF-16be", "UTF-16le"
	};
	final static HashMap<String, String> supportedCharsetMap	= CollectionTools.buildHashMapFromStrings(SUPPORTED_CHARSETS);
	
	/**
	 * Seek a charset specification in the MimeType header of the HTTP request.
	 * The return values are strange, in order to enable reporting an error to happen conveniently
	 * around the call site.
	 *
	 * @param mimeType	The Mime Type header.
	 * 
	 * @return			Null if the charset is supported (including if there is no specificaton of it in the header).
	 * 					The charset that is unsupported, if that is the case.
	 */
	public static String isCharsetSupported(String mimeType)
	{
		if (mimeType == null)
			return null;
		
		int charsetIndex	= mimeType.indexOf("charset");
		if (charsetIndex > -1)
		{
			int equalsIndex	= mimeType.indexOf('=', charsetIndex);
			if (equalsIndex++ > -1)		// seek and skip over the equals
			{
				int closingSemIndex	= mimeType.indexOf(';', equalsIndex);
				String charset = null;
				if( equalsIndex >= closingSemIndex )
				{
					charset		= (closingSemIndex == -1) ? mimeType.substring(equalsIndex) :
							mimeType.substring(closingSemIndex, equalsIndex);
				}
				
				if ((charset != null) && (charset.length() > 0))
				{
					charset		= charset.trim();
					if (charset.startsWith("\""))
						charset	= charset.substring(1);
					if (charset.endsWith("\""))
						charset	= charset.substring(0, charset.length() - 1);
					//println("CHARSET: '" + charset + "'");
					if (!supportedCharsetMap.containsKey(charset))
					{
						return charset;
					}
				}
			}
		}
		return null;
		/*
		 StringTokenizer st	= new StringTokenizer( mimeType, ";= ");
		 String encoding		= null;
		 while(st.hasMoreTokens())
		 {
		 if( st.nextToken().equals("charset"))
		 {
		 encoding = st.nextToken();
		 println("ENCODING : " + encoding);
		 }
		 }
		 if( (encoding != null) && !supportedCharsetMap.containsKey(encoding) )
		 {
		 infoCollector.displayStatus("Cant process charset " + encoding + " in " + purl.toString() );
		 return null;
		 }
		 */
	}
	
/**
 * Free resources as possible on the URLConnection passed in.
 * 
 * This is accomplished by calling disconnect() if it turns out to be an instance of
 * HttpURLConnection.
 * 
 * @param urlConnection	a reference to a URLConnection.
 */
	public static void disconnect(URLConnection urlConnection)
	{
		if ((urlConnection != null) && (urlConnection instanceof HttpURLConnection))
		{
			HttpURLConnection	httpConnection	= (HttpURLConnection) urlConnection;
			httpConnection.disconnect(); // free resources!
		}
	}

	public static void close(InputStream inStream)
	{
		if (inStream != null)
			try
			{
				inStream.close();
			}
			catch (IOException e)
			{
			}
	}
	static String localHost = null;
	/**
	 * local host address (parse out only IP address)
	 * @return
	 */
	public static String localHost()
	{
		String localHost1			= NetTools.localHost;
		if (localHost1 == null)
		{
			try
			{
				localHost1			= InetAddress.getLocalHost().toString();
				//		localHost = localHost.replace('/','_');
				localHost1			= localHost1.substring(localHost1.indexOf('/')+1);
				NetTools.localHost	= localHost1;
			} catch (UnknownHostException e)
			{
				e.printStackTrace();
			}
		}
		return localHost1;
	}

	public static InetAddress[] getAllInetAddressesForLocalhost()
	{
		HashSet<InetAddress> addresses = new HashSet<InetAddress>();

		try
		{
			Enumeration<NetworkInterface> byName = NetworkInterface.getNetworkInterfaces();
			while(byName.hasMoreElements())
			{
				NetworkInterface nextElement = byName.nextElement();
				//System.out.println(nextElement.getDisplayName());
				Enumeration<InetAddress> inetAddresses = nextElement.getInetAddresses();
				while(inetAddresses.hasMoreElements())
				{
					addresses.add(inetAddresses.nextElement());
				}
			}
		}
		catch (SocketException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try
		{
			for (InetAddress a : InetAddress.getAllByName("localhost"))
			{
				addresses.add(a);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		try
		{
			for (InetAddress a : InetAddress.getAllByName(InetAddress.getLocalHost().getHostAddress()))
			{
				addresses.add(a);
			}
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		try
		{
			addresses.add(InetAddress.getLocalHost());
		}
		catch (UnknownHostException e)
		{
			e.printStackTrace();
		}

		return addresses.toArray(new InetAddress[addresses.size()]);
	}

	/**
	 * Convienence method for getting a single-element array of InetAddresses for servers that normally take an array,
	 * but when only one is available.
	 * 
	 * @param address
	 *           the address to wrap.
	 * @return a single-element array containing address.
	 */
	public static final InetAddress[] wrapSingleAddress(InetAddress address)
	{
		InetAddress[] wrappedAddress =
		{ address };

		return wrappedAddress;
	}
}
