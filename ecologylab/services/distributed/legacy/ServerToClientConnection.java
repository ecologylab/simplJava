package ecologylab.services.distributed.legacy;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Collection;
import java.util.LinkedList;

import ecologylab.generic.Debug;
import ecologylab.services.distributed.common.ServerConstants;
import ecologylab.services.messages.ErrorResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XMLTranslationException;

/**
 * Interface Ecology Lab Distributed Computing Services framework<p/>
 * 
 * Runs the connection from the server to a client.
 * 
 * @author andruid
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * @author eunyee
 */
public class ServerToClientConnection extends Debug implements Runnable, ServerConstants
{

    protected InputStream    inputStream;

    protected PrintStream    outputStreamWriter;

    protected ServicesServer servicesServer;

    protected Socket         incomingSocket;

    protected boolean        running      = true;

    private boolean          shuttingDown = false;

    private Collection<Object> objectsToBeNotified = new LinkedList<Object>();
    
    public ServerToClientConnection(Socket incomingSocket,
            ServicesServer servicesServer) throws IOException
    {
        this.incomingSocket = incomingSocket;

        inputStream = incomingSocket.getInputStream();

        outputStreamWriter = new PrintStream(incomingSocket.getOutputStream());

        this.servicesServer = servicesServer;
    }

    public String toString()
    {
        return super.getClassName() + "[" + incomingSocket + "]";
    }

