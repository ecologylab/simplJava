package ecologylab.generic;

import ecologylab.platformspecifics.FundamentalPlatformSpecifics;

//import java.awt.Toolkit;

//import javax.swing.JOptionPane;


/**
 * A set of generic convenience methods for doing things like getting
 * typed parameters from the environment, getting other stuff from
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

   public static final String SEP	= "/";
   
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
   
   public static void beep()
   {
	   FundamentalPlatformSpecifics.get().beep();
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
	 * Show a dialog box to the user.
	 */
	public static void showDialog(String msg)
	{
		FundamentalPlatformSpecifics.get().showDialog(msg, DIALOG_OPTIONS);
	}
	
    public static String narrowFloat(float f)
	   {
	      String s	= Float.toString(f);
	      if (s.length() > 7)
			 s	= s.substring(0,7);
	      return s;
	   }
}
