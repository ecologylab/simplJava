package cm.generic;

import java.lang.*;
import java.net.*;

/**
 * @author andruid
 *
 * Methods for manipulating </ode>String</code> and <code>StringBuffer</code>s.
 */
public class StringTools
extends Debug
{
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
