/**
 * 
 */
package ecologylab.services.nio.action_processor;

import java.nio.channels.SocketChannel;

import ecologylab.services.nio.NIOServerBase;

/**
 * Uses a group of worker threads to handle processing all of the input from one
 * source. Each worker takes the most recent queue of incoming messages and
 * processes it.
 * 
 * @author Zach Toups
 * 
 */
public class NThreadedActionProcessor implements ServerActionProcessor
{

    /**
     * 
     */
    public NThreadedActionProcessor(int numWorkers)
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ecologylab.services.nio.action_processor.ServerActionProcessor#process(ecologylab.services.nio.NIOServerBase,
     *      java.nio.channels.SocketChannel, byte[], int)
     */
    public void process(NIOServerBase base, SocketChannel sc, byte[] bs,
            int bytesRead)
    {
        // TODO Auto-generated method stub

    }

}
