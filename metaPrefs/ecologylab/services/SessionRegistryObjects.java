/*
 * Created on Apr 13, 2006
 */
package ecologylab.services;

public interface SessionRegistryObjects
{

    /**
     * registry name for a HashMap of UID Strings to ServerToClientConnection objects.
     */
    final static String CONNECTIONS = "connections";

    /**
     * registry name for a String indicating the IP address of the server
     */
    final static String SERVER_IP = "serverIP";
}
