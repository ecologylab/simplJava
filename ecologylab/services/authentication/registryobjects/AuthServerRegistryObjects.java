/*
 * Created on Apr 17, 2006
 */
package ecologylab.services.authentication.registryobjects;

/**
 * Interface of constants used for the object registry in an authenticating
 * server. This file describes the Strings used, and what they should indicate.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public interface AuthServerRegistryObjects
{

    /**
     * Indicates a HashMap\<String\>\<ServerToClientConnectionAuthentication\>
     * object; used to map usernames to ServerToClientConnectionAuthentication
     * objects that represent their connections. Generally used to ensure that
     * each username is logged in only once.
     * 
     * XXX CHANGED TO USERNAME + IP!
     * XXX CHANGE TO USERNAME + TOKEN FOR NORMAL SERVICES SERVER
     */
    public static final String AUTHENTICATED_CLIENTS_BY_USERNAME = "authenticatedClientsByUsername";
    
    public static final String AUTHENTICATED_CLIENTS_BY_TOKEN = "authenticatedClientsByToken";

    /**
     * Indicates an AuthenticationList object that contains all the usernames
     * and passwords for the server.
     */
    public static final String AUTHENTICATION_LIST = "authenticationList";
    
    /**
     * Stores the Authentication Server reference. May be any implementation of the Auth Server.
     */
    public static final String AUTH_SERVER = "authServer";
}
