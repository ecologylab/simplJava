/**
 * 
 */
package ecologylab.services.distributed.exception;

/**
 * An exception that indicates that a message was too large; typically this
 * means that a message to be sent from a client to a server is too large for
 * the client's buffer and will likely be rejected by the server.
 * 
 * @author Zachary O. Toups (toupsz@ecologylab.net)
 */
public class MessageTooLargeException extends Exception
{
	private static final long	serialVersionUID	= 1732834475978273620L;

	private int						maxMessageSize;

	private int						actualMessageSize;

	/**
	 * @return the maxMessageSize
	 */
	public int getMaxMessageSize()
	{
		return maxMessageSize;
	}

	/**
	 * @return the actualMessageSize
	 */
	public int getActualMessageSize()
	{
		return actualMessageSize;
	}

	/**
	 * 
	 */
	public MessageTooLargeException(int maxMessageSize, int actualMessageSize)
	{
		this("", maxMessageSize, actualMessageSize);
	}

	/**
	 * @param message
	 */
	public MessageTooLargeException(String message, int maxMessageSize,
			int actualMessageSize)
	{
		super(message);

		this.maxMessageSize = maxMessageSize;
		this.actualMessageSize = actualMessageSize;
	}

	/**
	 * @param cause
	 */
	public MessageTooLargeException(Throwable cause)
	{
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MessageTooLargeException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
