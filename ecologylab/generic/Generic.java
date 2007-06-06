package ecologylab.generic;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.net.*;
import java.io.*;

import javax.swing.JOptionPane;

import ecologylab.appframework.Environment;
import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.net.ParsedURL;

/**
 * A set of generic convenience methods for doing things like getting
 * typed parameters from the envionment, getting other stuff from
 * the environment, building HashMaps from arrays, manipulating threads,
 * ... While some of these functionalities constitute a nice set of static
 * covers for facilities provided by {@link Environment Environment},
 * this class can be viewed as a kitchen sink of useful static methods.
 * 
 * @author andruid
 */
public class Generic 
{
/**
    * Convert a String to a boolean.
    * 
    * @param value
    * @return	true if the String is "true", "yes", or "1".
    * 			false otherwise
    */
   public static boolean booleanFromString(String value)
   {
	  return value.equalsIgnoreCase("true") ||
				value.equalsIgnoreCase("yes") || value.equals("1");
   }
   public static void status(String msg)
   {
      Environment.the.get().status(msg);
   }

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
 * Sleep easily.
 * Report <code>InterruptedException</code>s when they happen,
 * if the param is true.
 */
   public static final void sleep(int time)
   {
	  sleep(time, true);
   }
   
/**
 * Sleep easily.
 * Report <code>InterruptedException</code>s when they happen,
 * if the param is true.
 */
   public static final void sleep(int time, boolean reportExceptions)
   {
      try
      {
		 Thread.sleep(time);
      }
      catch (InterruptedException e)
      {
		 if (reportExceptions)
		 {
			Debug.println("Sleep was interrupted -- clearing if possible.");
			e.printStackTrace();
		 }
		 // in jdk 1.1x clears the interrupt
		 // !!! (undocumented) !!! (see Thread src)
		 Thread.interrupted();	
      }
   }

   public static ParsedURL codeBase()
   {
	   return Environment.the.get().codeBase();
   }
   
   static ParsedURL configDir;
   
   /**
    * The config directory, as located relative to the codebase, with the jar file and perhaps the sources.
    * This is where the tree of configuration assets is rooted.
    * Examples of configuration assets include interface graphics, interface semantic descriptions (xml),
    * dictionary, ...
    * These are files that don't change often, and are needed for an application.
    * 
    * @return
    */
   public static ParsedURL configDir()
   {
	   ParsedURL result	= configDir;
	   if (result == null)
	   {
		   result		= ParsedURL.getRelativeToCodeBase("config/", "Error forming config dir.");
		   configDir	= result;
	   }
	   return result;
   }
   /**
    * Obtain a path relative to the configDir().
    * 
    * @param relativePath
    * @return
    */
   public static ParsedURL configPath(String relativePath)
   {
	   return configDir().getRelative(relativePath, "Error forming config directory path.");
   }
public static final String SEP	= "/";
   
   /**
 * @return	The version of Java we're using (but not the specific release),
 *		as in 1.2, 1.3, 1.4,...
 */
   public static float javaVersion()
   {
      return Environment.the.javaVersion();
   }
   /**
    * Check to see if we're running on what we consider to be a decent, usable version of Java.
    * For 1.5, this means rel 4 or more; for 1.4, it means 1.42_04 or more.
    * 
    * @return	true if the Java we're running on is good; false if its crap.
    */
   public static boolean hasGoodJava()
   {
	   return Environment.the.hasGoodJava();
   }
   /**
    * @return The version of Java we're using (with the specific release)
    */
   public static String javaVersionFull()
   {
	   return System.getProperty("java.version");
   }
   
   /**
    * Where to navigate to to download the lastest Java.
    */
   public static ParsedURL	SUN_JAVA_PURL	= ParsedURL.getAbsolute("http://www.java.com/en/download/", "Java download");
   /**
    * Where to navigate to to download the lastest Java for the Macintosh.
    */
   public static ParsedURL	MAC_JAVA_PURL	= ParsedURL.getAbsolute("http://www.apple.com/java/", "Java download");

