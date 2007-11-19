package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.NetTools;
import ecologylab.services.distributed.common.ServicesHostsAndPorts;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.xml.TranslationSpace;

/**
 * A server that automatically records any incoming log data to a local file. The file is specified by the Prologue
 * provided by the Logging client.
 * 
 * @see ecologylab.services.logging.Logging
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class NIOLoggingServer extends DoubleThreadedNIOServer implements ServicesHostsAndPorts
{
	String							logFilesPath		= "";

	public static final Class	LOGGING_CLASSES[]	=
																{ LogOps.class, LogRequestMessage.class, LogueMessage.class,
			Prologue.class, SendEpilogue.class, SendPrologue.class, MixedInitiativeOp.class, Epilogue.class };

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param translationSpace
	 * @param objectRegistry
	 * @param authListFilename -
	 *           a file name indicating the location of the authentication list; this should be an XML file of an
	 *           AuthenticationList object.
	 * @return A server instance, or null if it was not possible to open a ServerSocket on the port on this machine.
	 */
	public static NIOLoggingServer getInstance(InetAddress inetAddress, ObjectRegistry objectRegistry,
			int idleConnectionTimeout, int maxPacketSize)
	{
		InetAddress[] addr =
		{ inetAddress };

		return getInstance(addr, objectRegistry, idleConnectionTimeout, maxPacketSize);
	}

	/**
	 * This is the actual way to create an instance of this.
	 * 
	 * @param portNumber
	 * @param translationSpace
	 * @param objectRegistry
	 * @param authListFilename -
	 *           a file name indicating the location of the authentication list; this should be an XML file of an
	 *           AuthenticationList object.
	 * @return A server instance, or null if it was not possible to open a ServerSocket on the port on this machine.
	 */
	public static NIOLoggingServer getInstance(InetAddress[] inetAddress, ObjectRegistry objectRegistry,
			int idleConnectionTimeout, int maxPacketSize)
	{
		NIOLoggingServer newServer = null;

		try
		{
			newServer = new NIOLoggingServer(LOGGING_PORT, inetAddress, null, objectRegistry, idleConnectionTimeout,
					maxPacketSize);
		}
		catch (IOException e)
		{
			println("ServicesServer ERROR: can't open ServerSocket on port " + LOGGING_PORT);
			e.printStackTrace();
		}

		return newServer;
	}

	/**
	 * Launches an instance of the logging server.
	 * 
	 * @param args
	 *           the parameters for the server instance: first argument is the local directory to which to write files;
	 *           second argument is the maximum packet size to accept from the client.
	 * @throws UnknownHostException
	 */
	public static void main(String args[]) throws UnknownHostException
	{
		int mPL = MAX_PACKET_SIZE_CHARACTERS;

		if (args.length > 1)
		{
			try
			{
				int newMPL = Integer.parseInt(args[1]);
				mPL = newMPL;
			}
			catch (NumberFormatException e)
			{
				System.err.println("second argument was not an integer, using MAX_PACKET_SIZE_CHARACTERS: " + MAX_PACKET_SIZE_CHARACTERS);
				e.printStackTrace();
			}
		}
		else
		{
			System.err.println("No max packet length specified, using " + mPL);
		}

		NIOLoggingServer loggingServer = getInstance(NetTools.getAllInetAddressesForLocalhost(), new ObjectRegistry(),
				-1, mPL);

		if (loggingServer != null)
		{
			if (args.length > 0)
			{
				loggingServer.setLogFilesPath(args[0]);
			}

			loggingServer.start();
		}
	}

	protected NIOLoggingServer(int portNumber, InetAddress[] inetAddress, TranslationSpace requestTranslationSpace,
			ObjectRegistry objectRegistry, int idleConnectionTimeout, int maxPacketSize) throws IOException, BindException
	{
		super(portNumber, inetAddress, TranslationSpace.get("double_threaded_logging " + inetAddress[0].toString() + ":"
				+ portNumber, LOGGING_CLASSES, requestTranslationSpace), objectRegistry, idleConnectionTimeout,
				maxPacketSize);
	}

	/**
	 * Sets the directory to which to write log files.
	 * 
	 * @param path
	 */
	public void setLogFilesPath(String path)
	{
		logFilesPath = path;
	}

	/**
	 * @return the log file path.
	 */
	public String getLogFilesPath()
	{
		return logFilesPath;
	}

	@Override protected LoggingContextManager generateContextManager(Object token, SocketChannel sc,
			TranslationSpace translationSpaceIn, ObjectRegistry registryIn)
	{
		return new LoggingContextManager(token, maxPacketSize, this, this.getBackend(), sc, translationSpaceIn,
				registryIn);
	}

	/**
	 * Displays some information about the logging server, then calls super.start()
	 * 
	 * @see ecologylab.services.distributed.server.DoubleThreadedNIOServer#start()
	 */
	@Override public void start()
	{
		this.debug("------------------------Logging Server starting------------------------");
		this.debug("max packet length: " + this.maxPacketSize);
		this.debug("saving logs to: " + this.logFilesPath);
		this.debug("operating on port: " + this.getBackend().getPortNumber());
		this.debug("using the following interfaces: ");

		for (InetAddress i : this.getBackend().getHostAddresses())
		{
			this.debug(i.toString());
		}

		super.start();
	}
}
