package cm.generic;

import java.lang.*;

/**
 * @author andruid
 *
 * Methods for manipulating </ode>String</code> and <code>StringBuffer</code>s.
 */
public class StringTools
{
/**
 * Changes the StringBuffer to lower case, in place, without any new storage
 * allocation.
 */
   public static void toLowerCase(StringBuffer buffer)
   {
      int length	= buffer.length();
      for (int i=0; i<length; i++)
      {
	 char c		= buffer.charAt(i);
	 if (Character.isLowerCase(c))
	    buffer.setCharAt(i, Character.toLowerCase(c));
      }
   }
/**
 * Use this method to efficiently get a <code>String</code> from a
 * <code>StringBuffer</code> on those occassions when you plan to keep
 * using the <code>StringBuffer</code>, and want an efficiently made copy.
 * In those cases, <i>much</i> better than 
 * <code>new String(StringBuffer)</code>
 */
   public static String toString(StringBuffer buffer)
   {
      return buffer.substring(0);
   }
}
