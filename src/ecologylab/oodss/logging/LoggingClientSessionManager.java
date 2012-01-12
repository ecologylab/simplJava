/**
 * 
 */
package ecologylab.oodss.logging;

import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.oodss.distributed.impl.NIOServerIOThread;
import ecologylab.oodss.distributed.server.clientsessionmanager.ClientSessionManager;
import ecologylab.serialization.SimplTypesScope;

/**
 * Provides a special implementation of performService(), that open()'s an OutputStream as necessary
 * to the appropriate directory for logging, based on the headers in the message, then logs the
 * message to there with a minimum of translation.
 * 
 * @author andruid
 * @author eunyee
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class LoggingClientSessionManager extends ClientSessionManager<LoggingContextScope<?>, Scope<?>>
{
	boolean	end	= false;

	/**
	 * @param token
	 * @param loggingServer
	 * @param server
	 * @param socket
	 * @param translationScope
	 * @param appObjScope
	 */
	public LoggingClientSessionManager(String sessionId, int maxPacketSize,
			NIOLoggingServer loggingServer, NIOServerIOThread server, SelectionKey sk,
			SimplTypesScope translationScope, Scope<?> appObjScope)
	{
		super(sessionId, maxPacketSize, server, loggingServer, sk, translationScope, appObjScope);
	}

	@Override
	public void shutdown()
	{
		while (this.messageWaiting || this.requestQueue.size() > 0)
		{
			try
			{
				wait(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		if (this.localScope.getOutputStreamWriter() != null)
		{ // if the local scope still contains the output stream, then shutdown isn't complete!
			// this will make the log readable, but it will not have its real epilogue
			SendEpilogue sE = new SendEpilogue();
			sE.performService(localScope);
		}

		super.shutdown();
	}

	/**
	 * @see ecologylab.oodss.distributed.server.clientsessionmanager.BaseSessionManager#generateContextScope(ecologylab.collections.Scope)
	 */
	@Override
	protected LoggingContextScope<?> generateContextScope(Scope<?> baseScope)
	{
		return new LoggingContextScope(baseScope);
	}
}
