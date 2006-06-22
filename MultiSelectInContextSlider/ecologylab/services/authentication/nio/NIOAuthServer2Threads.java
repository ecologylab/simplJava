package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.BindException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.logging.Logging;
import ecologylab.services.nio.MessageProcessor2Threads;
import ecologylab.services.nio.NIOServer2Threads;
import ecologylab.xml.ElementState;
import ecologylab.xml.NameSpace;
import ecologylab.xml.XmlTranslationException;

public class NIOAuthServer2Threads extends NIOServer2Threads implements
        AuthServerRegistryObjects, AuthMessages, AuthLogging
{
    private LinkedList logListeners = new LinkedList();

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
    public static NIOAuthServer2Threads get(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            String authListFilename)
    {
        NIOAuthServer2Threads newServer = null;

        try
        {
            AuthenticationList authList = (AuthenticationList) ElementState
                    .translateFromXML(authListFilename, NameSpace.get(
                            "authListNameSpace",
                            "ecologylab.services.authentication"));
            newServer = new NIOAuthServer2Threads(portNumber,
                    requestTranslationSpace, objectRegistry, authList);
        }
        catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        }
        catch (XmlTranslationException e)
        {
            e.printStackTrace();
        }

        return newServer;
    }

    /**
     * This is the actual way to create an instance of this.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authList -
     *            the AuthorizationList object to be used to determine possible
     *            users.
     * @return A server instance, or null if it was not possible to open a
     *         ServerSocket on the port on this machine.
     */
    public static NIOAuthServer2Threads get(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList)
    {
        NIOAuthServer2Threads newServer = null;

        try
        {
            newServer = new NIOAuthServer2Threads(portNumber,
                    requestTranslationSpace, objectRegistry, authList);
        }
        catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        }

        return newServer;
    }

    public NIOAuthServer2Threads(int portNumber,
            NameSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Login");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Logout");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages",
                "LoginStatusResponse");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages",
                "LogoutStatusResponse");

        this.objectRegistry.registerObject(AUTHENTICATION_LIST, authList);

        this.objectRegistry.registerObject(AUTHENTICATED_CLIENTS_BY_USERNAME,
                new HashMap());
        this.objectRegistry.registerObject(AUTHENTICATED_CLIENTS_BY_TOKEN,
                new HashMap());
        
        this.objectRegistry.registerObject(AUTH_SERVER, this);
    }

    protected MessageProcessor2Threads generateMessageProcessor(
            NameSpace translationSpace, ObjectRegistry registry)
    {
        return new AuthMessageProcessor2Threads(translationSpace, registry);
    }

    public void addLoggingListener(Logging log)
    {
        logListeners.add(log);
    }

    public void fireLoggingEvent(AuthenticationOp op)
    {
        Iterator loggingListenerIter = logListeners.iterator();

        while (loggingListenerIter.hasNext())
        {
            ((Logging) loggingListenerIter.next()).logAction(op);
        }
    }
}
