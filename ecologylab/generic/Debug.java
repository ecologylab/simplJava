package cm.generic;

import java.io.*;

/**
 * A Utility Class for debugging stuff.
 * Creation date: (11/9/00 7:54:15 PM)
 * @author: 
 */
public class Debug 
{
/**
 * Hi watermark. debug() messages with a level less than or equal to this 
 * will get printed out.
 */
   public static int		level	= 5;
   public static boolean	interactiveDebug;
   
/**
 * @param	messageLevel. If less than or equal to the static level,
 * message will get logged. Otherwise, the statement will be ignored.
 */
   public static void println(int messageLevel, String message) 
   {
      println(message, messageLevel);
   }
   public static void println(String message, int messageLevel) 
   {
      if (messageLevel <= level)
	 println(message);
   }
   public static void printlnI(String message, int messageLevel) 
   {
      if (interactiveDebug)
	 println(message);
   }
   public static void printlnI(Object o, String message)
   {
      printlnI(o, message, level);
   }
   public static void println(Object o, String message)
   {
      println(o, message, level);
   }
   public static void debug(Object o, String message, Exception e)
   {
      println(o, message);
      e.printStackTrace();
   }
   public static void printlnI(Object o, String message, int messageLevel)
   {
      printlnI(o + "." + message, messageLevel);
   }
   public static void println(Object o, String message, int messageLevel)
   {
      println(o + "." + message, messageLevel);
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
   public static void printlnA(Object o, String message) 
   {
      println(getClassName(o)+"." + message);
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
      String c	= thatClass.toString();
      return c.substring(c.lastIndexOf(".") + 1);
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
 * but only if messageLevel is greater than <code>level</code> (see above).
 */
   public void debug(String message, int messageLevel)
   {
      println(this, message, messageLevel);
   }
   public void debugI(String message, int messageLevel)
   {
      printlnI(this, message, messageLevel);
   }
   public static void toggleInteractive()
   {
      interactiveDebug	= !interactiveDebug;
   }
}
