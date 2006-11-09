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
	
	//TODO add call site that passes timeStamp in.
	/**
	 * 
	 */
	public BadClientException()
	{
		super();
	}

	/**
	 * @param arg0
	 */
	public BadClientException(String arg0)
	{
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BadClientException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	/**
	 * @param arg0
	 */
	public BadClientException(Throwable arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
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
		return !((entry == OK_HOST_ENTRY) || !entry.isEvil());
	}
}
