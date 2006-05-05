/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import ecologylab.services.ServerToClientConnection;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

/**
 * ServerToClientConnectionAuthentication represents the connection between a
 * server and client on the server side that requires authentication. This class
 * makes sure that the client cannot execute any RequestMessages until it has
 * first successfully logged in to the server.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class ServerToClientConnectionAuthentication extends
        ServerToClientConnection implements AuthMessages,
        RegistryObjectsServerAuthentication
{

    private boolean         loggedIn = false;

    private ResponseMessage responseMessage;

    /**
     * Creates a new ServerToClientAuthentication object using the given
     * arguments.
     * 
     * @param incomingSocket
     * @param servicesServer
     * @throws IOException
     */
    public ServerToClientConnectionAuthentication(Socket incomingSocket,
            AuthServer servicesServer) throws IOException
    {
        super(incomingSocket, servicesServer);
    }

    /**
     * Calls the super implementation of performService if this thread is
     * logged-in; otherwise, it bounces messages (with a
     * BadSemanticContentResponse whose error is
     * REQUEST_FAILED_NOT_AUTHENTICATED) until a Login message is successful.
     */
    protected ResponseMessage performService(RequestMessage requestMessage)
    {
        // if not logged in yet, make sure they log in first
        if (!loggedIn)
        {
            if (requestMessage instanceof Login)
            {
                // login needs to have it's IP address added before anything is
                // done with it!
                ((Login) requestMessage).setClientAddress(this.incomingSocket
                        .getInetAddress());

                // since this is a Login message, perform it.
                responseMessage = super.performService(requestMessage);

                if (responseMessage.isOK())
                {
                    // mark as logged in, and add to the authenticatedClients in
                    // the object registry
                    loggedIn = true;
                    ((HashMap) (servicesServer.getObjectRegistry())
                            .lookupObject(AUTHENTICATED_CLIENTS_BY_USERNAME)).put(
                            ((Login) requestMessage).getEntry().getUsername(),
                            this.incomingSocket.getInetAddress());

                    System.out.println(this.incomingSocket.getInetAddress()
                            .toString());
                }

            } else
            { // otherwise we consider it bad!
                responseMessage = new BadSemanticContentResponse(
                        REQUEST_FAILED_NOT_AUTHENTICATED);
            }

        } else
        {
            if (requestMessage instanceof Logout)
            {
                responseMessage = super.performService(requestMessage);

                if (responseMessage.isOK())
                {
                    loggedIn = false;
                    
                    ((HashMap) (servicesServer.getObjectRegistry())
                            .lookupObject(AUTHENTICATED_CLIENTS_BY_USERNAME))
                            .remove(((Login) requestMessage).getEntry()
                                    .getUsername());
                }
            } else
            {
                responseMessage = super.performService(requestMessage);
            }
        }

        // return the response message
        return responseMessage;
    }
}
