/**
 * 
 */
package ecologylab.services.nio.action_processor;

import java.nio.channels.SocketChannel;

import ecologylab.services.nio.NIOServerBase;

/**
 * Operates on its own thread to process messages coming in from a client. 
 * @author Zach Toups
 *
 */
public class DoubleThreadedActionProcessor implements ServerActionProcessor
{

    /**
     * 
     */
    public DoubleThreadedActionProcessor()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see ecologylab.services.nio.action_processor.ServerActionProcessor#process(ecologylab.services.nio.NIOServerBase, java.nio.channels.SocketChannel, byte[], int)
     */
    public void process(NIOServerBase base, SocketChannel sc, byte[] bs,
            int bytesRead)
    {
        // TODO Auto-generated method stub

    }

}
