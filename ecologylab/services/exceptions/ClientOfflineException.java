/**
 * 
 */
package ecologylab.services.exceptions;

/**
 * Exception indicating that an operation failed because the client is no longer connected.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class ClientOfflineException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * 
	 */
	public ClientOfflineException()
	{
	}

	/**
	 * @param arg0
	 */
	public ClientOfflineException(String arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 */
	public ClientOfflineException(Throwable arg0)
	{
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public ClientOfflineException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

}
