/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication.nio;

import java.io.IOException;

import ecologylab.generic.BooleanSlot;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.AuthConstants;
import ecologylab.services.authentication.AuthenticationListEntry;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.LoginStatusResponse;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.registryobjects.AuthClientRegistryObjects;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.NIOClient;
import ecologylab.xml.TranslationSpace;

public class NIOAuthClient extends /*NIOIntervalClient*/NIOClient implements
        AuthClientRegistryObjects, AuthConstants, AuthMessages
{
    protected AuthenticationListEntry entry      = null;

    /**
     * Indicates whether or not this is trying to log into the server.
     */
    private boolean                   loggingIn  = false;

    private boolean                   loggingOut = false;

    /**
     * @return Returns loggedIn.
     */
    public boolean isLoggedIn()
    {
        return ((BooleanSlot) objectRegistry.lookupObject(LOGIN_STATUS)).value;
    }

    /**
     * Creates a new AuthClient object using the given parameters.
     * 
     * @param server
     * @param port
     */
    public NIOAuthClient(String server, int port)
    {
        this(server, port, null, 0, null);
    }

    /**
     * Creates a new AuthClient object using the given parameters.
     * 
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     */
    public NIOAuthClient(String server, int port, TranslationSpace messageSpace,
            ObjectRegistry objectRegistry, int interval,
            RequestMessage messageToSend)
    {
        this(server, port, messageSpace, objectRegistry, null, interval,
                messageToSend);
    }

    /**
     * Creates a new AuthClient object using the given parameters.
     * 
     * @param server
     * @param port
     * @param entry
     */
    public NIOAuthClient(String server, int port,
            AuthenticationListEntry entry, int interval,
            RequestMessage messageToSend)
    {
        this(server, port, TranslationSpace.get("authClient",
                "ecologylab.services.authentication"), new ObjectRegistry(),
                entry, interval, messageToSend);
    }

    public NIOAuthClient(String server, int port, TranslationSpace messageSpace,
            ObjectRegistry objectRegistry, AuthenticationListEntry entry)
    {
        this(server, port, messageSpace, objectRegistry, entry, 0, null);
    }

    /**
     * Main constructor; creates a new AuthClient using the parameters.
     * 
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     * @param entry
     */
    public NIOAuthClient(String server, int port, TranslationSpace messageSpace,
            ObjectRegistry objectRegistry, AuthenticationListEntry entry,
            int interval, RequestMessage messageToSend)
    {
        super(server, port, messageSpace, objectRegistry/*, interval,
                messageToSend*/);

        messageSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Login");
        messageSpace.addTranslation(
                "ecologylab.services.authentication.messages", "Logout");
        messageSpace.addTranslation("ecologylab.services.authentication",
                "AuthenticationListEntry");
        messageSpace.addTranslation(
                "ecologylab.services.authentication.messages",
                "LoginStatusResponse");

        messageSpace.addTranslation("ecologylab.services.messages",
                "BadSemanticContentResponse");
        messageSpace.addTranslation("ecologylab.services.messages",
                "ErrorResponse");

        objectRegistry.registerObject(LOGIN_STATUS, new BooleanSlot(false));
        objectRegistry.registerObject(LOGIN_STATUS_STRING, null);

        this.entry = entry;
    }

    /**
     * @param entry
     *            The entry to set.
     */
    public void setEntry(AuthenticationListEntry entry)
    {
        this.entry = entry;
    }

    /**
     * Attempts to connect to the server using the AuthenticationListEntry that
     * is associated with the client's side of the connection. Does not block
     * for connection.
     * @throws IOException 
     */
    public boolean login() throws IOException
    {
        // if we have an entry (username + password), then we can try to connect
        // to the server.
        if (entry != null)
        {
            loggingOut = false;
            loggingIn = true;

            // Login response will handle changing the LOGIN_STATUS
            sendLoginMessage();
        }
        else
        {
            debug("ENTRY NOT SET!");
        }

        return isLoggedIn();
    }

    public boolean blockingLogin() throws IOException
    {
        login();

        return isLoggedIn();
    }

    /**
     * Attempts to log out of the server using the AuthenticationListEntry that
     * is associated with the client's side of the connection. Blocks until a
     * response is received or until LOGIN_WAIT_TIME passes, whichever comes
     * first.
     * @throws IOException 
     */
    public boolean logout() throws IOException
    {
        // if we have an entry (username + password), then we can try to logout
        // of
        // the server.
        if (entry != null)
        {
            loggingIn = false;
            loggingOut = true;

            // Login response will handle changing the LOGIN_STATUS
            sendLogoutMessage();
        }

        return isLoggedIn();
    }

    /**
     * Checks whether or not a login call is complete. May be called multiple
     * times.
     * 
     * @return true if a pending login (successful or not) is COMPLETE, false if
     *         it is still in progress.
     * @throws Exception
     *             if no login call has yet been made.
     */
    public boolean finishLogin() throws Exception
    {
        if (!connected())
        {
            throw new Exception("Not yet connected.");
        }
        else if (!loggingIn)
        {
            throw new Exception("No pending login.");
        }
        else
        {
            if (!isLoggedIn())
            { // if we are not logged in, it might be because login failed, or
                // because it's not yet finished; need to determine which.
                if (didLoginFail((String) objectRegistry
                        .lookupObject(LOGIN_STATUS_STRING)))
                { // login is complete, but failed
                    loggingIn = false;
                }
                // otherwise, we just leave it true

            }
            else
            {
                loggingIn = false;
            }
        }

        return !loggingIn;
    }

    public boolean finishLogout() throws Exception
    {
        if (!connected())
        {
            throw new Exception("Not connected.");
        }
        else if (!loggingOut)
        {
            throw new Exception("No pending logout.");
        }
        else
        {
            if (isLoggedIn())
            { // if we are logged in, it might be because logout failed, or
                // because it's not yet finished; need to determine which.
                if (didLogoutFail((String) objectRegistry
                        .lookupObject(LOGIN_STATUS_STRING)))
                { // login is complete, but failed
                    loggingOut = false;
                }
                // otherwise, we just leave it true

            }
            else
            {
                loggingOut = false;
            }
        }

        return !loggingOut;
    }

    public boolean didLogoutFail(String statusString)
    {
        return (LOGOUT_FAILED_NOT_LOGGEDIN.equals(statusString));
    }

    public boolean didLoginFail(String statusString)
    {
        return (LOGIN_FAILED_PASSWORD.equals(statusString) || LOGIN_FAILED_LOGGEDIN
                .equals(statusString));
    }

    /**
     * Sends a Logout message to the server; may be overridden by subclasses
     * that need to add addtional information to the Logout message.
     * 
     */
    protected ResponseMessage sendLogoutMessage() throws IOException
    {
        return this.sendMessage(new Logout(entry), 5000);
    }

    /**
     * Sends a Login message to the server; may be overridden by subclasses that
     * need to add addtional information to the Login message.
     * 
     */
    protected ResponseMessage sendLoginMessage() throws IOException
    {
        ResponseMessage temp = this.sendMessage(new Login(entry), 5000);
        
//        System.out.println(((LoginStatusResponse)temp).responseMessage+", "+((LoginStatusResponse)temp).isOK());
        
        return temp;
    }

    /**
     * @return Returns the loggingIn.
     */
    public boolean isLoggingIn()
    {
        return loggingIn;
    }

    /**
     * @return Returns the loggingOut.
     */
    public boolean isLoggingOut()
    {
        return loggingOut;
    }

    public String getExplanation()
    {
        String temp = (String) objectRegistry.lookupObject(LOGIN_STATUS_STRING);

        if (temp == null)
        {
            return "";
        }
        else
        {
            return temp;
        }
    }
}
