/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import ecologylab.generic.ObjectRegistry;
import ecologylab.services.ServicesClient;
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

public class ServicesClientNIO extends ServicesClient
{

    private Selector selector = null;
    private SocketChannel channel = null;
    
    /**
     * @param port
     * @param messageSpace
     */
    public ServicesClientNIO(int port, NameSpace messageSpace)
    {
        super(port, messageSpace);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param port
     */
    public ServicesClientNIO(int port)
    {
        super(port);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param server
     * @param port
     * @param messageSpace
     * @param objectRegistry
     */
    public ServicesClientNIO(String server, int port, NameSpace messageSpace, ObjectRegistry objectRegistry)
    {
        super(server, port, messageSpace, objectRegistry);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param server
     * @param port
     * @param messageSpace
     */
    public ServicesClientNIO(String server, int port, NameSpace messageSpace)
    {
        super(server, port, messageSpace);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param server
     * @param port
     */
    public ServicesClientNIO(String server, int port)
    {
        super(server, port);
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#connect()
     */
    public boolean connect()
    {
        try
        {
            selector = Selector.open();
            channel = SocketChannel.open();
            
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

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#waitForConnect()
     */
    public void waitForConnect()
    {
        // TODO Auto-generated method stub
        super.waitForConnect();
    }

    /* (non-Javadoc)
     * @see ecologylab.services.ServicesClient#setServer(java.lang.String)
     */
    public void setServer(String server)
    {
        // TODO Auto-generated method stub
        super.setServer(server);
    }

    
}
