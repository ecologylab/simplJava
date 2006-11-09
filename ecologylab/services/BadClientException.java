/**
 * 
 */
package ecologylab.services;

import java.util.HashMap;

/**
 * Throw this Exception when we detect that the client is evil or lame.
 * 
 * @author andruid
 *
 */
public class BadClientException extends Exception
{
	private static HashMap<String, EvilHostEntry>	evilHostsMap	= 
		new HashMap();
	
	/**
	 * Time for which a seemingly evil host gets locked out.
	 * Currently 20 minutes.
	 */
	public static final long LOCKOUT_INTERVAL	= 20 * 60 * 1000;
	
	/**
	 * Time in which bad responses from a host define it as evil host.
	 * Currently 1 minute.
	 */
	public static final long REPEAT_OFFENDER_INTERVAL	=  60 * 1000;
	
	public static final int	 BAD_OFFENSE_THRESHOLD		= 3;
	
	//TODO make all call sites pass IP number in
	//TODO make call site in timeoutBeforeValidMsg also pass timeStamp in.
	//TODO should ipNumber be of type InetAddress, instead of String ?!

	/**
	 * Report that the client has behaved badly, by sending an improperly formed message.
	 * @param message
	 */
	//TODO -- get rid of this constructor
	public BadClientException(String message)
	{
		super(message);
		// badClientIncident(ipNumber, timeStamp)
	}
	/**
	 * Report that the client has behaved badly, by sending an improperly formed message.
	 * 
	 * @param message
	 */
	public BadClientException(String ipNumber, String message)
	{
		super(message);
		badClientIncident(ipNumber);
	}
	/**
	 * Report that the client has behaved badly, by timing out.
	 * @param message
	 */
	public BadClientException(String ipNumber, long timeStamp, String message)
	{
		super(message);
		badClientIncident(ipNumber, timeStamp);
	}

	static class EvilHostEntry
	{
		long	timeStamp;
		int		count;
		
		EvilHostEntry(long timeStamp)
		{
			this.timeStamp	= timeStamp;
		}
		EvilHostEntry()
		{
			this(System.currentTimeMillis());
		}
		void badClientIncident(long timeStamp)
		{
			count++;
			this.timeStamp	= timeStamp;
		}
		void badClientIncident()
		{
			badClientIncident(System.currentTimeMillis());
		}
		boolean isEvil()
		{
			return (System.currentTimeMillis() - timeStamp) < LOCKOUT_INTERVAL;
		}
	}
	static final EvilHostEntry OK_HOST_ENTRY = 
		new EvilHostEntry(System.currentTimeMillis() - REPEAT_OFFENDER_INTERVAL);
	
	public static boolean isEvilHostByNumber(String ipNumber)
	{
		EvilHostEntry entry		= evilHostsMap.get(ipNumber);
		if (entry == null)
		{
			synchronized (evilHostsMap)
			{
				entry		= evilHostsMap.get(ipNumber);
				if (entry == null)
				{
					entry	= OK_HOST_ENTRY;
					evilHostsMap.put(ipNumber, OK_HOST_ENTRY);
				}
			}
		}
		// use double negatives here so that if condition 1 is true, we dont bother executing condition 2
		return !((entry == OK_HOST_ENTRY) || !entry.isEvil());
	}
	
	/**
	 * Register a BadClientException incident for this host.
	 * A timestamp of now is generated.
	 * 
	 * @param ipNumber		Host that was bad.
	 * 
	 * @return	true if now the host is considered to be evil.
	 */
	private static boolean badClientIncident(String ipNumber)
	{
		return badClientIncident(ipNumber, System.currentTimeMillis());
	}
	/**
	 * Register a BadClientException incident for this host.
	 * 
	 * @param ipNumber		Host that was bad.
	 * @param timeStamp		When the bad event occurred
	 * 
	 * @return	true if now the host is considered to be evil.
	 */
	private static boolean badClientIncident(String ipNumber, long timeStamp)
	{
		EvilHostEntry entry		= evilHostsMap.get(ipNumber);
		if ((entry == null) || (entry == OK_HOST_ENTRY))
		{
			synchronized (evilHostsMap)
			{
				entry		= evilHostsMap.get(ipNumber);
				if ((entry == null) || (entry == OK_HOST_ENTRY))
				{
					entry		= new EvilHostEntry(timeStamp);
					evilHostsMap.put(ipNumber, entry);
				}
			}
		}
		entry.badClientIncident(timeStamp);
		return entry.isEvil();
		
	}
}
