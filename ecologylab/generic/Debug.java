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
   public static String getClass(Object o)
   {
      String c	= o.getClass().toString();
      return c.substring(c.lastIndexOf(".") + 1);
   }
   public void debug(String message)
   {
      println(this, message);
   }
   public void debugI(String message)
   {
      printlnI(this, message);
   }
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
