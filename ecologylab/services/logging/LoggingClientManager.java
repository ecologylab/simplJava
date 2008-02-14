/**
 * 
 */
package ecologylab.services.logging;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.common.ServerConstants;
import ecologylab.services.distributed.impl.NIOServerIOThread;
import ecologylab.services.distributed.server.clientmanager.ClientManager;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;

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
public class LoggingClientManager extends ClientManager
{

	OutputStreamWriter	outputStreamWriter;

	NIOLoggingServer		loggingServer;

	boolean					end	= false;

	/**
	 * @param token
	 * @param loggingServer
	 * @param server
	 * @param socket
	 * @param translationSpace
	 * @param registry
	 */
	public LoggingClientManager(Object token, int maxPacketSize,
			NIOLoggingServer loggingServer, NIOServerIOThread server,
			SelectionKey sk, TranslationSpace translationSpace, Scope registry)
	{
		super(token, maxPacketSize, server, loggingServer, sk, translationSpace,
				registry);

		this.loggingServer = loggingServer;
	}

	@Override protected ResponseMessage performService(
			RequestMessage requestMessage)
	{
		ResponseMessage responseMessage;
		if (requestMessage instanceof InitConnectionRequest)
		{
			responseMessage = super.performService(requestMessage);
		}
		else
		{
			if (requestMessage instanceof SendPrologue)
			{
				String name = loggingServer.getLogFilesPath()
						+ ((SendPrologue) requestMessage).getFileName();
				getOutputStreamWriter(name);
				// servicesServer.getObjectRegistry().registerObject(LoggingDef.keyStringForFileObject,
				// getFile(name) );
			}
			else if (outputStreamWriter == null)
			{
				debug("Prologue has not been received OR File has not been created!! "
						+ requestMessage);
			}

			if ((outputStreamWriter != null)
					&& (requestMessage instanceof LogRequestMessage))
			{
				((LogRequestMessage) requestMessage).setWriter(outputStreamWriter);
			}

			responseMessage = super.performService(requestMessage);

			if (requestMessage instanceof SendEpilogue)
			{
				debug("received epiliogue, set end to true");
				try
				{
					outputStreamWriter.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				end = true;
			}
			else
			{
				requestMessage.getClass();
			}
		}
		return responseMessage;
	}

	protected OutputStreamWriter getOutputStreamWriter(String fileName)
	{
		if (outputStreamWriter == null)
		{
			try
			{
				File file = new File(fileName);
				String dirPath = file.getParent();
				if (dirPath != null)
				{
					File dir = new File(dirPath);
					if (!dir.exists())
						dir.mkdirs();
				}

				debug("logging to file at: " + file.getAbsolutePath());

				// TODO what if (file.exists()) ???
				FileOutputStream fos = new FileOutputStream(file, true);
				CharsetEncoder encoder = Charset.forName(
						ServerConstants.CHARACTER_ENCODING).newEncoder();
				outputStreamWriter = new OutputStreamWriter(fos, encoder);
				return outputStreamWriter;
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return outputStreamWriter;
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

		if (!end)
		{
			SendEpilogue sE = new SendEpilogue();
			sE.setWriter(outputStreamWriter);
			sE.performService(localScope);
		}

		super.shutdown();
	}
}
