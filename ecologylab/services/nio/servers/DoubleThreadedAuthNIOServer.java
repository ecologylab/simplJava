/**
 * 
 */
package ecologylab.services.nio.servers;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.authentication.Authenticatable;
import ecologylab.services.authentication.AuthenticationList;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.Authenticator;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.LoginStatusResponse;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.messages.LogoutStatusResponse;
import ecologylab.services.authentication.nio.AuthContextManager;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.logging.Logging;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Zach Toups
 * 
 */
public class DoubleThreadedAuthNIOServer extends DoubleThreadedNIOServer
        implements AuthServerRegistryObjects, AuthMessages, AuthLogging,
        Authenticatable
{
    private LinkedList<Logging> logListeners  = new LinkedList<Logging>();

    protected Authenticator     authenticator = null;

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
    public static DoubleThreadedAuthNIOServer getInstance(int portNumber,
            InetAddress[] inetAddress, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize, String authListFilename)
    {
        DoubleThreadedAuthNIOServer newServer = null;

        try
        {
            newServer = new DoubleThreadedAuthNIOServer(portNumber,
                    inetAddress, requestTranslationSpace, objectRegistry,
                    idleConnectionTimeout, maxPacketSize,
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
    public static DoubleThreadedAuthNIOServer getInstance(int portNumber,
            InetAddress[] inetAddress, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize, AuthenticationList authList)
    {
        DoubleThreadedAuthNIOServer newServer = null;

        try
        {
            newServer = new DoubleThreadedAuthNIOServer(portNumber,
                    inetAddress, requestTranslationSpace, objectRegistry,
                    idleConnectionTimeout, maxPacketSize, authList);
        }
        catch (IOException e)
        {
            println("ServicesServer ERROR: can't open ServerSocket on port "
                    + portNumber);
            e.printStackTrace();
        }

        return newServer;
    }

    /**
     * @param portNumber
     * @param inetAddress
     * @param requestTranslationSpace
     * @param objectRegistry
     * @throws IOException
     * @throws BindException
     */
    protected DoubleThreadedAuthNIOServer(int portNumber,
            InetAddress[] inetAddress, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleConnectionTimeout,
            int maxPacketSize, AuthenticationList authList) throws IOException,
            BindException
    {
        super(portNumber, inetAddress, requestTranslationSpace, objectRegistry,
                idleConnectionTimeout, maxPacketSize);

        this.registry.registerObject(MAIN_AUTHENTICATABLE, this);

        this.translationSpace.addTranslation(Login.class);
        this.translationSpace.addTranslation(Logout.class);
        this.translationSpace.addTranslation(LoginStatusResponse.class);
        this.translationSpace.addTranslation(LogoutStatusResponse.class);

        authenticator = new Authenticator(authList);
    }

    /**
     * 
     * @param token
     * @param translationSpace
     * @param registry
     * @return
     */
    protected ContextManager generateContextManager(Object token,
            SocketChannel sc, TranslationSpace translationSpace,
            ObjectRegistry registry)
    {
        try
        {
            return new AuthContextManager(token, maxPacketSize, getBackend(),
                    this, sc, translationSpace, registry, this);
        }
        catch (ClassCastException e)
        {
            debug("ATTEMPT TO USE AuthMessageProcessor WITH A NON-AUTHENTICATING SERVER!");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @see ecologylab.services.authentication.logging.AuthLogging#addLoggingListener(ecologylab.services.logging.Logging)
     */
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

    private void remove(InetAddress address)
    {
        authenticator.remove(address);
    }

    /**
     * Ensure that the user associated with sc has been logged out of the
     * authenticator, then call super.invalidate().
     * 
     * @see ecologylab.services.nio.servers.DoubleThreadedNIOServer#invalidate(java.lang.Object,
     *      ecologylab.services.nio.NIOServerBackend,
     *      java.nio.channels.SocketChannel)
     */
    @Override public ContextManager invalidate(Object token,
            NIOServerBackend base, SocketChannel sc, boolean permanent)
    {
        if (permanent)
        {
            InetAddress addr = sc.socket().getInetAddress();

            this.remove(addr);
        }
        return super.invalidate(token, base, sc, permanent);
    }
}
