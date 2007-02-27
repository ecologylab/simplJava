package ecologylab.services.authentication.nio;

import java.nio.channels.SocketChannel;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.authentication.logging.AuthLogging;
import ecologylab.services.authentication.logging.AuthenticationOp;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.LoginStatusResponse;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.messages.LogoutStatusResponse;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.ContextManager;
import ecologylab.services.nio.NIOServerBackend;
import ecologylab.xml.TranslationSpace;

/**
 * Stores information about the connection context for the client. Should be
 * extended for more specific implementations. Handles accumulating incoming
 * messages and translating them into RequestMessage objects.
 * 
 * @author Zach Toups
 */
public class AuthContextManager extends ContextManager implements
        ServerConstants, AuthServerRegistryObjects, AuthMessages
{
    private boolean     loggedIn       = false;

    private AuthLogging servicesServer = null;

    public AuthContextManager(Object token, int maxPacketSize, NIOServerBackend server, SocketChannel socket,
            TranslationSpace translationSpace, ObjectRegistry registry,
            AuthLogging servicesServer)
    {
        super(token, maxPacketSize, server, socket, translationSpace, registry);

        this.servicesServer = servicesServer;
    }

    /**
     * Calls performService on the given RequestMessage using the local
     * ObjectRegistry. Can be overridden by subclasses to provide more
     * specialized functionality.
     * 
     * @param requestMessage
     * @return
     */
    protected ResponseMessage performService(RequestMessage requestMessage)
    {
        ResponseMessage response;

        // if not logged in yet, make sure they log in first
        if (!loggedIn)
        {
            if (requestMessage instanceof Login)
            {
                // since this is a Login message, perform it.
                response = super.performService(requestMessage);

                if (response.isOK())
                {
                    // mark as logged in, and add to the authenticated
                    // clients
                    // in the object registry
                    loggedIn = true;
                }

                // tell the server to log it
                servicesServer.fireLoggingEvent(new AuthenticationOp(
                        ((Login) requestMessage).getEntry().getUsername(),
                        true, ((LoginStatusResponse) response)
                                .getResponseMessage(), socket.socket().getInetAddress()
                                .toString(), socket.socket().getPort()));
            }
            else
            { // otherwise we consider it bad!
                response = new BadSemanticContentResponse(
                        REQUEST_FAILED_NOT_AUTHENTICATED);
            }

        }
        else
        {
            response = super.performService(requestMessage);

            if (requestMessage instanceof Logout)
            {
                // tell the server to log it
                servicesServer.fireLoggingEvent(new AuthenticationOp(
                        ((Logout) requestMessage).getEntry().getUsername(),
                        false, ((LogoutStatusResponse) response)
                                .getResponseMessage(), socket.socket().getInetAddress()
                                .toString(), socket.socket().getPort()));
            }
        }

        // return the response message
        return response;
    }
}
