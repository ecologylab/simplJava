package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.net.NetTools;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.nio.servers.DoubleThreadedNIOServer;
import ecologylab.xml.TranslationSpace;

public class NIOLoggingServer extends DoubleThreadedNIOServer implements
        ServicesHostsAndPorts
{
    String logFilesPath = "";

    /**
     * This is the actual way to create an instance of this.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authListFilename -
     *            a file name indicating the location of the authentication
     *            list; this should be an XML file of an AuthenticationList
     *            object.
     * @return A server instance, or null if it was not possible to open a
     *         ServerSocket on the port on this machine.
     */
    public static NIOLoggingServer getInstance(InetAddress inetAddress,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize)
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
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authListFilename -
     *            a file name indicating the location of the authentication
     *            list; this should be an XML file of an AuthenticationList
     *            object.
     * @return A server instance, or null if it was not possible to open a
     *         ServerSocket on the port on this machine.
     */
    public static NIOLoggingServer getInstance(InetAddress[] inetAddress,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize)
    {
        NIOLoggingServer newServer = null;

        try
        {
            newServer = new NIOLoggingServer(LOGGING_PORT, inetAddress,
                    TranslationSpace.get("ecologylab.services.logging",
                            "ecologylab.services.logging"), objectRegistry,
                    idleConnectionTimeout, maxPacketSize);
        }
        catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + LOGGING_PORT);
            e.printStackTrace();
        }

        return newServer;
    }

    public static void main(String args[]) throws UnknownHostException
    {
        int mPL = MAX_PACKET_SIZE;

        if (args.length > 1)
        {
            try
            {
                int newMPL = Integer.parseInt(args[1]);
                mPL = newMPL;
            }
            catch (NumberFormatException e)
            {
                System.err
                        .println("second argument was not an integer, using MAX_PACKET_SIZE: "
                                + MAX_PACKET_SIZE);
                e.printStackTrace();
            }
        }
        else
        {
            System.err.println("No max packet length specified, using " + mPL);
        }

        NIOLoggingServer loggingServer = getInstance(
                NetTools.getAllInetAddressesForLocalhost(), new ObjectRegistry(), -1, mPL);

        if (loggingServer != null)
        {
            if (args.length > 0)
            {
                loggingServer.setLogFilesPath(args[0]);
            }

            loggingServer.start();
        }
    }

    protected NIOLoggingServer(int portNumber, InetAddress[] inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize) throws IOException, BindException
    {
        super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
                idleConnectionTimeout, maxPacketSize);

        this.translationSpace
                .setDefaultPackageName("ecologylab.services.logging");
    }

    public void setLogFilesPath(String path)
    {
        logFilesPath = path;
    }

    public String getLogFilesPath()
    {
        return logFilesPath;
    }

    @Override protected LoggingContextManager generateContextManager(
            Object token, SocketChannel sc, TranslationSpace translationSpaceIn,
            ObjectRegistry registryIn)
    {
        debug("NEW LOGGING CONTEXT MANAGER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return new LoggingContextManager(token, maxPacketSize, this, this
                .getBackend(), sc, translationSpaceIn, registryIn);
    }

    /**
     * Displays some information about the logging server, then calls super.start()
     * 
     * @see ecologylab.services.nio.servers.DoubleThreadedNIOServer#start()
     */
    @Override public void start()
    {
        this.debug("------------------------Logging Server starting------------------------");
        this.debug("max packet length: "+this.maxPacketSize);
        this.debug("saving logs to: "+this.logFilesPath);
        this.debug("operating on port: "+this.getBackend().getPortNumber());
        this.debug("using the following interfaces: ");
        
        for (InetAddress i : this.getBackend().getHostAddresses())
        {
            this.debug(i.toString());
        }
        
        super.start();
    }
}
