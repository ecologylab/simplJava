/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.io.IOException;
import java.net.BindException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerToClientConnection;
import ecologylab.services.ServicesServer;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.logging.Logging;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * ServiceServerAuthentication is used to handle servers that require
 * authentication. This means that the ServerToClientConnection will require
 * that the client log in before it will perform any other work.
 * 
 * It also means that the objectRegistry must contain "authenticationList" and
 * "authenticatedClients" entries. These are created by the constructors in this
 * class.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class AuthServer extends ServicesServer implements
        AuthServerRegistryObjects, AuthLogging
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
    public static AuthServer get(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            String authListFilename)
    {
        AuthServer newServer = null;
        try
        {
            AuthenticationList authList = (AuthenticationList) ElementState
                    .translateFromXML(authListFilename, TranslationSpace.get(
                            "authListNameSpace",
                            "ecologylab.services.authentication"));
            newServer = new AuthServer(portNumber,
                    requestTranslationSpace, objectRegistry, authList);
        } catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        } catch (XmlTranslationException e)
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
    public static AuthServer get(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList)
    {
        AuthServer newServer = null;
        try
        {
            newServer = new AuthServer(portNumber,
                    requestTranslationSpace, objectRegistry, authList);
        } catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        }

        return newServer;
    }

    /**
     * Creates a new AuthServer with the given arguments. This
     * constructor should only be invoked by a subclass or the .get() method to
     * ensure that only one server is running on the given port.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     * @param authList
     * @throws IOException
     * @throws BindException
     */
    protected AuthServer(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        requestTranslationSpace.addTranslation(
                ecologylab.services.authentication.messages.Login.class);
        requestTranslationSpace.addTranslation(
                ecologylab.services.authentication.messages.Logout.class);

        this.objectRegistry = objectRegistry;
        
        this.objectRegistry.registerObject(AUTHENTICATION_LIST, authList);

        this.objectRegistry
                .registerObject(AUTHENTICATED_CLIENTS_BY_USERNAME, new HashMap());
    }

    /**
     * Create a ServerToClientConnectionAuthentication, the object that handles
     * the connection to each incoming client and requires that they pass a
     * Login item that matches an entry in the authenticationList. To extend the
     * functionality of the client, you can override this method in your
     * subclass of this, to return a subclass of ServerToClientConnection.
     * 
     * @param incomingSocket
     * @return
     * @throws IOException
     */
    protected ServerToClientConnection getConnection(Socket incomingSocket)
            throws IOException
    {
        return new AuthServerToClientConnection(incomingSocket, this);
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
            ((Logging)loggingListenerIter.next()).logAction(op);
        }
    }
}
