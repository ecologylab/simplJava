/*
 * Created on May 4, 2006
 */
package ecologylab.services.authentication;

import java.net.InetAddress;
import java.nio.channels.Channel;
import java.util.HashMap;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.authentication.messages.Login;
import ecologylab.services.authentication.messages.Logout;
import ecologylab.services.authentication.messages.AuthMessages;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.nio.MessageProcessor;
import ecologylab.xml.NameSpace;

public class AuthMessageProcessor extends MessageProcessor implements
        RegistryObjectsServerAuthentication, AuthMessages
{
    private boolean     loggedIn      = false;

    private InetAddress clientAddress = null;

    public AuthMessageProcessor(AuthMessageProcessorPool pool, Channel channel,
            Object token, NameSpace translationSpace, ObjectRegistry registry,
            InetAddress clientAddress)
    {
        super(pool, channel, token, translationSpace, registry);
        
        this.clientAddress = clientAddress;
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
                ((Login) requestMessage).setClientAddress(clientAddress);

                // since this is a Login message, perform it.
                response = super.performService(requestMessage);

                if (response.isOK())
                {
                    // mark as logged in, and add to the authenticated clients
                    // in the object registry
                    loggedIn = true;

                    ((HashMap) (registry)
                            .lookupObject(AUTHENTICATED_CLIENTS_BY_USERNAME))
                            .put(((Login) requestMessage).getEntry()
                                    .getUsername(), token);

                    ((HashMap) (registry)
                            .lookupObject(AUTHENTICATED_CLIENTS_BY_TOKEN)).put(
                            token, ((Login) requestMessage).getEntry()
                                    .getUsername());
                }

            } else
            { // otherwise we consider it bad!
                response = new BadSemanticContentResponse(
                        REQUEST_FAILED_NOT_AUTHENTICATED);
            }

        } else
        {
            if (requestMessage instanceof Logout)
            {
                response = super.performService(requestMessage);

                if (response.isOK())
                {
                    loggedIn = false;
                    
                    ((HashMap) (registry)
                            .lookupObject(AUTHENTICATED_CLIENTS_BY_USERNAME))
                            .remove(((Login) requestMessage).getEntry()
                                    .getUsername());

                    ((HashMap) (registry)
                            .lookupObject(AUTHENTICATED_CLIENTS_BY_TOKEN)).remove(
                            token);
                }
            } else
            {
                response = super.performService(requestMessage);
            }
        }

        // return the response message
        return response;
    }
}
