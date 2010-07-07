/*
 * Created on May 4, 2006
 */
package ecologylab.oodss.distributed.legacy;

import java.net.Socket;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.oodss.distributed.common.ClientConstants;
import ecologylab.oodss.messages.InitConnectionResponse;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

/**
 * Abstract base class for building ecologylab.oodss clients.
 *
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public abstract class ServicesClientBase extends Debug implements
        ClientConstants
{
    protected Socket         socket;

    protected int            port;

    protected String         server;

    protected TranslationScope      translationScope = null;

    protected Scope objectRegistry;

    /**
     * Contains the unique identifier for the next message that the client will
     * send.
     */
    private long             uidIndex         = 1;

    public ServicesClientBase(String server, int port, TranslationScope messageSpace,
            Scope objectRegistry)
    {
        this.port = port;
        this.server = server;
        this.translationScope = messageSpace;

        if (objectRegistry == null)
            objectRegistry = new Scope();
        this.objectRegistry = objectRegistry;
        
        this.translationScope.addTranslation(InitConnectionResponse.class);
    }

    protected void processResponse(ResponseMessage responseMessage)
    {
        responseMessage.processResponse(objectRegistry);
    }

    /**
     * Connect to the server (if not already connected). Return connection
     * status.
     * 
     * @return True if connected, false if not.
     */
    public boolean connect()
    {
        if (connected())
            return true;

        return createConnection();
    }

    public abstract void disconnect();

    public abstract boolean connected();

    protected abstract boolean createConnection();

    /**
     * Performs a blocking send: sends request, waits for a ResponseMessage,
     * processes the response, then returns it.
     * 
     * @param request
     *            the request to send to the server.
     * @return the response received from the server.
     */
    public abstract ResponseMessage sendMessage(RequestMessage request);

    /**
     * Check to see if the server is running.
     * 
     * @return true if the server is running, false otherwise.
     */
    public boolean isServerRunning()
    {
        debug("checking availability of server");
        boolean serverIsRunning = createConnection();

        // we're just checking, don't keep the connection
        if (connected())
        {
            debug("server is running; disconnecting");
            disconnect();
        }

        return serverIsRunning;
    }

    /**
     * Try and connect to the server. If we fail, wait
     * CONNECTION_RETRY_SLEEP_INTERVAL and try again. Repeat ad nauseum.
     */
    public void waitForConnect()
    {
        System.out.println("Waiting for a server on port " + port);
        while (!connect())
        {
            // try again soon
            Generic.sleep(WAIT_BEWTEEN_RECONNECT_ATTEMPTS);
        }
    }

    /**
     * Use the ServicesClient and its NameSpace to do the translation. Can be
     * overridden to provide special functionalities
     * 
     * @param messageString
     * @return
     * @throws SIMPLTranslationException
     */
    protected ResponseMessage translateXMLStringToResponseMessage(
            String messageString) throws SIMPLTranslationException
    {
        return translateXMLStringToResponseMessage(messageString, true);
    }

    public ResponseMessage translateXMLStringToResponseMessage(
            String messageString, boolean doRecursiveDescent)
            throws SIMPLTranslationException
    {
        return (ResponseMessage) translationScope.deserializeCharSequence(
                messageString);
    }

    /**
     * Increments the internal tracker of the next UID, and returns the current
     * one.
     * 
     * @return the current uidIndex.
     */
    public long generateUid()
    {
        // return the current value of uidIndex, then increment.
        return uidIndex++;
    }

    /**
     * As getUid(), but does not increment the internal uidIndex.
     * 
     * @return the current uidIndex.
     */
    public long getUidNoIncrement()
    {
        return uidIndex;
    }

    /**
     * @return Returns the server.
     */
    public String getServer()
    {
        return server;
    }

    /**
     * @param server
     *            The server to set.
     */
    public void setServer(String server)
    {
        this.server = server;
    }
}
