package ecologylab.generic;

import java.net.*;

/**
 * Generic generics.
 */
public class Convenience
{
   public static boolean contains(String s, String c)
   {
      return (s.indexOf(c) != -1);
   }
   public static String shortURL3(URL u)
   {
      String s	= null;
      if (u == null)
	 s	= "";
      else
      {
	 String file	= u.getFile();
	 String ending	= ending(file);
	 if (contains(ending, "default.") ||
	     contains(ending, "index.") ||
	     contains(ending, "default.") ||
	     ending.equals(""))
	 {
	 }	 
      }
      return s;
   }
   public static String ending(String file)
   {
      return (file == "") ? "" : file.substring(file.lastIndexOf('/') + 1);
   }
   public static String shortURL(URL u)
   {
      String s;
      if (u == null)
	 s	= "";
      else
      {
	 String file	= u.getFile();
//	 Debug.println("short URL file="+file);
	 String begining= file;
	 int endDex	= begining.lastIndexOf('/');
	 String ending	= begining.substring(endDex + 1);
	 if (contains(ending, "default.") ||
	     contains(ending, "index.") ||
	     ending.equals(""))
	     
	 {
	    if (endDex > 0)
	    {
	       begining	= begining.substring(0, endDex);
	       endDex	= begining.lastIndexOf('/');
	       ending	= begining.substring(endDex + 1);
//	       Debug.println("2nd begining="+begining+" "+ " ending="+ending);
	    }
	    else
	       ending	= "";
	 }
	 String intermediate = (endDex <= 0) ? "/" : "/.../";
	 s	= u.getHost() + intermediate + ending;
      }
      return s;
   }
	 
}
