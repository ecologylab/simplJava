/*
 * Created on May 4, 2006
 */
package ecologylab.services;

import java.net.Socket;

import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;

public abstract class ServicesClientBase extends Debug implements
        ClientConstants
{
    protected Socket         sock;

    protected int            port;

    protected String         server;

    protected NameSpace      translationSpace = null;

    protected ObjectRegistry objectRegistry;

    public ServicesClientBase(String server, int port, NameSpace messageSpace,
            ObjectRegistry objectRegistry)
    {
        this.port = port;
        this.server = server;
        this.translationSpace = messageSpace;

        if (objectRegistry == null)
            objectRegistry = new ObjectRegistry();
        this.objectRegistry = objectRegistry;

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
    public abstract ResponseMessage sendMessage(RequestMessage request);
    
    /**
     * Check to see if the server is running.
     * @return true if the server is running, false otherwise.
     */
    public boolean isServerRunning()
    {
        boolean serverIsRunning = createConnection();
        // we're just checking, don't keep the connection
        disconnect();
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
            Generic.sleep(CONNECTION_RETRY_SLEEP_INTERVAL);
        }
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
