package cm.generic;

import java.util.*;

/**
 * @author andruid
 * 
 * A developer-friendly base class and tools set for printing debug messages.
 * 
 * Supports a threshold, aka <code>level</code> with 2 levels of granularity:
 *	1) global	<br>
 *	2) on a per class basis	<br>
 * 
 * This levels are configured via runtime startup params 
 * ( via the JavaScript prefs mechanisms for  applet versions)
 * 
 * in the form of
 *	1) 
 *		debug_global_level = 4;
 *	2) 
 *		debug_levels	= "Parser 3; HTMLPage 2; CollageOp 37";
 */
public class Debug 
{
/**
 * Global hi watermark. debug() messages with a level less than or equal
 *  to this will get printed out.
 */
   public static int		level	= 5;
   public static boolean	interactiveDebug;
   
   static final HashMap		classAbbrevNames	= new HashMap();
/**
 * Holds class specific debug levels.
 */
   static final HashMap		classLevels	= new HashMap();

   public static void initialize()
   {
      // global
      Debug.level	= Generic.parameterInt("debug_global_level", 0);
      
      // class specific
      String levels	= Generic.parameter("debug_levels");
      println("Debug.initialize(" + levels);
      if (levels != null)
      {
	 StringTokenizer tokenizer	= new StringTokenizer(levels,";");
	 {
	    try
	    {
	       while (tokenizer.hasMoreTokens())
	       {
		  String thisSpec		= tokenizer.nextToken();
		  StringTokenizer specTokenizer= new StringTokenizer(thisSpec);
		  try
		  {
		     String thisClassName	= specTokenizer.nextToken();
		     int thisLevel		=
			Integer.parseInt(specTokenizer.nextToken());
		     Debug.println("Debug.level\t" + thisClassName + "\t" +
				   thisLevel);
		     classLevels.put(thisClassName,
				     new IntSlot(thisLevel));
		  } catch (Exception e)
		  {
		  }
	       }
	    } catch (NoSuchElementException e)
	    {
	    }
	 }
      }
   }
   public final int level()
   {
      return level(this);
   }
   public static final int level(Object that)
   {
      int result	= level;
      IntSlot slot	= (IntSlot) classLevels.get(getClassName(that));
      if (slot != null)
	 result		= slot.value;
      return result;
   }
/**
 * @param	messageLevel. If less than or equal to the static level,
 * message will get logged. Otherwise, the statement will be ignored.
 */
   public static void println(int messageLevel, String message) 
   {
      if (messageLevel <= level)
	 println(message);
   }
   public static void printlnI(int messageLevel, String message) 
   {
      if (interactiveDebug)
	 println(message);
   }
   public static void println(Object o, String message)
   {
      println(o + "." + message);
   }
   public static void printlnI(Object o, String message)
   {
      if (interactiveDebug)
	 println(o, message);
   }
   public static void printlnI(String message) 
   {
      if (interactiveDebug)
	 println(message);
   }
   public static void println(String message) 
   {
      System.out.println(message);
   }
   public static void print(String message) 
   {
      System.out.print(message);
   }
/**
 * Print a debug message, starting with the abbreviated class name of
 * the object.
 */
   public static void printlnA(Object that, String message) 
   {
      println(getClassName(that)+"." + message+" " +level(that));
   }
/**
 * Print a debug message, starting with the abbreviated class name.
 */
   public static void printlnA(Class c, String message) 
   {
      println(getClassName(c)+"." + message);
   }

/**
 * @return   the abbreviated name of the class - without the package qualifier.
 */
   public static String getClassName(Class thatClass)
   {
      String fullName	= thatClass.toString();
      String abbrevName	= (String) classAbbrevNames.get(fullName);
      if (abbrevName == null)
      {
	 abbrevName	= fullName.substring(fullName.lastIndexOf(".") + 1);
	 synchronized (classAbbrevNames)
	 {
	    classAbbrevNames.put(fullName, abbrevName);
	 }
      }
      return abbrevName;
   }
/**
 * @return   the abbreviated name of the class - without the package qualifier.
 */
   public static String getClassName(Object o)
   {
      return getClassName(o.getClass());
   }
/**
 * @return  the abbreviated name of this class - without the package qualifier.
 */
   public String getClassName()
   {
      return getClassName(this);
   }
   public String toString()
   {
      return getClassName(this);
   }
/**
 * Print a debug message that starts with this.toString().
 */
   public void debug(String message)
   {
      println(this, message);
   }
/**
 * Print a debug message that starts with the abbreviated class name of this.
 */
   public void debugA(String message)
   {
      printlnA(this, message);
   }
   public void debugI(String message)
   {
      printlnI(this, message);
   }
/**
 * Print a debug message that starts with the abbreviated class name of this,
 * but only if messageLevel is greater than the debug <code>level</code> for
 * this class (see above).
 */
   public void debug(int messageLevel, String message)
   {
      if (messageLevel <= level())
	 println(this, message);
   }
   public void debugA(int messageLevel, String message)
   {
      if (messageLevel <= level())
	 printlnA(this, message);
   }
   public static void println(Object that, int messageLevel, String message)
   {
      if (messageLevel <= level(that))
	 println(that, message);
   }
   public static void printlnA(Object that, int messageLevel, String message)
   {
      if (messageLevel <= level(that))
	 printlnA(that, message);
   }
   public static void printlnI(Object that, int messageLevel, String message)
   {
      if (messageLevel <= level(that))
	 printlnI(that, message);
   }
   public void debugI(int messageLevel, String message)
   {
      if (messageLevel <= level())
	 printlnI(this, message);
   }
   public static void debug(Object o, String message, Exception e)
   {
      println(o, message);
      e.printStackTrace();
   }

   public static void toggleInteractive()
   {
      interactiveDebug	= !interactiveDebug;
   }
}
