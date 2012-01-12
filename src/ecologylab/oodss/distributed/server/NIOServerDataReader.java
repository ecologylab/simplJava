/**
 * 
 */
package ecologylab.oodss.distributed.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import ecologylab.oodss.distributed.impl.NIOServerIOThread;
import ecologylab.oodss.exceptions.BadClientException;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 *
 */
public interface NIOServerDataReader extends NIOServerProcessor
{

	/**
	 * Handles passing incoming bytes to the appropriate ClientSessionManager.
	 * 
	 * @param sessionToken		Identifies which ClientSessionManager to pass the bytes to. 
	 * 							If this is the first time a new ClientSessionManger will be constructed.
	 * @param base
	 * @param sk
	 * @param bs
	 * @param bytesRead
	 * @throws BadClientException
	 */
	public abstract void processRead(Object sessionToken, NIOServerIOThread base, SelectionKey sk,
			ByteBuffer bs, int bytesRead) throws BadClientException;

}