   /**
    * Checks what platform we're on, and returns a suitable PURL that you would navigate to,
    * to download the current Java.
    * 
    * @return	PURL of www.java.com, or www.apple.com/java.
    */
   public static ParsedURL downloadJavaPURL()
   {
	   ParsedURL result;
	   switch (PropertiesAndDirectories.os())
	   {
	   case PropertiesAndDirectories.MAC:
		   result	= MAC_JAVA_PURL;
	   case PropertiesAndDirectories.WINDOWS:
	   case PropertiesAndDirectories.LINUX:
	   default:
		   result	= SUN_JAVA_PURL;
	   }
	   return result;
   }
   
   public static boolean hasXML()
   {
      return Environment.the.hasXML();
   }

/**
 * Set the priority of the current thread.
 */
   static final public void setPriority(int priority)
   {
      setPriority(Thread.currentThread(), priority);
   }
/**
 * Set the priority of the current thread.
 */
   static final public void setPriority(Thread t, int priority)
   {
      int oldPriority	= t.getPriority();
      if (oldPriority != priority)
      	 t.setPriority(priority);
   }

   public static final boolean contains(String in, String toMatch)
   {
      return StringTools.contains(in, toMatch);
   }

   //////////////////////////////////////////////////////////////
   public static String round(float f, int powerOfTen)
   {
      if (Float.isNaN(f))
		 return "NaN";
      int i = (int) f;
      if ((f - i) == 0)
      {
		 return Integer.toString(i);
      }
      String input = Float.toString(f);
	  //    Debug.println("input="+input+" powerOfTen="+powerOfTen);
      int end = input.length();
      int dot = input.indexOf('.');
      int exp = input.indexOf('E');
      int endFigs = (exp <= 0) ? end : exp;
      int figs = endFigs - dot;
	  //    Debug.println("dot="+dot+" exp="+exp+" figs="+figs+" endFigs="+endFigs);
      String result = input;
      if (figs > powerOfTen)
      {
		 result = input.substring(0, dot+powerOfTen+1);
		 if (exp > 0)
			result += input.substring(exp);
      }
      return result ;
   }

/**
 * Get the IP number for the user's machine.
 * returns:	the ip number as a string, or unknown if JDK 1.0x or
 * other error (like security).
 * !!! for error cases, could create somewhat elaborate scheme to synthesize
 * some kind of id from a cookie, but current usage is just for the study --
 * doesnt need to be perfect. nb: getting ip addr on server side
 * isn't adequate cause proxy servers are so popular w mongo isps like aol!!!
 */
   public static String getLocalIp(URL remote)
   {
      String result	= null;

      try
      {
		 InetAddress server = InetAddress.getByName(remote.getHost());
		 
		 //	 println("getByName() = " + server);
		 
		 Socket socket	= new Socket(server, 80);

		 try
		 {
			InetAddress localHost	= socket.getLocalAddress();
			result		= localHost.getHostAddress();
		 } catch (Exception e)
		 {
			// no such method in JDK 1.0x: getLocalAddress()
			if (!(e instanceof NoSuchMethodException))
			   Debug.println("UserStudy.getLocalIp() unknown error: " +
							 e);
			result		= "unknown";
		 }
		 //	 println("localHost=" + result);
		 socket.close();
      } catch (UnknownHostException e)
      {
		 Debug.println("getByName() failed.\n" + e);
      } catch (IOException e)
      {
		 Debug.println("new Socket() failed.\n" + e);
      }
      return result;
   }

