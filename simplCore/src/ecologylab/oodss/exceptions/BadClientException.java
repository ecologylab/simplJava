/**
 * 
 */
package ecologylab.oodss.exceptions;

import java.util.HashMap;

import ecologylab.generic.Debug;

/**
 * Throw this Exception when we detect that the client is evil or lame.
 * 
 * @author andruid
 * 
 */
public class BadClientException extends Exception
{
	/**
	 * 
	 */
	private static final long								serialVersionUID				= 1652784829579621254L;

	private static HashMap<String, EvilHostEntry>	evilHostsMap					= new HashMap<String, EvilHostEntry>();

	/**
	 * Time for which a seemingly evil host gets locked out. Currently 20 minutes.
	 */
	public static final long								LOCKOUT_INTERVAL				= 20 * 60 * 1000;

	/**
	 * Time in which bad responses from a host define it as evil host. Currently 1 minute.
	 */
	public static final long								REPEAT_OFFENDER_INTERVAL	= 60 * 1000;

	public static final int									BAD_OFFENSE_THRESHOLD		= 3;

	// TODO make all call sites pass IP number in
	// TODO make call site in timeoutBeforeValidMsg also pass timeStamp in.

	/**
	 * Report that the client has behaved badly, by sending an improperly formed message.
	 * 
	 * @param message
	 */
	public BadClientException(String ipNumber, String message)
	{
		this(ipNumber, System.currentTimeMillis(), message);
	}

	/**
	 * Report that the client has behaved badly, by timing out.
	 * 
	 * @param message
	 */
	public BadClientException(String ipNumber, long timeStamp, String message)
	{
		super(message);
		badClientIncident(ipNumber, timeStamp);
		Debug.println(ipNumber + " flagged because " + message);
	}

	static class EvilHostEntry
	{
		long	timeStamp;

		int	count;

		EvilHostEntry(long timeStamp)
		{
			this.timeStamp = timeStamp;
		}

		EvilHostEntry()
		{
			this(System.currentTimeMillis());
		}

		void badClientIncident(long newTimeStamp)
		{
			count++;
			this.timeStamp = newTimeStamp;
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

	static class OkEvilHostEntry extends EvilHostEntry
	{
		OkEvilHostEntry()
		{

		}

		@Override boolean isEvil()
		{
			return false;
		}
	}

	static final EvilHostEntry	OK_HOST_ENTRY	= new OkEvilHostEntry();

	public static boolean isEvilHostByNumber(String ipNumber)
	{
		Debug.println("I'm looking up " + ipNumber);
		for (String s : evilHostsMap.keySet())
		{
			Debug.println(s);
		}
		EvilHostEntry entry = evilHostsMap.get(ipNumber);
		if (entry == null)
		{
			synchronized (evilHostsMap)
			{
				entry = evilHostsMap.get(ipNumber);
				if (entry == null)
				{
					entry = OK_HOST_ENTRY;
					evilHostsMap.put(ipNumber, OK_HOST_ENTRY);
				}
			}
		}

		Debug.println(ipNumber + " is evil? " + entry.isEvil());

		return entry.isEvil();
	}

	/**
	 * Register a BadClientException incident for this host.
	 * 
	 * @param ipNumber
	 *           Host that was bad.
	 * @param timeStamp
	 *           When the bad event occurred
	 * 
	 * @return true if now the host is considered to be evil.
	 */
	private static boolean badClientIncident(String ipNumber, long timeStamp)
	{
		Debug.println("client at " + ipNumber + " was naughty and is going into timeout.");
		EvilHostEntry entry = evilHostsMap.get(ipNumber);
		if ((entry == null) || (entry == OK_HOST_ENTRY))
		{
			synchronized (evilHostsMap)
			{
				entry = evilHostsMap.get(ipNumber);
				if ((entry == null) || (entry == OK_HOST_ENTRY))
				{
					entry = new EvilHostEntry(timeStamp);
					evilHostsMap.put(ipNumber, entry);
				}
			}
		}
		entry.badClientIncident(timeStamp);
		return entry.isEvil();

	}
}
