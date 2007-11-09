/**
 * 
 */
package ecologylab.services.nio.servers;

import java.nio.channels.SocketChannel;

import ecologylab.generic.StartAndStoppable;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.services.nio.contextmanager.AbstractContextManager;

/**
 * ServerActionProcessor objects handle the translation of request/response
 * messages and their actions on a server. These objects keep different
 * connections separate. They are used in conjunction with an NIO services
 * server: it will handle all of the actual communication over the network.
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
    void processRead(Object token, NIOServerBackend base, SocketChannel sc,
            byte[] bs, int bytesRead) throws BadClientException;

    /**
     * Performs any internal actions that should be taken whenever a client is
     * disconnected.
     * 
     * @param token
     * @param base
     * @param sc
     * @param permanent -
     *            indicates whether the NIOServerFrontend should destroy the
     *            context manager object associated with the given connection.
     * @return the ContextManager object associated with sc that has been
     *         removed from the system.
     */
    AbstractContextManager invalidate(Object token, NIOServerBackend base,
            SocketChannel sc, boolean permanent);

    /**
     * Attempts to switch the ContextManager for a SocketChannel. oldId
     * indicates the session id that was used for the connection previously (in
     * order to find the correct ContextManager) and newContextManager is the
     * recently-created (and now, no longer necessary) ContextManager for the
     * connection.
     * 
     * @param oldId
     * @param newContextManager
     * @return true if the restore was successful, false if it was not.
     */
    public boolean restoreContextManagerFromSessionId(Object oldId,
            AbstractContextManager newContextManager);
}
