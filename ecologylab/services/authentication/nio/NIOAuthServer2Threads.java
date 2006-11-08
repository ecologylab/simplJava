package ecologylab.services.authentication.nio;

import java.io.IOException;
import java.net.BindException;
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
import ecologylab.services.nio.two_threaded.MessageProcessor2Threads;
import ecologylab.services.nio.two_threaded.NIOServer2Threads;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

public class NIOAuthServer2Threads extends NIOServer2Threads implements
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
    public static NIOAuthServer2Threads get(int portNumber,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, String authListFilename)
    {
        NIOAuthServer2Threads newServer = null;

        try
        {
            newServer = new NIOAuthServer2Threads(portNumber,
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
    public static NIOAuthServer2Threads get(int portNumber,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, AuthenticationList authList)
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

    protected NIOAuthServer2Threads(int portNumber,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, AuthenticationList authList)
            throws IOException, BindException
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

        this.objectRegistry.registerObject(MAIN_AUTHENTICATABLE, this);
        
        this.authenticator = new Authenticator(authList);
    }

    protected MessageProcessor2Threads generateMessageProcessor(
            TranslationSpace translationSpace, ObjectRegistry registry)
    {
        return new AuthMessageProcessor2Threads(translationSpace, registry,
                this);
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

    public void logout(AuthenticationListEntry entry)
    {
        authenticator.logout(entry);
    }

    public boolean isLoggedIn(String username)
    {
        return authenticator.isLoggedIn(username);
    }

    public boolean login(AuthenticationListEntry entry)
    {
        return authenticator.login(entry);
    }
}
