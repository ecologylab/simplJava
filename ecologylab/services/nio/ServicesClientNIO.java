/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServicesClient;
import ecologylab.services.ServicesClientBase;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.NameSpace;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class ServicesClientNIO extends ServicesClientBase
{

    private Charset charset = Charset.forName("ASCII");
    private CharsetDecoder decoder = charset.newDecoder();
    private CharsetEncoder encoder = charset.newEncoder();
    
    private Selector selector = null;
    private SocketChannel channel = null;

    /**
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     */
    public ServicesClientNIO(String server, int port, NameSpace messageSpace, ObjectRegistry objectRegistry)
    {
        super(server, port, messageSpace, objectRegistry);
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#connect()
     */
    public boolean connect()
    {
        try
        {
            selector = Selector.open();
            channel = SocketChannel.open();d
            
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress(server, port));
            
            channel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ);
            
            return true;

        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
            return false;
        }
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#connected()
     */
    public boolean connected()
    {
        // TODO Auto-generated method stub
        return super.connected();
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#disconnect()
     */
    public void disconnect()
    {
        // TODO Auto-generated method stub
        super.disconnect();
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#processResponse(ecologylab.services.messages.ResponseMessage)
     */
    protected void processResponse(ResponseMessage responseMessage)
    {
        // TODO Auto-generated method stub
        super.processResponse(responseMessage);
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#sendMessage(ecologylab.services.messages.RequestMessage)
     */
    public ResponseMessage sendMessage(RequestMessage requestMessage)
    {
        // TODO Auto-generated method stub
        return super.sendMessage(requestMessage);
    }

    protected boolean createConnection()
    {
        // TODO Auto-generated method stub
        return false;
    }

    
}
