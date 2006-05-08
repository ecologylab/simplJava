/*
 * Created on Apr 13, 2006
 */
package ecologylab.services.messages;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;

import sun.misc.BASE64Encoder;
import ecologylab.generic.ObjectRegistry;
import ecologylab.services.SessionRegistryObjects;

/**
 * RequestConnection sends a request for a UID for the session. A proper response is a
 * RespondWithUID object.
 * 
 * @author Zach Toups (toupsz@gmail.com)
 */
public class RequestConnection extends RequestMessage implements
        SessionRegistryObjects
{
    public String clientIP;

    public int clientPort;

    public String uid;
    
    public Date startTime;

    /**
     * Constructor for XML translations only.
     * 
     */
    public RequestConnection()
    {
        super();
    }

    public RequestConnection(String clientIP, int clientPort)
    {
        this.clientIP = clientIP;
        this.clientPort = clientPort;
    }

    /**
     * Requires SERVER_IP and CONNECTIONS be in the objectRegistry.
     */
    public ResponseMessage performService(ObjectRegistry objectRegistry)
    {
        // set the startTime
        startTime = new Date();
        
        // generate the UID
        uid = generateUID((String)objectRegistry.lookupObject(SERVER_IP), startTime);
        
        // then add this UID and the start time to the hashmap in the registry
        ((HashMap)objectRegistry.lookupObject(CONNECTIONS)).put(uid, startTime);

        return new RespondWithUID(uid);
    }

    /**
     * Uses the server's IP, the client's IP, the client's port, and the current
     * time to produce a unique identifier by running a SHA-256 hash on them.
     * The hashed string is in the form of: \<client IP\>\<client port\>\<server
     * IP\>\<date\>\<time\>.
     * 
     * Updates the uid value.
     * 
     * @param serverIP -
     *            the IP address of the server.
     */
    private String generateUID(String serverIP, Date startTime)
    {
        try
        {
            MessageDigest hasher = MessageDigest.getInstance("SHA-256");

            // now we build up the information for the digest
            hasher.update(clientIP.getBytes());

            hasher.update(Integer.toString(clientPort).getBytes());

            hasher.update(serverIP.getBytes());

            hasher.update(startTime.toString().getBytes());

            // convert to normal characters and return as a String
            return new String((new BASE64Encoder()).encode(hasher.digest()));

        } catch (NoSuchAlgorithmException e)
        {
            // this won't happen in practice, once we have the right one! :D
            e.printStackTrace();
        }
        
        // this cannot happen
        return null;
    }
}