   public static ParsedURL docBase()
   {
      return Environment.the.get().docBase();
   }
   /**
    * Raise the priority of the current thread to the priority level,
    * if the current priority level is less than it.
    * Otherwise, do nothing.
    * 
    * @param priority
    */
   public static void raisePriority(int priority)
   {
      Thread t	= Thread.currentThread();
      int oldPriority	= t.getPriority();
      if (oldPriority < priority)
      {
		 raiseMaxPriority(t, priority);
		 t.setPriority(priority);
		 //Debug.println("\nraisingPriority{" + t + "} " + oldPriority +" -> "+
					   //t.getPriority());
      }
   }
   public static void raiseMaxPriority(int priority)
   {
      raiseMaxPriority(Thread.currentThread(), priority);
   }
   public static void raiseMaxPriority(Thread thread, int priority)
   {
      raiseMaxPriority(thread.getThreadGroup(), priority);
   }
   public static void raiseMaxPriority(ThreadGroup threadGroup, int priority)
   {
      int oldMaxPriority	= threadGroup.getMaxPriority();
      if (oldMaxPriority < priority)
      {
		 ThreadGroup parent	= threadGroup.getParent();
		 if (parent != null)
			raiseMaxPriority(parent, priority); // recurse
		 
		 threadGroup.setMaxPriority(priority);
		 Debug.println("\nraisingMaxPriority to " + priority+"->"+
					   threadGroup.getMaxPriority()+    
					   " "+threadGroup+ " "+parent);
      }
   }
   public static ThreadGroup findThreadGroup(int priority)
   {
      return findThreadGroup(Thread.currentThread(), priority);
   }
   public static ThreadGroup findThreadGroup(Thread thread, 
					     int priority)
   {
      return findThreadGroup(thread.getThreadGroup(), priority);
   }
   public static ThreadGroup findThreadGroup(ThreadGroup threadGroup, 
					     int priority)
   {
      int maxPriority	= threadGroup.getMaxPriority();
      if (maxPriority < priority)
      {
		 ThreadGroup parent	= null;
		 try
		 {
			parent = threadGroup.getParent();
		 } catch (java.security.AccessControlException e)
		 {  // (damn macintosh!)
			Debug.println("ERROR manipulating thread groups!");
			e.printStackTrace();
		 }
		 if (parent != null)
			return findThreadGroup(parent, priority); // recurse
		 else
			return null;
      }
      else
      {
		 Debug.println("found " + threadGroup+"  w maxPriority="+maxPriority);
		 return threadGroup;
      }
   }
/**
 * @return the int represented by <code>input</code>. 
 * Silently defaults to <code>defaultVal</code>, <code>input</code> is
 * not in good form. 
 */
   public static final int parseInt(String input, int defaultVal)
   {
	  if (input != null)
	  {
		 try
		 {
			defaultVal	= Integer.parseInt(input);
		 } catch (NumberFormatException e)
		 {
		 }
	  }
      return defaultVal;
   }

   public static void main(String[] s)
   {
      Debug.println(round(LN_EMPTY_WEIGHT, 2));
      Debug.println(round(.334455f, 3));
      Debug.println(round(-.334455f, 3));
      Debug.println(round(22, 3));
   }
   static final float	LN_EMPTY_WEIGHT	= Float.MAX_VALUE / 1000;
   
   /**
    * Open a document in a web browser.
    * 
    * @param purl	The address of the web document.
    */
   public static void navigate(ParsedURL purl)
   {
      Environment.the.get().navigate(purl, Environment.the.frame());
   }
   
   public static void beep()
   {
	  Toolkit.getDefaultToolkit().beep();
   }

/**
 * Find the first object equal to the one passed in as the second argument,
 * within the List that is the first argument. 
 * 
 * @param list		The list to search.
 * @param object	The object to look for. Must not be null.
 * 
 * @return		The equal object from the list, if there is one, or null.
 */
   static public Object findEqual(java.util.List list, Object object)
   {
	  synchronized (list)
	  {
		 int size		= list.size();
		 for (int i=0; i<size; i++)
		 {
			Object that	= list.get(i);
			if (that.equals(object))
			   return that;
		 }
	  }
	  return null;
   }
   
   /**
	 * Returns the environment's current 
	 * max memory settings (based on command-line parameters in the 
	 * java control panel)
	 * 
	 * @return The maximum amount of memory that can be allocated to the JVM
	 * in megabytes.
	 */
	public static int getMaxMemory()
	{
		long maxMemory		= Runtime.getRuntime().maxMemory();
	    // report in megabytes
	    return				  (int)(maxMemory / (1024 * 1024));
	}
	
	private static final String[] DIALOG_OPTIONS = { "ok" };
	
	/*
	 * Show a dialog box to the user, and then exit the VM.
	 */
	public static void showDialogAndExit(String msg, int code)
	{
		showDialog(msg);
		Generic.exit(code);
	}
	/*
	 * Show a dialog box to the user.
	 */
	public static void showDialog(String msg)
	{
		JOptionPane.showOptionDialog(null, msg, "combinFormation exited", JOptionPane.DEFAULT_OPTION, 
									JOptionPane.WARNING_MESSAGE, null, DIALOG_OPTIONS, DIALOG_OPTIONS[0]);
	}
    /**
     * Called at the end of an invocation. Calls System.exit(code).
     * 
     * @param	code -- 0 for normal. other values are application specific.
     */
    public static void exit(int code)
    {
	   Environment.the.get().exit(code);   	
    }
}
