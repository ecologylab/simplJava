/*
 * Created on Mar 30, 2006
 */
package ecologylab.services.authentication;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

import ecologylab.services.ServerToClientConnection;
import ecologylab.services.ServicesServer;
import ecologylab.services.messages.BadSemanticContentResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;

public class ServerToClientConnectionAuthentication extends
        ServerToClientConnection implements AuthenticationMessages {

    private boolean loggedIn = false;

    private ResponseMessage responseMessage;

    public ServerToClientConnectionAuthentication(Socket incomingSocket,
            ServicesServer servicesServer) throws IOException 
            {
        super(incomingSocket, servicesServer);
    }

	/**
	 * Calls the super implementation of performService if this thread is logged-in; otherwise, it bounces messages until a Login message is successful.
	 */
	protected ResponseMessage performService(RequestMessage requestMessage) 
	{
		//		 if not logged in yet, make sure they log in first
        if (!loggedIn) 
        {
            if (requestMessage instanceof Login) 
            {
                // since this is a Login message, perform it.
                responseMessage = super.performService(requestMessage);

                if (responseMessage.isOK()) {
                	// mark as logged in, and add to the authenticatedClients in the object registry
                    loggedIn = true;
                    ((HashMap)
                    		(servicesServer.getObjectRegistry())
                    		.lookupObject("authenticatedClients"))
                    		.put(((Login)requestMessage).getEntry().getUsername(), this);
                }
                
            } else 
            { // otherwise we consider it bad!
                responseMessage = new BadSemanticContentResponse(REQUEST_FAILED_NOT_AUTHENTICATED);
            }
                    
        } else {
        	if (requestMessage instanceof Logout) 
        	{         		
        		responseMessage = super.performService(requestMessage);
        		
        		if (responseMessage.isOK()) {
        			loggedIn = false;
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
