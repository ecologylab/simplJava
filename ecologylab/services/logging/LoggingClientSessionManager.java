/**
 * 
 */
package ecologylab.services.logging;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.clientsessionmanager.ClientSessionManager;
import ecologylab.xml.TranslationScope;

/**
 * Provides a special implementation of performService(), that open()'s an
 * OutputStream as necessary to the appropriate directory for logging, based on
 * the headers in the message, then logs the message to there with a minimum of
 * translation.
 * 
 * @author andruid
 * @author eunyee
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public class LoggingClientSessionManager extends ClientSessionManager
{
	boolean					end	= false;

	/**
	 * @param token
	 * @param loggingServer
	 * @param server
	 * @param socket
	 * @param translationSpace
	 * @param registry
	 */
	public LoggingClientSessionManager(Object token, int maxPacketSize,
			NIOLoggingServer loggingServer, NIOServerIOThread server,
			SelectionKey sk, TranslationScope translationSpace, Scope registry)
	{
		super(token, maxPacketSize, server, loggingServer, sk, translationSpace,
				registry);
	}

	@Override public void shutdown()
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
		
		if (this.localScope.containsKey(LogEvent.OUTPUT_STREAM))
		{ // if the local scope still contains the output stream, then shutdown isn't complete!
			// this will make the log readable, but it will not have its real epilogue
			SendEpilogue sE = new SendEpilogue();
			sE.performService(localScope);
		}

		super.shutdown();
	}
}
