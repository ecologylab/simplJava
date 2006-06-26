package ecologylab.generic;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.net.*;
import java.io.*;

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
 * Get a boolean parameter from the Environment. Environment is an interface that enables
 * consistent runtime preferences to be passed into code from diverse circumstances, such as
 * applets, applications, and servlets. <p/>
 * If the value is the
 * string <code>true</code> or <code>yes</code>, the result is 
 * <code>true</code>; else false.
 * 
 * @param	name	The name of the parameter's key.
 */
   public static boolean parameterBool(String name)
   {
      String value	= parameter(name);
      boolean result	= value != null;
      if (result)
		 result		=  value.equalsIgnoreCase("true") ||
			value.equalsIgnoreCase("yes") || (value.equals("1"));
      return result;
   }
   public static boolean parameterBool(String name, boolean defaultValue)
   {
      String param	= parameter(name);
      boolean result;
      if (param != null)
		 result		=  param.equalsIgnoreCase("true") ||
			param.equalsIgnoreCase("yes") || (param.equals("1"));
      else
		 result		= defaultValue;
      return result;
   }
/**
 * Get an integer from the Environment. Environment is an interface that enables
 * consistent runtime preferences to be passed into code from diverse circumstances, such as
 * applets, applications, and servlets. <p/>
 * The default is 0.
 * 
 * @param	paramName	The name of the parameter's key.
 */
   public static int parameterInt(String paramName)
   { return parameterInt(paramName, 0); }
   
/**
 * Get an integer parameter  from the Environment. Environment is an interface that enables
 * consistent runtime preferences to be passed into code from diverse circumstances, such as
 * applets, applications, and servlets. <p/> 
 * 
 * @param	paramName	The name of the parameter's key.
 * @param	defaultValue	Default integer value, in case param is 
 *				unspecified in the runtime env.
 */
   public static int parameterInt(String paramName, int defaultValue)
   {
      String paramValue	= parameter(paramName);
      int result	= defaultValue;
      if (paramValue != null)
		 try
		 {
			result	= Integer.parseInt(paramValue);
		 } catch (NumberFormatException e)
		 {
			Debug.println("bad number format: "+paramName+"="+paramValue);
		 }
      return result;
   }
