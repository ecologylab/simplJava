/*
 * Created on May 12, 2006
 */
package ecologylab.services.authentication;

public interface AuthConstants
{
    /**
     * Specifies how long an NIOAuthClient will wait for a response from the server regarding log in status.
     */
    public static final int LOGIN_WAIT_TIME = 10000;
    
    public static final String LOGIN_FAILED_TIMEOUT = "Server failed to respond after waiting for "+(LOGIN_WAIT_TIME/1000)+" seconds.";
}
