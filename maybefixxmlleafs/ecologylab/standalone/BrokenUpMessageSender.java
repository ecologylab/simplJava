/**
 * 
 */
package ecologylab.standalone;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.messages.PingRequest;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.nio.NIOClient;
import ecologylab.xml.TranslationSpace;

/**
 * @author toupsz
 *
 */
public class BrokenUpMessageSender extends NIOClient
{

    /**
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     */
    public BrokenUpMessageSender(String server, int port,
            TranslationSpace messageSpace, ObjectRegistry objectRegistry)
    {
        super(server, port, messageSpace, objectRegistry);
        // TODO Auto-generated constructor stub
    }

    public void sendInPieces(RequestMessage req)
    {
        
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        PingRequest ping = new PingRequest();
        while (true)
        {
            
        }
    }

}
