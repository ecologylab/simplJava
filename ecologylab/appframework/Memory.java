/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package cm.generic;

/**
 * Utility routines related to memory management, used to watch the
 * JVM's consumption of memory, and to kick the garbage collector into action.
 */
public class Memory
{
/**
 * Less than this many bytes of memory free means danger, baby.
 */
   public static final int	DANGER_THRESHOLD= 7000 * 1024;
   static Runtime		runtime		= Runtime.getRuntime();
/**
 * Number of times we've called gc().
 */
   public static int		gcCount;

   public static boolean	isMicroshaftVM;
   
   static StringBuffer buffy = new StringBuffer(256);
/**
 * Prod the garbage collector, and print a message about memory status.
 */
   public static void reclaim()
   {
      reclaim("");
   }
/**
 * Prod the garbage collector, and print a message about memory status.
 * 
 * @param		Part of the message to be printed, to identify call site.
 */
   public static void reclaim(String s)
   {
      try
      {
		 StringTools.clear(buffy);
		 buffy.append("\nMemory.reclaim(")
			.append(Thread.currentThread().getName())
			   .append(".gc(").append(s).append("): ").append(usage());
		 reclaimQuiet();
		 buffy.append(" -> ").append(usage()).append(" #").append(gcCount)
			.append("\n");
		 Debug.println(buffy);
      } catch (Exception e)
      {
		 e.printStackTrace();
      }
   }
   public static void reclaimQuiet()
   {
      System.gc();
      System.runFinalization();
      gcCount++;
   }
/**
 * Try to reclaim memory if it seems to be in low supply.
 * @return	true if memory status is in danger and more agressive
 * measures are called for.
 */
   public static boolean reclaimIfLow()
   {
      for (int i=0; i!=5; i++)
      {
		 boolean danger	= (runtime.freeMemory() < DANGER_THRESHOLD)
			&& (!isMicroshaftVM || (Math.random() > .9));
		 if (danger)
			reclaimQuiet();
		 else
			break;
      }
      return runtime.freeMemory() < DANGER_THRESHOLD;
   }
   
   public static void recover(Throwable throwable, String msg)
   {
      Memory.reclaimQuiet();
      Debug.println("Memory.recover()");
      if (msg != null)
		 Memory.reclaim(msg);
      throwable.printStackTrace();
	  //      Thread.dumpStack();
      Debug.println(Memory.threads());
      Debug.println("");
   }
   public static String usage()
   {
      return K(runtime.freeMemory()) + " free of " + K(runtime.totalMemory());
   }
   public static String K(long numBytes)
   {
      return (numBytes/1024) + "K";
   }
   public static String threads()
   {
      Thread current	= Thread.currentThread();
      int count		= current.activeCount();
      Thread[] threads = new Thread[count];
      current.enumerate(threads);
      String result	= count + " Threads ACTIVE\n";
      for (int i=0; i!=count; i++)
      {
		 Thread t	= threads[i];
		 if (t != null)
		 {
			//	    String alive = t.isAlive() ? "alive" : "dead";
			result		+= threads[i].getName() + "\n";
		 }
      }
      return result;
   }
   static int		outOfMemoryCount;
   static boolean	processingOutOfMemory;
   static final int	ENOUGH_OUT_OF_MEMORY_CALLS	= 10;
   
   /**
	* @return true if its time to give up!
	*/
   public static boolean outOfMemory(Throwable throwable)
   {
      if (outOfMemoryCount >= ENOUGH_OUT_OF_MEMORY_CALLS)
		 return true;
      
      if (!processingOutOfMemory)
      {  // dont bother locking, cause we're too desparate
		 processingOutOfMemory	= true;
		 outOfMemoryCount++;
		 Memory.recover(throwable, null);
		 processingOutOfMemory	= false;
      }	 
      return false;
   }
}
