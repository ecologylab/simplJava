package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
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

        NIOLoggingServer loggingServer = getInstance(
                InetAddress.getLocalHost(), new ObjectRegistry(), -1, mPL);

        if (loggingServer != null)
        {
            if (args.length > 0)
            {
                loggingServer.setLogFilesPath(args[0]);
            }

            loggingServer.start();
        }
    }

    protected NIOLoggingServer(int portNumber, InetAddress inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize) throws IOException, BindException
    {
        super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
                idleConnectionTimeout, maxPacketSize);
    }

    public void setLogFilesPath(String path)
    {
        logFilesPath = path;
    }

    public String getLogFilesPath()
    {
        return logFilesPath;
    }

    @Override
    protected LoggingContextManager generateContextManager(Object token,
            SocketChannel sc, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        return new LoggingContextManager(token, maxPacketSize, this, this
                .getBackend(), sc, translationSpace, registry);
    }
}
