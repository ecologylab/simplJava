package ecologylab.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.SocketException;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;

/**
 * Interface Ecology Lab Distributed Computing Services framework<p/>
 * 
 * Client to connect to ServicesServer.
 * 
 * @author blake
 * @author andruid
 */
public class ServicesClient extends ServicesClientBase implements
        ServerConstants
{
    BufferedReader reader;

    PrintStream    output;

    /**
     * Stores information about this for the toString() method.
     */
    private String toString;

    /**
     * Stores the response from the server until it is translated from XML.
     */
    private String response;

    /**
     * Create a client that will connect on the provided port. Assume localhost
     * 
     * @param port
     *            The localhost port to connect.
     */
    public ServicesClient(int port)
    {
        this(port, null);
    }

    public ServicesClient(int port, NameSpace messageSpace)
    {
        this("localhost", port, messageSpace);
    }

    public ServicesClient(String server, int port)
    {
        this("localhost", port, null);
    }

    public ServicesClient(String server, int port, NameSpace messageSpace)
    {
        this(server, port, messageSpace, null);
    }

    public ServicesClient(String server, int port, NameSpace messageSpace,
            ObjectRegistry objectRegistry)
    {
        super(server, port, messageSpace, objectRegistry);
    }

    protected boolean createConnection()
    {
        InetAddress address = null;
        try
        {
            // address = InetAddress.getLocalHost();
            // get the address and connect
            address = InetAddress.getByName(server);
            socket = new Socket(address, port);

            // in = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(socket
                    .getInputStream()));
            output = new PrintStream(socket.getOutputStream());
        }
        catch (BindException e)
        {
            debug("Couldnt create socket connection to server '" + server
                    + "': " + e);
            // e.printStackTrace();
            socket = null;
        }
        catch (PortUnreachableException e)
        {
            debug("Server is alive, but has no daemon on port " + port + ": "
                    + e);
            // e.printStackTrace();
            socket = null;
        }
        catch (SocketException e)
        {
            debug("Server '" + server + "' unreachable: " + e);
        }
        catch (IOException e)
        {
            debug("Bad response from server: " + e);
            // e.printStackTrace();
            socket = null;
        }
        return socket != null;
    }

    public String toString()
    {
        String toString = this.toString;
        if (toString == null)
        {
            toString = this.getClassName() + "[" + server + ": " + port + "]";
            this.toString = toString;
        }
        return toString;
    }

    /**
     * Determine if we are connected.
     * 
     * @return True if connected, false if not.
     */
    public boolean connected()
    {
        return (socket != null);
    }

    /**
     * Disconnect from the server (if connected).
     */
    public void disconnect()
    {
        if (connected())
        {
            try
            {
                socket.close();
                System.out.println("Closed the connection.");
                socket = null;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            System.out.println("Could not close connection: not connected!");
        }
    }

    /**
     * Send a message to the ServicesServer to get a service performed,
     * 
     * @param requestMessage
     * @return The ResponseMessage from the server. This could be null, which
     *         means that communication with the server failed. Reasons for
     *         failure include: 1) IOException: the socket connection broke
     *         somehow. 2) XmlTranslationException: The message was malformed or
     *         translation failed strangely.
     */
    public ResponseMessage sendMessage(RequestMessage requestMessage)
    {
        // get the UID for the request message
        requestMessage.setUid(this.getUid());

        if (!connected())
            createConnection();

        ResponseMessage responseMessage = null;
        boolean transactionComplete = false;
        int badTransmissionCount = 0;

        while (!transactionComplete)
        {
            String requestMessageXML = null;
            try
            {
                // requestMessage.stampTime();
                requestMessageXML = requestMessage.translateToXML(false);

                if (requestMessageXML.getBytes().length > ServerConstants.MAX_PACKET_SIZE)
                {
                    debug("requestMessage is Bigger than acceptable server size \n CANNOT SEND : "
                            + requestMessageXML);
                    break;
                }

                output.println(requestMessageXML);

                if (show(5))
                    debug("Services Client: just sent message: "
                            + requestMessageXML);

                if (show(5))
                    debug("Services Client: awaiting a response");

                response = reader.readLine();

                responseMessage = (ResponseMessage) ResponseMessage
                        .translateFromXMLString(response, translationSpace);

                // System.out.println("the response lagged
                // "+(System.currentTimeMillis() -
                // responseMessage.timeStamp)+"ms; the round trip took
                // "+(System.currentTimeMillis() -
                // requestMessage.timeStamp)+"ms.)");

                if (responseMessage instanceof ServerToClientConnection.BadTransmissionResponse)
                {
                    badTransmissionCount++;
                    if (badTransmissionCount == 3)
                    {
                        debug("ERROR: Quitting sending to the server because of the network condition after "
                                + badTransmissionCount + " times try ");
                        break;
                    }
                    else
                    {
                        debug("ERROR: BADTransmission of: " + requestMessageXML
                                + "\n\t Resending.");
                    }
                }
                else
                {
                    if (requestMessage.getUid() == responseMessage.getUid())
                    {
                        if (show(5))
                            debug("received response: " + response);
                        processResponse(responseMessage);
                        transactionComplete = true;
                    }
                    else
                    {
                        debug("Request UID is " + requestMessage.getUid()
                                + "; response was: " + responseMessage.getUid() + "; ignoring message.");
                    }
                }

            }
            catch (Exception e)
            {
                debug("ERROR: Failed sending " + requestMessage + ": " + e);
                transactionComplete = true;
            }
        }
        
        return responseMessage;
    }
}
