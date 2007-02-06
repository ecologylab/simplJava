package ecologylab.services.logging;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.servers.DoubleThreadedNIOServer;
import ecologylab.xml.TranslationSpace;

/**
 * creating logging server and set the file for saving debug messages
 * 
 * @author eunyee
 */

public class LoggingServer extends DoubleThreadedNIOServer implements
        LoggingDef
{
    String  logFilesPath = "";

    boolean end          = false;

    protected LoggingServer(int portNumber, InetAddress inetAddress,
            TranslationSpace nameSpace, ObjectRegistry objectRegistry)
            throws BindException, IOException
    {
        super(portNumber, inetAddress, nameSpace, objectRegistry, -1);
        // Let server debug messages print to the file
        // Debug.setLoggingFile(serverLogFile);
    }

    protected LoggingServer() throws BindException, IOException
    {
        this(ServicesHostsAndPorts.LOGGING_PORT, InetAddress.getLocalHost(),
                TranslationSpace.get("ecologylab.services.logging",
                        "ecologylab.services.logging"), new ObjectRegistry());
    }

    public void setLogFilesPath(String path)
    {
        logFilesPath = path;
    }

    public String getLogFilesPath()
    {
        return logFilesPath;
    }

    /**
     * Construct an instance of the LoggingServer. Handle and report on
     * exceptions that may occur in the process.
     * 
     * @return The LoggingServer instance, or null if exceptions are thrown.
     */
    public static LoggingServer get()
    {
        LoggingServer loggingServer = null;
        try
        {
            loggingServer = new LoggingServer();
        }
        catch (BindException e)
        {
            println("LoggingServer ERROR binding to port during initialization: "
                    + e);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            println("LoggingServer ERROR during initialization: " + e);
            e.printStackTrace();
        }
        return loggingServer;
    }

    public static void main(String args[])
    {
        LoggingServer loggingServer = get();

        if (loggingServer != null)
        {
            if (args.length > 0)
                loggingServer.setLogFilesPath(args[0]);

            loggingServer.start();
        }
    }

    /**
     * @see ecologylab.services.nio.servers.DoubleThreadedNIOServer#generateContextManager(java.lang.Object,
     *      java.nio.channels.SocketChannel, ecologylab.xml.TranslationSpace,
     *      ecologylab.appframework.ObjectRegistry)
     */
    @Override protected ContextManager generateContextManager(Object token,
            SocketChannel sc, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        return new LoggingContextManager(token, this.getBackend(), sc,
                translationSpace, registry, this.logFilesPath);
    }
}