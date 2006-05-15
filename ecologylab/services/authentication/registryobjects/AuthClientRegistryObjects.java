/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication.registryobjects;

public interface AuthClientRegistryObjects
{
    /**
     * Indicates whether or not a client is logged in.
     * 
     * Type: BooleanSlot
     */
    public static final String LOGIN_STATUS = "loginStatus";
    
    /**
     * Indicates the most recent server response regarding logging-in.
     * 
     * Type: String
     */
    public static final String LOGIN_STATUS_STRING = "loginStatusString";
}