    /**
     * Service the client connection. <p/> Do not override this method! If you
     * need more specific functionality, add some sort of a hook that gets
     * called from in here, that subclasses can override. -- Andruid
     */
    public final void run()
    {
        int badTransmissionCount = 0;
        while (running)
        {
            debug("waiting for packet");
            // get the packet message
            String messageString = "";
            try
            {
                if (!this.shuttingDown)
                {
                    messageString = readToMax(inputStream);
                }

                if (messageString != null)
                {
                    if (show(5))
                        debug("got raw message["
                                + messageString.getBytes().length + "]: "
                                + messageString);

                    RequestMessage requestMessage = translateXMLStringToRequestMessage(messageString);

                    if (requestMessage == null)
                        debug("ERROR: translation failed: " + messageString);
                    else
                    {
                        // perform the service being requested
                        ResponseMessage responseMessage = performService(requestMessage);

                        if (sendResponse(requestMessage, responseMessage))
                            stop(); // sorry http clients can only handle one
                        // message per connection :-(
                        badTransmissionCount = 0;
                    }
                }
                else
                {
                    debug("ERROR: null returned when translating message.");
                }
                
                if (this.shuttingDown)
                {
                    this.stop();
                }
            }
            catch (java.net.SocketException e)
            {
                // this seems to mean the connection went away
                if (outputStreamWriter != null) // dont need the message if
                    // we're already shutting down
                    debug("STOPPING:  It seems we are no longer connected to the client.");
                break;
            }
            catch (IOException e)
            {
                // TODO count streak of errors and break;
                debug("IO ERROR: " + e.getMessage());
                e.printStackTrace();
            }
            catch (XMLTranslationException e)
            {
                // report error on XML passed through the socket
                debug("Bogus Message ERROR: " + messageString);
                e.printStackTrace();
                if (++badTransmissionCount >= MAXIMUM_TRANSMISSION_ERRORS)
                {
                    debug("Too many bogus messages.");
                    break;
                }
                else
                    try
                    {
                        // sendResponse(ResponseMessage.BADTransmissionResponse());
                        sendResponse(null, new BadTransmissionResponse());
                    }
                    catch (XMLTranslationException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
            }
            catch (Exception e)
            {
                debug("Exception Caught: " + e.toString());
                e.printStackTrace();
                break;
            }
        }
        synchronized (this)
        {
            if (running)
                stop();
        }
    }

    /**
     * Use the ServicesServer and its ObjectRegistry to do the translation. Can
     * be overridden to provide special functionalities
     * 
     * @param messageString
     * @return
     * @throws XMLTranslationException
     */
    protected RequestMessage translateXMLStringToRequestMessage(
            String messageString) throws XMLTranslationException,
            UnsupportedEncodingException
    {
        RequestMessage requestMessage = servicesServer
                .translateXMLStringToRequestMessage(messageString, true);
        return requestMessage;
    }

    /**
     * Perform the service specified by the request method. The default
     * implementation, here, simply passes the message to the servicesServer,
     * which is keeping an objectRegistry context, and does the perform. <p/>
     * This routine is abstracted out here, so that customized Servers can do
     * thread/connection specific custom processing in this method, as needed,
     * by overriding the definition.
     * 
     * @param requestMessage
     * @return
     */
    protected ResponseMessage performService(RequestMessage requestMessage)
    {
        // add the IP address
        requestMessage.setSender(this.incomingSocket.getInetAddress());

        ResponseMessage responseMessage = servicesServer
                .performService(requestMessage);
        return responseMessage;
    }

    /**
     * Send the response message back to the client.
     * 
     * @param requestMessage
     *            Provide context for response sending, when needed. May be
     *            ignored in some cases.
     * @param responseMessage
     * 
     * @return True if the connection should be terminated after this.
     * 
     * @throws XMLTranslationException
     */
    protected boolean sendResponse(RequestMessage requestMessage,
            ResponseMessage responseMessage) throws XMLTranslationException
    {
        responseMessage.setUid(requestMessage.getUid());
        sendResponse(responseMessage.translateToXML());

        return false;
    }

    protected void sendResponse(CharSequence responseString)
            throws XMLTranslationException
    {
        // send the response
        if (outputStreamWriter != null)
        {
            outputStreamWriter.println(responseString);
            outputStreamWriter.flush();
        }
        else
        {
            debug("Cant send message after stop: " + responseString);
        }
    }

    public synchronized void stop()
    {
        running = false;
        debug("stopping.");
        
        try
        {
            if (outputStreamWriter != null)
            {
                outputStreamWriter.close();
                // debug("writer is closed.");
                outputStreamWriter = null;
            }
            if (inputStream != null)
            {
                inputStream.close();
                // debug("reader is closed.");
                inputStream = null;
            }
        }
        catch (IOException e)
        {
            debug("while closing reader & writer: " + e.getMessage());
        }
        
        servicesServer.connectionTerminated(this);
        
        notifyObjects();
    }

    private void notifyObjects()
    {
        for (Object o : this.objectsToBeNotified)
        {
            synchronized (o)
            {
                o.notify();
            }
        }
    }
    
    /**
     * Limit the data size and send exception if the request data is bigger than
     * defined size.
     * 
     * @param in
     * @return
     * @throws Exception
     */
    public String readToMax(InputStream in) throws Exception
    {
        char[] ch_array = new char[MAX_PACKET_SIZE_CHARACTERS];
        int count = 0;

        while (count < MAX_PACKET_SIZE_CHARACTERS)
        {
            int c = in.read();
            if (c == -1)
                throw new java.net.SocketException(
                        "Client terminated connection.");

            ch_array[count] = (char) c;
            count++;
            if ((count != 1) && (c == '\n' || c == '\r'))
            {
                String str = new String(ch_array, 0, count);
                str.trim();
                return str;
            }
        }

        throw new Exception("Data is over Maximum Size !!");

    }

    /**
     * This is the error Response sent 3 times by this server, when it receives
     * a bogus (not proper xml) message. Outside of in this server, and in
     * ServicesClient, where this message must recognized as a request for a
     * retry, this class *MUST* not be used anywhere!
     * 
     * @author andruid
     */
    class BadTransmissionResponse extends ErrorResponse
    {
        private BadTransmissionResponse()
        {

        }
    }

    /**
     * Causes this to stop accepting new requests, completes all pending
     * requests, closes, then notifies objectsToBeNotified.
     * 
     * See ecologylab.services.Shutdownable#shutdownAndNotify(java.util.Collection)
     */
    public void shutdown(Collection<Object> objectsToBeNotified)
    {
        debug("server to client connection "+this.toString()+" shutting down.");

        this.objectsToBeNotified.addAll(objectsToBeNotified);

        this.shuttingDown = true;
    }
}
