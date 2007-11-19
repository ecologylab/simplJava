package ecologylab.services.distributed.legacy;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.distributed.server.DoubleThreadedNIOServer;
import ecologylab.xml.TranslationSpace;

/**
 * 
 * @author eunyee
 * 
 */
public class HTTPPostServer extends DoubleThreadedNIOServer
{
    private static int postServerPort = 10200;
	public static String datastore	= "//project//ecologylab//TestCollections//";

    public static void main(String args[]) throws UnknownHostException
    {
        System.out.println("localhost: " + InetAddress.getLocalHost());
        System.out.println(InetAddress.getByName("localhost"));
        HTTPPostServer validateionTestServer = get(postServerPort, InetAddress
                .getByName("localhost"), TranslationSpace.get(
                "validateMessage", "ecologylab.services.messages"), new ObjectRegistry());

        if (validateionTestServer != null)
        {
            validateionTestServer.start();
        }
    }

    /**
     * Instantiates an HTTPPostServer.
     * 
     * @param portNumber
     * @param inetAddress
     * @param translationSpace
     * @param objectRegistry
     * @throws IOException
     * @throws BindException
     */
    protected HTTPPostServer(int portNumber, InetAddress[] inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry) throws IOException, BindException
    {
        super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
                -1, MAX_PACKET_SIZE_CHARACTERS);
    }

    /**
     * Construct an instance of the TestDataServer. Handle and report on
     * exceptions that may occur in the process.
     * 
     * @return The TestDataServer instance, or null if exceptions are thrown.
     */
    protected static HTTPPostServer get(int portNumber,
            InetAddress inetAddress, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry)
    {
        HTTPPostServer httpPostServer = null;
        try
        {
            InetAddress[] address = { inetAddress };
            httpPostServer = new HTTPPostServer(portNumber, address,
                    requestTranslationSpace, objectRegistry);
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
        return httpPostServer;
    }
}