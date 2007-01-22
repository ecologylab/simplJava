/*
 * Created on May 3, 2006
 */
package ecologylab.services;

/**
 * Constants used by ServicesServers and their components.
 */
public interface ServerConstants
{
    /**
     * If we get more bad messages than this, it may be malicous.
     */
    static final int  MAXIMUM_TRANSMISSION_ERRORS = 3;

    /**
     * the maximum size of message acceptable by server
     */
    static final int MAX_PACKET_SIZE             = 96 * 1024;

    /**
     * Limit the maximum number of client connection to the server
     */
    static final int MAX_CONNECTIONS       	  	= 20;
    
    static final int MAX_TARDINESS              = 20;
    
    static final int MAX_TIME_BEFORE_VALID_MSG	= 20000;
    
    static final String CHARACTER_ENCODING = "ASCII";

}
