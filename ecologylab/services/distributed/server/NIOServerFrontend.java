/**
 * 
 */
package ecologylab.services.distributed.server;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import ecologylab.generic.StartAndStoppable;
import ecologylab.services.distributed.impl.NIOServerBackend;
import ecologylab.services.distributed.server.contextmanager.AbstractContextManager;
import ecologylab.services.exceptions.BadClientException;

/**
 * An interface indicating necessary functionality for a server so that it can properly service ContextManagers.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public interface NIOServerFrontend extends StartAndStoppable
{
	/**
	 * @param base
	 * @param sc
	 * @param bs
	 * @param bytesRead
	 * @throws BadClientException
	 */
	void processRead(Object token, NIOServerBackend base, SocketChannel sc, byte[] bs, int bytesRead)
			throws BadClientException;

	/**
	 * Performs any internal actions that should be taken whenever a client is disconnected.
	 * 
	 * @param token
	 * @param base
	 * @param sc
	 * @param permanent -
	 *           indicates whether the NIOServerFrontend should destroy the context manager object associated with the
	 *           given connection.
	 * @return the ContextManager object associated with sc that has been removed from the system.
	 */
	AbstractContextManager invalidate(Object token, NIOServerBackend base, SocketChannel sc, boolean permanent);

	/**
	 * Attempts to switch the ContextManager for a SocketChannel. oldId indicates the session id that was used for the
	 * connection previously (in order to find the correct ContextManager) and newContextManager is the recently-created
	 * (and now, no longer necessary) ContextManager for the connection.
	 * 
	 * @param oldId
	 * @param newContextManager
	 * @return true if the restore was successful, false if it was not.
	 */
	public boolean restoreContextManagerFromSessionId(Object oldId, AbstractContextManager newContextManager);
}