/**
 * Get a float parameter  from the Environment. Environment is an interface that enables
 * consistent runtime preferences to be passed into code from diverse circumstances, such as
 * applets, applications, and servlets. <p/>
 * 
 * @param	paramName	The name of the parameter's key.
 * @param	defaultValue	Default floating point value, in case param is 
 *				unspecified in the runtime env.
 */
   public static float parameterFloat(String paramName, float defaultValue)
   {
      String paramValue	= parameter(paramName);
      float result	= defaultValue;
      if (paramValue != null)
      {
		 float parsedValue	= Generic.parseFloat(paramValue);
		 if (!Float.isNaN(parsedValue))
			result	= parsedValue;
      }
      return result;
   }
   public static Color parameterColor(String param)
   {	
      String s = parameter(param);
      return (s != null) ? Palette.hexToColor(s) : null;
   }

   public static final String parameter(String paramName)
   {
      return Environment.the.get().parameter(paramName);
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
/**
 * Find the path to system files.
 * Change return value from URL to ParsedURL.
 */
   public static ParsedURL systemPath(String relativePath)
   {
   	  return Environment.the.get().codeBase().getRelative(relativePath, "forming system path");
//      return Environment.the.get().codeRelativeURL(relativePath);
   }
   public static final String SEP	= "/";
   
   /* Change return value from URL to ParsedURL. */
   public static ParsedURL systemPhotoPath(String relativePath)
   {
      String photoPathParam	= parameter("photo_path");
      if (photoPathParam == null)
		 throw new RuntimeException("Generic configuration ERROR! Startup parameter photo_path is not defined.");
      String sep = photoPathParam.endsWith(SEP) || relativePath.startsWith(SEP)
		 ? "" : SEP;
      return systemPath(photoPathParam + sep + relativePath);
   }

/**
 * @return	The version of Java we're using (but not the specific release),
 *		as in 1.2, 1.3, 1.4,...
 */
   public static float javaVersion()
   {
      return Environment.the.javaVersion();
   }
   /**
    * @return The version of Java we're using (with the specific release)
    */
   public static String javaVersionFull()
   {
	   return System.getProperty("java.version");
   }
   public static boolean hasXML()
   {
      return Environment.the.hasXML();
   }
   public static final void propogateValues(Rectangle src, Rectangle dest)
   {
      dest.x		= src.x;
      dest.y		= src.y;
      dest.width	= src.width;
      dest.height	= src.height;
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

   /**
    * Create a HashMap, and populate with entries from the passed in array of Strings.
    * The key and value will be the same for each entry.
    * @param strings
    * @return
    */
   public static final HashMap buildHashMapFromStrings(String[] strings)
   {
      HashMap hashMap	= new HashMap(strings.length);
      buildMapFromStrings(hashMap, strings);
      return hashMap;
   }
   /**
    * Create a HashMap, and populate with entries from the passed in array of Strings.
    * But only use Strings that are lower case; other entries in the array are ignored.
    * The key and value will be the same for each entry.
    * @param strings
    * @return
    */
   public static final HashMap buildHashMapFromLCStrings(String[] strings)
   {
      HashMap hashMap	= new HashMap(strings.length);
      buildMapFromLCStrings(hashMap, strings);
      return hashMap;
   }

   /**
    * Create a HashMap, and popuplate with entries from the passed in array of
    * key value pairs.
    * @param entries
    * @return
    */
   public static final HashMap buildHashMap(Object[][] entries)
   {
      HashMap hashMap	= new HashMap(entries.length);
      buildMap(hashMap, entries);
      return hashMap;
   }
   /**
    * Populate a HashMap from the supplied array of Strings.
    * The key and value will be the same for each entry.
    * @param map
    * @param strings
    */
   public static final void buildMapFromStrings(Map map, String[] strings)
   {
      for (int i=0; i<strings.length; i++)
      {
		 String thatString	= strings[i];
		 map.put(thatString, thatString);
      }
   }
   /**
    * Populate a HashMap from the supplied array of Strings.
    * But only use Strings that are lower case; other entries in the array are ignored.
    * The key and value will be the same for each entry.
    * @param map
    * @param strings
    */
   public static final void buildMapFromLCStrings(Map map, String[] strings)
   {
      for (int i=0; i<strings.length; i++)
      {
		 String thatString	= strings[i];
		 if (StringTools.isLowerCase(thatString))
			 map.put(thatString, thatString);
      }
   }
   public static final void buildMap(Map map, Object[][] entries)
   {
      for (int i=0; i<entries.length; i++)
      {
    	 Object[] thatEntry	= entries[i];
    	 Object thatKey		= thatEntry[0];
    	 Object thatValue	= thatEntry[1];
		 map.put(thatKey, thatValue);
      }
   }
   
   // The keys come from the String[], the values are corresponding number start with 0
   public static final HashMap buildNumberHashMapFromStrings(String[] strings)
   {
      HashMap hashMap	= new HashMap(strings.length);
      buildNumberMapFromStrings(hashMap, strings);
      return hashMap;
   }
   
   // The keys come from the String[], the values are corresponding number start with 0
   public static final void buildNumberMapFromStrings(Map map, String[] strings)
   {
      for (int i=0; i<strings.length; i++)
      {
		 String thatString	= strings[i];
		 Integer integer = new Integer(i);
		 map.put(thatString, integer);
      }
   }
   
   public static final void stringIntMapEntry(Map map,
					      String string, int integer)
   {
      map.put(string, new IntSlot(integer));
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
   public static void go(ParsedURL purl)
   {
      Environment.the.get().go(purl, Environment.the.frame());
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
	
	/**
	 * Creates a new ParsedURL from a string and returns null if unsuccessful
	 * 
	 * @param address The address string representation
	 * @return The new ParsedURL
	 */
	public static ParsedURL getParsedURLFromString(String address)
	{
		try
		{
			return new ParsedURL(new URL(address));
		}
		catch (Exception e)
		{
			return null;
		}
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
