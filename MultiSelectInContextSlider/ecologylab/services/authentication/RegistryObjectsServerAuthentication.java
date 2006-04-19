/*
 * Created on Apr 17, 2006
 */
package ecologylab.services.authentication;

/**
 * Interface of constants used for the object registry in an authenticating
 * server. This file describes the Strings used, and what they should indicate.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public interface RegistryObjectsServerAuthentication
{

    /**
     * Indicates a HashMap\<String\>\<ServerToClientConnectionAuthentication\>
     * object; used to map usernames to ServerToClientConnectionAuthentication
     * objects that represent their connections. Generally used to ensure that
     * each username is logged in only once.
     */
    public static final String AUTHENTICATED_CLIENTS = "authenticatedClients";

    /**
     * Indicates an AuthenticationList object that contains all the usernames
     * and passwords for the server.
     */
    public static final String AUTHENTICATION_LIST = "authenticationList";
}
