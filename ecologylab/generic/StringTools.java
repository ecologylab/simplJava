package cm.generic;

import java.lang.*;
import java.net.*;
import java.util.*;

/**
 * @author andruid
 *
 * Methods for manipulating </ode>String</code> and <code>StringBuffer</code>s.
 */
public class StringTools
extends Debug
{
   static final HashMap	oneDotDomains	= new HashMap();
   static final String[]	oneDotDomainStrings = 
   {
      "com", "edu", "gov", "org", "net",
   };
   static
   {
      for (int i=0; i<oneDotDomainStrings.length; i++)
      {
	 String thatDomain	= oneDotDomainStrings[i];
	 oneDotDomains.put(thatDomain, thatDomain);
      }
   }
/**
 * Changes the StringBuffer to lower case, in place, without any new storage
 * allocation.
 */
   public static final void toLowerCase(StringBuffer buffer)
   {
      int length	= buffer.length();
      for (int i=0; i<length; i++)
      {
	 char c		= buffer.charAt(i);
//	 if (Character.isUpperCase(c))
	 // A = 0x41, Z = 0x5A; a = 0x61, z = 0x7A
	 if ((c >='A') && (c <= 'Z'))
	 {
	    c	       += 0x20;
	    buffer.setCharAt(i, c);
	 }
      }
   }
   public static final boolean sameDomain(URL url1, URL url2)
   {
      return domain(url1).equals(domain(url2));
   }
   public static final String domain(URL url)
   {
      return domain(url.getHost());
   }
/**
 * Useful for finding common domains.
 */
   public static final String domain(String urlString)
   {
      int end	= urlString.length() - 1;
      int domainStartingDot	= 0;
      boolean foundFirstDot	= false;
      boolean foundSecondDot	= false;
      boolean international	= false;
      String result	= urlString;
      for (int i=end; i>0; i--)
      {
	 if (urlString.charAt(i) == '.')
	 {
	    if (foundFirstDot)
	    {
	       if (international && !foundSecondDot)
	       {
		  foundSecondDot	= true;
	       }
	       else
	       {
		  domainStartingDot	= i + 1;
		  break;
	       }
	    }
	    else
	    {
	       foundFirstDot	= true;
	       String suffix	= urlString.substring(i+1);
	       international	= !oneDotDomains.containsKey(suffix);
	    }
	 }
      }
      return urlString.substring(domainStartingDot, end + 1);
   }
   
/**
 * Use this method to efficiently get a <code>String</code> from a
 * <code>StringBuffer</code> on those occassions when you plan to keep
 * using the <code>StringBuffer</code>, and want an efficiently made copy.
 * In those cases, <i>much</i> better than 
 * <code>new String(StringBuffer)</code>
 */
   public static final String toString(StringBuffer buffer)
   {
      return buffer.substring(0);
   }
   public static final boolean contains(String in, String toMatch)
   {
      return (in == null) ? false : in.indexOf(toMatch) != -1;
   }

/**
 * Very efficiently forms String representation of url (better than 
 * <code>URL.toExternalForm()</code>). Doesn't include query or anchor.
 */
   public static final String pageString(URL u)
   {
      String protocol	= u.getProtocol();
      String authority	= u.getAuthority(); // authority is host:port
      String path	= u.getPath();	    // doesn't include query

      // pre-compute length of StringBuffer
      int length	=
	 protocol.length() + 3 /* :// */ + authority.length() + path.length();

      StringBuffer result = new StringBuffer(length);
      result.append(protocol).append("://").append(authority).append(path);

      return new String(result);
   }
   public static void main(String[] args)
   {
      for (int i=0; i<args.length; i++)
	 println(pageString(Generic.getURL(args[i], "oops " + i)));
	 
   }
}
