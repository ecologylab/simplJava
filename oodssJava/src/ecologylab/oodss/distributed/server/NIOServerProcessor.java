/**
 * 
 */
package ecologylab.oodss.distributed.server;

import ecologylab.generic.CharBufferPool;
import ecologylab.generic.StartAndStoppable;
import ecologylab.generic.StringBuilderPool;
import ecologylab.io.ByteBufferPool;
import ecologylab.oodss.distributed.server.clientsessionmanager.BaseSessionManager;

/**
 * An interface indicating necessary functionality for a server so that it can properly service
 * ContextManagers.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public interface NIOServerProcessor extends StartAndStoppable
{
	/**
	 * Performs any internal actions that should be taken whenever a client is disconnected.
	 * 
	 * @param sessionId
	 *          the identifier for the client's connection (key.attachment(), normally)
	 * @param forcePermanent
	 *          TODO
	 * @return true if the client is permanently disconnecting
	 */
	public boolean invalidate(String sessionId, boolean forcePermanent);

	/**
	 * Attempts to switch the ContextManager for a SocketChannel. oldId indicates the session id that
	 * was used for the connection previously (in order to find the correct ContextManager) and
	 * newContextManager is the recently-created (and now, no longer necessary) ContextManager for the
	 * connection.
	 * 
	 * @param oldId
	 * @param newContextManager
	 * @return true if the restore was successful, false if it was not.
	 */
	public boolean restoreContextManagerFromSessionId(String oldId,
			BaseSessionManager newContextManager);
	
	public ByteBufferPool getSharedByteBufferPool();
	
	public CharBufferPool getSharedCharBufferPool();
	
	public StringBuilderPool getSharedStringBuilderPool();
	
	public void increaseSharedBufferPoolSize(int newCapacity);
}
