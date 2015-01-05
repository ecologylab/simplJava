/**
 * 
 */
package ecologylab.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class runs in order to clean out unrefreshed elements of a Map on a specified interval.
 * Access to the Map should go through this object to ensure proper sychonous access.
 * 
 * Note that this object will automatically remove elements that have been in the map, and unused,
 * for too long (per the liveTime argument) and will persist for at LEAST as long as the liveTime
 * argument.
 * 
 * I'm sure there's a nice mathmematical way to explain it, but each item lasts until the next
 * cleanupInterval...if, at the interval, it has been idle for more than liveTime, it is removed and
 * Java GC will take care of it when it gets around to it.
 * 
 * This object automatically starts up a monitoring thread whenever it contains items, and shuts
 * itself off when there are no items.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class AutoCleanerMap<KEY, VALUE> extends ConcurrentHashMap<KEY, VALUE> implements Runnable
{
	private static final long	serialVersionUID	= 1L;

	HashMap<KEY, TimeTracker>	timeTrackerMap		= new HashMap<KEY, TimeTracker>();

	private long							liveTime;

	private long							cleanupInterval;

	private boolean						running						= false;

	private Thread						t;

	public AutoCleanerMap(long liveTime, long cleanupInterval)
	{
		this.liveTime = liveTime;
		this.cleanupInterval = cleanupInterval;
	}

	/**
	 * Finds the matching value, calls access for it, then returns it.
	 * 
	 * @see java.util.concurrent.ConcurrentHashMap#get(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public synchronized VALUE get(Object key)
	{
		try
		{
			KEY actualKey = (KEY) key;
			this.access(actualKey);
			return super.get(actualKey);
		}
		catch (ClassCastException e)
		{
			e.printStackTrace();
			Debug
					.println("This stupid method takes Objects instead of things bound by the appropriate parameterization of this class. Somehow, you've managed to pass it an Object that does not cast correctly. Well done...you broke it.");
			return null;
		}
	}

	private synchronized void start()
	{
		this.running = true;

		t = new Thread(this);
		t.start();
	}

	private synchronized void stop()
	{
		this.running = false;
		t.interrupt();
	}

	@Override
	public synchronized VALUE put(KEY key, VALUE value)
	{
		TimeTracker tt = new TimeTracker(key);
		// we're just letting the old time tracker get thrown away
		timeTrackerMap.put(key, tt);

		if (this.size() == 0)
		{
			this.start();
		}

		return super.put(key, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized VALUE remove(Object key)
	{
		try
		{
			KEY actualKey = (KEY) key;
			this.timeTrackerMap.remove(actualKey);

			if (this.size() == 1)
			{
				this.stop();
			}

			return super.remove(actualKey);
		}
		catch (ClassCastException e)
		{
			e.printStackTrace();
			Debug
					.println("This stupid method takes Objects instead of things bound by the appropriate parameterization of this class. Somehow, you've managed to pass it an Object that does not cast correctly. Well done...you broke it.");
			return null;
		}
	}

	public synchronized void access(KEY key)
	{
		// look up the TimeTracker object
		TimeTracker tt = timeTrackerMap.get(key);

		if (tt != null)
		{
			tt.access();
		}
	}

	private synchronized void cleanup()
	{
		long cleanupTime = System.currentTimeMillis();

		ArrayList<KEY> toRemove = new ArrayList<KEY>();

		Iterator<KEY> trackerIter = timeTrackerMap.keySet().iterator();
		while (trackerIter.hasNext())

		{
			KEY k = trackerIter.next();

			TimeTracker tt = timeTrackerMap.get(k);

			if (cleanupTime - tt.getLastAccess() > liveTime)
			{ // too old
				toRemove.add(tt.key);
			}
		}

		for (KEY k : toRemove)
		{
			this.remove(k);
			timeTrackerMap.remove(k);

			Debug.println("**********************************cleanup removed: " + k);
		}
	}

	@Override
	public void run()
	{
		Debug.println("Starting up AutoMapCleaner.");
		while (running)
		{
			try
			{
				Thread.sleep(cleanupInterval);
			}
			catch (InterruptedException e)
			{
				Thread.interrupted();
				e.printStackTrace();
			}

			Debug.println("cleaning...");
			this.cleanup();
			Debug.println("done.");
		}
		Debug.println("Stopping AutoMapCleaner.");
	}

	/**
	 * TimeTracker matches an object of type KEY to a Date. It is meant to track a Key into a Hash.
	 * 
	 * @author Zachary O. Toups (zach@ecologylab.net)
	 */
	class TimeTracker
	{
		private long	lastAccess;

		private KEY		key;

		public TimeTracker(KEY key)
		{
			this.key = key;
			lastAccess = System.currentTimeMillis();
		}

		public void access()
		{
			lastAccess = System.currentTimeMillis();
		}

		/**
		 * @return the lastAccess
		 */
		public long getLastAccess()
		{
			return lastAccess;
		}
	}
}
