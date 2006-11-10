/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.util.Iterator;
import java.util.LinkedList;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.Authenticator;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.logging.Logging;
import ecologylab.services.nio.MessageProcessor;
import ecologylab.services.nio.n_threaded.NIOServerNThreads;
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
public class NIOAuthServerNThreads extends NIOServerNThreads implements
        AuthServerRegistryObjects, AuthMessages, AuthLogging, Authenticatable
{
    private LinkedList<Logging> logListeners = new LinkedList<Logging>();
    
    private Authenticator authenticator = null;

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
    public static NIOAuthServerNThreads get(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            String authListFilename)
    {
        NIOAuthServerNThreads newServer = null;
        
        try
        {
            AuthenticationList authList = (AuthenticationList) ElementState
                    .translateFromXML(authListFilename, TranslationSpace.get(
                            "authListNameSpace",
                            "ecologylab.services.authentication"));
            newServer = new NIOAuthServerNThreads(portNumber,
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
    public static NIOAuthServerNThreads get(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList)
    {
        NIOAuthServerNThreads newServer = null;
        
        try
        {
            newServer = new NIOAuthServerNThreads(portNumber,
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
    protected NIOAuthServerNThreads(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        requestTranslationSpace.addTranslation(
                ecologylab.services.authentication.messages.Login.class);
        requestTranslationSpace.addTranslation(
                ecologylab.services.authentication.messages.Logout.class);
        requestTranslationSpace.addTranslation(
                ecologylab.services.authentication.messages.LoginStatusResponse.class);
        requestTranslationSpace.addTranslation(
                ecologylab.services.authentication.messages.LogoutStatusResponse.class);
        
        authenticator = new Authenticator(authList);
        
        this.objectRegistry.registerObject(MAIN_AUTHENTICATABLE, this);
    }

    protected MessageProcessor placeKeyInPool(SelectionKey key)
    {
        AuthMessageProcessor temp = new AuthMessageProcessor(key.attachment(), key, requestTranslationSpace, objectRegistry, this);

        pool.put(key.attachment(), temp);
        
        return temp;
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
    
    public boolean logout(AuthenticationListEntry entry, InetAddress address)
    {
        return authenticator.logout(entry, address);
    }

    public boolean isLoggedIn(String username)
    {
        return authenticator.isLoggedIn(username);
    }

    public boolean login(AuthenticationListEntry entry, InetAddress address)
    {
        return authenticator.login(entry, address);
    }

}
