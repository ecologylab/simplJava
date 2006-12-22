/**
 * 
 */
package ecologylab.services.nio.servers;

import java.nio.channels.SocketChannel;

import ecologylab.services.nio.NIOServerBackend;

/**
 * Uses a group of worker threads to handle processing all of the input from one
 * source. Each worker takes the most recent queue of incoming messages and
 * processes it.
 * 
 * @author Zach Toups
 * 
 */
public class NThreadedNIOServer extends NIOServerBase
{

    /**
     * 
     */
    public NThreadedNIOServer(int numWorkers)
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ecologylab.services.nio.servers.NIOServerFrontend#process(ecologylab.services.nio.NIOServerBackend,
     *      java.nio.channels.SocketChannel, byte[], int)
     */
    public void process(NIOServerBackend base, SocketChannel sc, byte[] bs,
            int bytesRead)
    {
        // TODO Auto-generated method stub

    }

}
