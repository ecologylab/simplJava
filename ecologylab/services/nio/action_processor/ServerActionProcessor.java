/**
 * 
 */
package ecologylab.services.nio.action_processor;

import java.nio.channels.SocketChannel;

import ecologylab.generic.StartAndStoppable;
import ecologylab.services.BadClientException;
import ecologylab.services.nio.NIOServerBase;

/**
 * ServerActionProcessor objects handle the translation of request/response
 * messages and their actions on a server. These objects keep different
 * connections separate. They are used in conjunction with an NIO services
 * server: it will handle all of the actual communication over the network.
 * 
 * @author Zach Toups
 * 
 */
public interface ServerActionProcessor extends StartAndStoppable
{
    /**
     * @param base
     * @param sc
     * @param bs
     * @param bytesRead
     * @throws BadClientException 
     */
    void process(Object token, NIOServerBase base, SocketChannel sc, byte[] bs, int bytesRead) throws BadClientException;
}
