/*
 * Copyright 1996-2002 by Andruid Kerne. All rights reserved.
 * CONFIDENTIAL. Use is subject to license terms.
 */
package ecologylab.appframework;

import ecologylab.generic.Debug;
import ecologylab.generic.StringTools;

/**
 * Utility routines related to memory management, used to watch the
 * JVM's consumption of memory, and to kick the garbage collector into action.
 */
public class Memory
{
//	private static final int KICK_GC_COUNT = 5;
	private static final int KICK_GC_COUNT = 3;

	/**
	 * Less than this many bytes of memory free means danger, baby.
	 * Currently set at 32M.
	 */
	public static final int	DANGER_THRESHOLD	= 32 * 1024 * 1024;

	public static final int	RECLAIM_THRESHOLD	= 2 * DANGER_THRESHOLD;  

	static final Runtime		RUNTIME				= Runtime.getRuntime();
	/**
	 * Number of times we've called gc().
	 */
	public static int		gcCount;

	public static boolean	isMicroshaftVM;

	static long				gcTimeStamp;

	static StringBuffer 		buffy				= new StringBuffer(256);

	static final int			GC_MAX_DELTA_T		= 30 * 1000;	// 30 seconds
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
	 * @param s		Part of the message to be printed, to identify call site.
	 */
	public static synchronized void reclaim(String s)
	{
		try
		{
			StringTools.clear(buffy);
			buffy.append("\nMemory.reclaim(")
			.append(Thread.currentThread().getName())
			.append(".gc(").append(s).append("):\n\t").append(usage());
			reclaimQuiet();
			buffy.append(" -> ").append(usage()).append(" #").append(gcCount)
			.append("\n");
			Debug.println(buffy);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	public static synchronized void reclaimQuiet()
	{
		gcTimeStamp	= System.currentTimeMillis(); 
		System.gc();
		gcCount++;
	}


	private static final Object MEMORY_RECLAIM_LOCK = new Object();

	/**
	 * Try to reclaim memory if it seems to be in low supply.
	 * Kicks the garbage collector, perhaps repeatedly.
	 * Does this even though GC is supposed to be automatic.
	 * <p/>
	 * Also maintains the current time stamp to the one in which the garbage collector was last kicked.
	 * To avoid maxing out the CPU calling GC repeatedly, will not kick GC again if the last time was recent.
	 * (Currently, recent means within the last 30 seconds.)
	 * 
	 * @return	true 	if memory status is in danger and more agressive measures are called for.
	 * 			false 	if everything is fine, and the caller can proceed to perform operations that
	 * 					use lots of memory, as needed.
	 * 
	 */
	public static boolean reclaimIfLow()
	{
		synchronized (MEMORY_RECLAIM_LOCK)
		{
			//  this lock  pushes memory check (and the expensive reclaim part) into one thread at a time.

			long now		= System.currentTimeMillis();
			long deltaT	= now - gcTimeStamp;
			if (deltaT >= GC_MAX_DELTA_T)
			{
				for (int i=0; i!=KICK_GC_COUNT; i++)
				{
					if (RUNTIME.freeMemory() < RECLAIM_THRESHOLD)
						reclaim();
					else
						break;
				}
			}
		}
		return RUNTIME.freeMemory() < DANGER_THRESHOLD;
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
		return getFreeMemoryInK() + " free of " + K(RUNTIME.totalMemory());
	}
	public static String K(long numBytes)
	{
		return (numBytes/1024) + "K";
	}
	public static String threads()
	{
		Thread current	= Thread.currentThread();
		int count		= Thread.activeCount();
		Thread[] threads = new Thread[count];
		Thread.enumerate(threads);
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

	public static long getFreeMemoryInBytes()
	{
		return RUNTIME.freeMemory();
	}

	public static String getFreeMemoryInK()
	{
		return K(getFreeMemoryInBytes());
	}
}
