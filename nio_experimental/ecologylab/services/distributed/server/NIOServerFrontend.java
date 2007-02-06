/**
 * 
 */
package ecologylab.services.nio.servers;

import java.nio.channels.SocketChannel;

import ecologylab.generic.StartAndStoppable;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.nio.NIOServerBackend;

/**
 * ServerActionProcessor objects handle the translation of request/response
 * messages and their actions on a server. These objects keep different
 * connections separate. They are used in conjunction with an NIO services
 * server: it will handle all of the actual communication over the network.
 * 
 * @author Zach Toups
 * 
 */
public interface NIOServerFrontend extends StartAndStoppable
{
    /**
     * @param base
     * @param sc
     * @param bs
     * @param bytesRead
     * @throws BadClientException
     */
    void processRead(Object token, NIOServerBackend base, SocketChannel sc,
            byte[] bs, int bytesRead) throws BadClientException;
 
    void accept(Object token, NIOServerBackend base, SocketChannel sc);
    
    void invalidate(Object token, NIOServerBackend base, SocketChannel sc);
}
