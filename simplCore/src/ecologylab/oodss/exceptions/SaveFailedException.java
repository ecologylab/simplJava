/**
 * 
 */
package ecologylab.oodss.exceptions;

/**
 * Indicates that a call to save() failed. The actual exception that caused the failure is provided.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class SaveFailedException extends Exception
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * 
	 */
	public SaveFailedException()
	{
		super();
	}

	/**
	 * @param message
	 * @param cause
	 */
	public SaveFailedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * @param message
	 */
	public SaveFailedException(String message)
	{
		super(message);
	}

	/**
	 * @param cause
	 */
	public SaveFailedException(Throwable cause)
	{
		super(cause);
	}
}
