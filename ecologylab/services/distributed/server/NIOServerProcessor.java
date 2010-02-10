/**
 * 
 */
package ecologylab.services.distributed.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

import ecologylab.generic.StartAndStoppable;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager;
import ecologylab.services.exceptions.BadClientException;

/**
 * An interface indicating necessary functionality for a server so that it can
 * properly service ContextManagers.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public interface NIOServerProcessor extends StartAndStoppable
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
	void processRead(Object sessionToken, NIOServerIOThread base, SelectionKey sk,
			ByteBuffer bs, int bytesRead) throws BadClientException;

	/**
	 * Performs any internal actions that should be taken whenever a client is
	 * disconnected.
	 * 
	 * @param token
	 *           the identifier for the client's connection (key.attachment(),
	 *           normally)
	 * @param forcePermanent TODO
	 * @return true if the client is permanently disconnecting
	 */
	boolean invalidate(Object token, boolean forcePermanent);

	/**
	 * Attempts to switch the ContextManager for a SocketChannel. oldId indicates
	 * the session id that was used for the connection previously (in order to
	 * find the correct ContextManager) and newContextManager is the
	 * recently-created (and now, no longer necessary) ContextManager for the
	 * connection.
	 * 
	 * @param oldId
	 * @param newContextManager
	 * @return true if the restore was successful, false if it was not.
	 */
	public boolean restoreContextManagerFromSessionId(String oldId,
			AbstractClientSessionManager newContextManager);
}
