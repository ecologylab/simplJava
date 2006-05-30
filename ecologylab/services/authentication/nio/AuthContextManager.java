package ecologylab.services.authentication.nio;

import java.net.InetAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServerConstants;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.registryobjects.AuthServerRegistryObjects;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.ContextManager;
import ecologylab.xml.NameSpace;

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
    private boolean     loggedIn      = false;

    private InetAddress clientAddress = null;

    public AuthContextManager(Object token, SelectionKey key,
            NameSpace translationSpace, ObjectRegistry registry)
    {
        super(token, key, translationSpace, registry);

        this.clientAddress = ((SocketChannel) key.channel()).socket()
                .getInetAddress();
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
                    // login needs to have it's IP address added before anything
                    // is
                    // done with it!
                    ((Login) requestMessage).setClientAddress(clientAddress);

                    // since this is a Login message, perform it.
                    response = super.performService(requestMessage);

                    if (response.isOK())
                    {
                        // mark as logged in, and add to the authenticated
                        // clients
                        // in the object registry
                        loggedIn = true;

                        ((HashMap) (registry)
                                .lookupObject(AUTHENTICATED_CLIENTS_BY_USERNAME))
                                .put(((Login) requestMessage).getEntry()
                                        .getUsername(), key.attachment());

                        ((HashMap) (registry)
                                .lookupObject(AUTHENTICATED_CLIENTS_BY_TOKEN))
                                .put(key.attachment(), ((Login) requestMessage)
                                        .getEntry().getUsername());
                    }

                }
                else
                { // otherwise we consider it bad!
                    response = new BadSemanticContentResponse(
                            REQUEST_FAILED_NOT_AUTHENTICATED);
                }

            }
            else
            {
                if (requestMessage instanceof Logout)
                {
                    response = super.performService(requestMessage);

                    if (response.isOK())
                    {
                        loggedIn = false;

                        ((HashMap) (registry)
                                .lookupObject(AUTHENTICATED_CLIENTS_BY_USERNAME))
                                .remove(((Logout) requestMessage).entry
                                        .getUsername());

                        ((HashMap) (registry)
                                .lookupObject(AUTHENTICATED_CLIENTS_BY_TOKEN))
                                .remove(key.attachment());
                    }
                }
                else
                {
                    response = super.performService(requestMessage);
                }
            }

            // return the response message
            return response;
        
    }
}
