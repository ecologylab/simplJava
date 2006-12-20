package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
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
import ecologylab.services.nio.NIOServerBase;
import ecologylab.services.nio.action_processor.ServerActionProcessor;
import ecologylab.services.nio.two_threaded.MessageProcessor2Threads;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

public class NIOAuthServer extends NIOServerBase implements
        AuthServerRegistryObjects, AuthMessages, AuthLogging, Authenticatable
{
    private LinkedList<Logging> logListeners = new LinkedList<Logging>();
    
    protected Authenticator authenticator = null;

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
    public static NIOAuthServer get(int portNumber, InetAddress inetAddress, 
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, String authListFilename)
    {
        NIOAuthServer newServer = null;

        try
        {
            newServer = new NIOAuthServer(portNumber, inetAddress,
                    requestTranslationSpace, objectRegistry,
                    (AuthenticationList) ElementState.translateFromXML(
                            authListFilename, TranslationSpace.get(
                                    "authListNameSpace",
                                    "ecologylab.services.authentication")));
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
    public static NIOAuthServer get(int portNumber, InetAddress inetAddress,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, AuthenticationList authList)
    {
        NIOAuthServer newServer = null;

        try
        {
            newServer = new NIOAuthServer(portNumber, inetAddress,
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

    protected NIOAuthServer(int portNumber, InetAddress inetAddress, 
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, AuthenticationList authList)
            throws IOException, BindException
    {
        super(portNumber, new AuthServerActionProcessor(), requestTranslationSpace, objectRegistry);

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

        this.objectRegistry.registerObject(MAIN_AUTHENTICATABLE, this);
        
        this.authenticator = new Authenticator(authList);
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

    public void shutdown()
    {
        // TODO Auto-generated method stub
        
    }

}
