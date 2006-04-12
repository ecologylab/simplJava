/*
 * Created on Apr 6, 2006
 */
package ecologylab.services.authentication;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServicesClient;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;

public class ServicesClientAuthentication extends ServicesClient implements AuthenticationMessages
{
    private AuthenticationListEntry entry = null;
    private boolean loggedIn = false;

    /**
     * @return Returns the loggedIn.
     */
    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public ServicesClientAuthentication(String server, int port) 
    {
        this(server, port, null);
    }
    
    public ServicesClientAuthentication(String server, int port, NameSpace messageSpace, ObjectRegistry objectRegistry)
    {
        this(server, port, messageSpace, objectRegistry, null);
    }
    
    public ServicesClientAuthentication(String server, int port, AuthenticationListEntry entry)
    {
        this(server, port, NameSpace.get("authClient", "ecologylab.services.authentication"), new ObjectRegistry(), entry);
    }
    
    /**
     * Main constructor; creates a new ServicesClientAuthentication using the parameters.
     * 
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     * @param entry
     */
    public ServicesClientAuthentication(String server, int port, NameSpace messageSpace, ObjectRegistry objectRegistry, AuthenticationListEntry entry)
    {
        super(server, port, messageSpace, objectRegistry);
        
        messageSpace.addTranslation("ecologylab.services.authentication", "Login");
        messageSpace.addTranslation("ecologylab.services.authentication", "Logout");
        messageSpace.addTranslation("ecologylab.services.authentication", "AuthenticationListEntry");
        
        messageSpace.addTranslation("ecologylab.services.messages", "OKResponse");
        messageSpace.addTranslation("ecologylab.services.messages", "BadSemanticContentResponse");
        messageSpace.addTranslation("ecologylab.services.messages", "ErrorResponse");
        
        this.entry = entry;
    }

    /**
     * @param entry The entry to set.
     */
    public void setEntry(AuthenticationListEntry entry)
    {
        this.entry = entry;
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#connect()
     */
    public boolean connect()
    {
        ResponseMessage response = null;
        
        super.connect();
        
        // if we have an entry (username + password), then we can try to connect to the server.
        if (entry != null) {
            response = this.sendMessage(new Login(entry));
            
            if (response.isOK()) {
                loggedIn = true;
            } else
            {
                loggedIn = false;
            }
            
        } else
        {
            loggedIn = false;
        }
        
        if (!loggedIn)
        {
            super.disconnect();
        }
        
        return this.connected();
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#processResponse(ecologylab.services.messages.ResponseMessage)
     */
    protected void processResponse(ResponseMessage responseMessage)
    {
        // TODO Auto-generated method stub
        super.processResponse(responseMessage);
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#connected()
     */
    public boolean connected()
    {
        return (loggedIn && super.connected());
    }
}
