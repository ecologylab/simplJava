package cm.generic;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 * @author madhur+andruid
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Generic 
{

	/**
 * Turn a string into a float.
 * 
 * @return	the float, if the String is cool; else Float.NaN
 */
   static public float parseFloat(String floatString)
   {
      float result;
      try
      {
	 Double fObj	= Double.valueOf(floatString);
	 result		= fObj.floatValue();
      } catch (NumberFormatException e)
      {
	 result		= Float.NaN;
      }
      return result;
   }

	/**
 * Sleep easily, ignoring (unlikely) <code>InterruptedException</code>s.
 */
   public static void sleep(int time)
   {
      try
      {
	 Thread.sleep(time);
      }
      catch (InterruptedException e)
      {
//	 System.out.println("Sleep was interrupted -- clearing if possible.\n"
//			    + e);
	 // in jdk 1.1x clears the interrupt
	 // !!! (undocumented) !!! (see Thread src)
	 Thread.interrupted();	
      }
   }

/**
 * Form a URL easily, without perhaps throwing an exception.
 */
   public static URL getURL(URL base, String path, String error)
   {
      // ??? might want to allow this default behaviour ???
      if (path == null)
	 return null;
      try 
      {
	 return new URL(base, path);
      } catch (MalformedURLException e) 
      {
	 if (error != null)
	    throw new Error(e + "\n" + error + " " + base + " -> " + path);
	 return null;
      }
   }


	/**
 * Form a URL easily, without perhaps throwing an exception.
 */
   public static URL getURL(String path, String error)
   {
      // ??? might want to allow this default behaviour ???
      if (path == null)
	 return null;
      try 
      {
	 return new URL(path);
      } catch (MalformedURLException e) 
      {
	 throw new Error(e + "\n" + error + " " + path);
      }
   }



	/**
 * Set the priority of the current thread.
 */
   static public void setPriority(int priority)
   {
      Thread.currentThread().setPriority(priority);
   }

	   public static boolean contains(String in, String toMatch)
   {
      return (in == null) ? false : in.indexOf(toMatch) != -1;
   }

   //////////////////////////////////////////////////////////////


   ///////////////////////////////////////////////////////////////////////

}
