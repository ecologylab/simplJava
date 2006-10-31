/**
 * 
 */
package ecologylab.services;

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

}
