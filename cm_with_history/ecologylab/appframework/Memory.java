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
   public static void reclaim()
   {
      reclaim("");
   }
   public static void reclaim(String s)
   {
      try
      {
	 buffy.setLength(0);
	 buffy.append("\nMemory.reclaim(")
	    .append(Thread.currentThread().getName())
	       .append(".gc(").append(s).append("): ").append(usage());
	 reclaimQuiet();
	 buffy.append(" -> ").append(usage()).append(" #").append(gcCount)
	    .append("\n");
	 System.out.println(buffy);
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
      System.out.println("Memory.recover()");
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
}
