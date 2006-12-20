/*
 * Created on May 15, 2006
 */
package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.util.LinkedList;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.Authenticator;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.logging.Logging;
import ecologylab.services.nio.single_threaded.NIOServer1Thread;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

public class NIOAuthServer1Thread extends NIOServer1Thread implements
AuthServerRegistryObjects, AuthLogging, Authenticatable
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
    public static NIOAuthServer1Thread get(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            String authListFilename)
    {
        NIOAuthServer1Thread newServer = null;
        
        try
        {
            AuthenticationList authList = (AuthenticationList) ElementState
                    .translateFromXML(authListFilename, TranslationSpace.get(
                            "authListNameSpace",
                            "ecologylab.services.authentication"));
            newServer = new NIOAuthServer1Thread(portNumber,
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
    public static NIOAuthServer1Thread get(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList)
    {
        NIOAuthServer1Thread newServer = null;
        
        try
        {
            newServer = new NIOAuthServer1Thread(portNumber,
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
    protected NIOAuthServer1Thread(int portNumber,
            TranslationSpace requestTranslationSpace, ObjectRegistry objectRegistry,
            AuthenticationList authList) throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Login");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Logout");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "LoginStatusResponse");
        requestTranslationSpace.addTranslation(
                "ecologylab.services.authentication.messages", "LogoutStatusResponse");
        
        authenticator = new Authenticator(authList);
        
        this.objectRegistry.registerObject(MAIN_AUTHENTICATABLE, this);
    }
    
    public void addLoggingListener(Logging log)
    {
        logListeners.add(log);
    }
    
    public void fireLoggingEvent(AuthenticationOp op)
    {
        for (Logging logListener : logListeners)
        {
            logListener.logAction(op);
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
