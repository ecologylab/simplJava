package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.common.ServicesHostsAndPorts;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.TranslationScope;

/**
 * A server that automatically records any incoming log data to a local file.
 * The file is specified by the Prologue provided by the Logging client.
 * 
 * @see ecologylab.services.logging.Logging
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class NIOLoggingServer extends DoubleThreadedNIOServer implements
		ServicesHostsAndPorts
{
	String							logFilesPath							= "";

	public static final String	LOG_FILES_PATH							= "LOG_FILES_PATH";

	public static final Class	LOGGING_CLASSES[]						=
																					{ LogOps.class,
			LogEvent.class, LogueMessage.class, Prologue.class,
			SendEpilogue.class, SendPrologue.class, MixedInitiativeOp.class,
			Epilogue.class													};

	public static final int		MAX_MESSAGE_SIZE_CHARS_LOGGING	= 1024 * 1024;		// 1MB

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param translationSpace
	 * @param objectRegistry
	 * @param authListFilename -
	 *           a file name indicating the location of the authentication list;
	 *           this should be an XML file of an AuthenticationList object.
	 * @return A server instance, or null if it was not possible to open a
	 *         ServerSocket on the port on this machine.
	 */
	public static NIOLoggingServer getInstance(InetAddress inetAddress,
			Scope objectRegistry, int idleConnectionTimeout, int maxPacketSize)
	{
		InetAddress[] addr =
		{ inetAddress };

		return getInstance(addr, objectRegistry, idleConnectionTimeout,
				maxPacketSize);
	}

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param translationSpace
	 * @param objectRegistry
	 * @param authListFilename -
	 *           a file name indicating the location of the authentication list;
	 *           this should be an XML file of an AuthenticationList object.
	 * @return A server instance, or null if it was not possible to open a
	 *         ServerSocket on the port on this machine.
	 */
	public static NIOLoggingServer getInstance(InetAddress[] inetAddress,
			Scope objectRegistry, int idleConnectionTimeout, int maxPacketSize)
	{
		NIOLoggingServer newServer = null;

		try
		{
			newServer = new NIOLoggingServer(LOGGING_PORT, inetAddress, null,
					objectRegistry, idleConnectionTimeout, maxPacketSize);
		}
		catch (IOException e)
		{
			println("ServicesServer ERROR: can't open ServerSocket on port "
					+ LOGGING_PORT);
			e.printStackTrace();
		}

		return newServer;
	}

	/**
	 * Launches an instance of the logging server.
	 * 
	 * @param args
	 *           the parameters for the server instance: first argument is the
	 *           local directory to which to write files; second argument is the
	 *           maximum packet size to accept from the client.
	 * @throws UnknownHostException
	 */
	public static void main(String args[]) throws UnknownHostException
	{
		int mPL = MAX_MESSAGE_SIZE_CHARS_LOGGING;

		if (args.length > 1)
		{
			try
			{
				int newMPL = Integer.parseInt(args[1]);
				mPL = newMPL;
			}
			catch (NumberFormatException e)
			{
				Debug
						.println("second argument was not an integer, using MAX_MESSAGE_SIZE_CHARS_LOGGING: "
								+ MAX_MESSAGE_SIZE_CHARS_LOGGING);
				e.printStackTrace();
			}
		}
		else
		{
			Debug.println("No max packet length specified, using " + mPL);
		}

		NIOLoggingServer loggingServer = getInstance(NetTools
				.getAllInetAddressesForLocalhost(), new Scope(), -1, mPL);

		if (loggingServer != null)
		{
			if (args.length > 0)
			{
				loggingServer.setLogFilesPath(args[0]);
			}

			if (args.length > 2)
				Debug.setLoggingFile(args[2]);

			loggingServer.start();
		}
	}

	protected NIOLoggingServer(int portNumber, InetAddress[] inetAddresses,
			TranslationScope requestTranslationSpace,
			Scope applicationObjectScope, int idleConnectionTimeout,
			int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddresses, TranslationScope.get(
				"double_threaded_logging " + inetAddresses[0].toString() + ":"
						+ portNumber, LOGGING_CLASSES, DefaultServicesTranslations
						.get(), requestTranslationSpace), applicationObjectScope,
				idleConnectionTimeout, maxPacketSize);

		// add the necessary scope object mappings for logging
		// note this is probably null right now...
		this.applicationObjectScope.put(LOG_FILES_PATH, this.logFilesPath);
	}

	/**
	 * Sets the directory to which to write log files.
	 * 
	 * @param path
	 */
	public void setLogFilesPath(String path)
	{
		logFilesPath = path;

		this.applicationObjectScope.put(LOG_FILES_PATH, this.logFilesPath);
	}

	/**
	 * @return the log file path.
	 */
	public String getLogFilesPath()
	{
		return logFilesPath;
	}

	@Override protected LoggingClientSessionManager generateContextManager(
			Object token, SelectionKey sk, TranslationScope translationSpaceIn,
			Scope registryIn)
	{
		return new LoggingClientSessionManager(token, maxMessageSize, this, this
				.getBackend(), sk, translationSpaceIn, registryIn);
	}

	/**
	 * Displays some information about the logging server, then calls
	 * super.start()
	 * 
	 * @see ecologylab.services.distributed.server.DoubleThreadedNIOServer#start()
	 */
	@Override public void start()
	{
		this
				.debug("------------------------ Logging Server starting ------------------------");
		this.debug("             max packet length: " + this.maxMessageSize);
		this.debug("                saving logs to: " + this.logFilesPath);
		this.debug("             operating on port: "
				+ this.getBackend().getPortNumber());
		this.debug("using the following interfaces: ");

		for (InetAddress i : this.getBackend().getHostAddresses())
		{
			this.debug("                                " + i.toString());
		}

		super.start();
	}
}
