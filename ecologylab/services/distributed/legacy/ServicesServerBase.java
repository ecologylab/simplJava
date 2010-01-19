/*
 * Created on May 3, 2006
 */
package ecologylab.services.distributed.legacy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;

import lib.Base64Coder;
import ecologylab.collections.Scope;
import ecologylab.generic.StartAndStoppable;
import ecologylab.services.distributed.impl.Manager;
import ecologylab.services.distributed.impl.ServerEvent;
import ecologylab.services.distributed.impl.ServerListener;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

/**
 * A base set of fields and methods that are necessary for any server
 * implementation.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public abstract class ServicesServerBase extends Manager implements Runnable,
        StartAndStoppable
{
    protected int              portNumber;

    protected ServerSocket     serverSocket;

    protected boolean          finished;

    protected boolean          shuttingDown    = false;

    protected LinkedList<ServerListener> serverListeners = new LinkedList<ServerListener>();
    
    /**
     * Space that defines mappings between xml names, and Java class names, for
     * request messages.
     */
    protected TranslationScope requestTranslationSpace;

    /**
     * Provides a context for request processing.
     */
    protected Scope   objectRegistry;

    protected int              connectionCount = 0;

    private MessageDigest      digester;

    private long               dispensedTokens;

    /**
     * Creates a Services Server Base. Sets internal variables, but does not
     * bind the port. Port binding is to be handled by sublcasses.
     * 
     * @param portNumber
     * @param translationSpace
     * @param objectRegistry
     *            Provides a context for request processing.
     * @throws IOException
     */
    protected ServicesServerBase(int portNumber,
            TranslationScope requestTranslationSpace,
            Scope objectRegistry) throws IOException,
            java.net.BindException
    {
        this.portNumber = portNumber;
        this.requestTranslationSpace = requestTranslationSpace;
        if (objectRegistry == null)
            objectRegistry = new Scope();
        this.objectRegistry = objectRegistry;

        try
        {
            digester = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e)
        {
            debug("This can only happen if the local implementation does not include the given hash algorithm.");
            e.printStackTrace();
        }
    }

    /**
     * Generates a unique identifier String for the given socket, based upon
     * actual ports used and ip addresses with a hash.
     * 
     * @param incomingSocket
     * @return
     */
    protected String generateSessionToken(Socket incomingSocket)
    {
        // clear digester
        digester.reset();

        // we make a string consisting of the following:
        // time of initial connection (when this method is called), server ip,
        // client ip, client actual port
        digester.update(String.valueOf(System.currentTimeMillis()).getBytes());
        // digester.update(String.valueOf(System.nanoTime()).getBytes());
        digester.update(this.serverSocket.getInetAddress().toString()
                .getBytes());
        digester.update(incomingSocket.getInetAddress().toString().getBytes());
        digester.update(String.valueOf(incomingSocket.getPort()).getBytes());

        digester.update(String.valueOf(this.dispensedTokens).getBytes());

        dispensedTokens++;

        // convert to normal characters and return as a String
        return new String(Base64Coder.encode(digester.digest()));
    }

    /**
     * Perform the service associated with a RequestMessage, by calling the
     * performService() method on that message.
     * 
     * @param requestMessage
     *            Message to perform.
     * @return Response to the message.
     */
    public ResponseMessage performService(RequestMessage requestMessage)
    {
        ResponseMessage temp = requestMessage.performService(objectRegistry);
        if (temp != null)
            temp.setUid(requestMessage.getUid());

        return temp;
    }

    public RequestMessage translateXMLStringToRequestMessage(
            String messageString, boolean doRecursiveDescent)
            throws XMLTranslationException
    {
        RequestMessage requestMessage = (RequestMessage) ElementState
                .translateFromXMLCharSequence(messageString, requestTranslationSpace);
        return requestMessage;
    }

    /**
     * Remove the argument passed in from the set of connections we know about.
     */
    protected void connectionTerminated()
    {
        connectionCount--;
        // When thread close by unexpected way (such as client just crashes),
        // this method will end the service gracefully.
        terminationAction();
    }

    /**
     * This defines the actions that server needs to perform when the client
     * ends unexpected way. Detail implementations will be in subclasses.
     */
    protected void terminationAction()
    {

    }

    /**
     * Get the message passing context associated with this server.
     * 
     * @return
     */
    public Scope getObjectRegistry()
    {
        return objectRegistry;
    }

    /**
     * @return Returns the translationSpace.
     */
    public TranslationScope getRequestTranslationSpace()
    {
        return requestTranslationSpace;
    }

    public SocketAddress getAddress()
    {
        return this.serverSocket.getLocalSocketAddress();
    }

    public void addServerListener(ServerListener listener)
    {
        this.serverListeners.add(listener);
    }
    
    protected void fireServerEvent(String event)
    {
        for (ServerListener l : serverListeners)
        {
            l.serverEventOccurred(new ServerEvent(event, this));
        }
    }

    /**
     * @return the portNumber
     */
    public int getPortNumber()
    {
        return portNumber;
    }
}
