/*
 * Created on Mar 2, 2007
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.generic.Debug;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.exceptions.ClientOfflineException;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationSpace;
import ecologylab.xml.XmlTranslationException;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public abstract class NIONetworking extends Debug implements Runnable
{
    /**
     * 
     * @author James Greenfield
     * 
     */
    private class ChangeRequest
    {
        public static final int REGISTER               = 1;

        public static final int CHANGEOPS              = 2;

        public static final int INVALIDATE_PERMANENTLY = 3;

        public static final int INVALIDATE_TEMPORARILY = 4;

        public SocketChannel    socket;

        public int              type;

        public int              ops;

        public ChangeRequest(SocketChannel socket, int type, int ops)
        {
            this.socket = socket;
            this.type = type;
            this.ops = ops;
        }
    }

    private ByteBuffer                            readBuffer                = ByteBuffer
                                                                                    .allocate(1024*4);

    private Map<SocketChannel, Queue<ByteBuffer>> pendingWrites             = new HashMap<SocketChannel, Queue<ByteBuffer>>();

    private Queue<ChangeRequest>                  pendingSelectionOpChanges = new LinkedList<ChangeRequest>();

    private boolean                               running;

    private Thread                                thread;

    protected int                                 portNumber;

    protected boolean                             shuttingDown              = false;

    protected Selector                            selector;

    /**
     * Space that defines mappings between xml names, and Java class names, for
     * request messages.
     */
    protected TranslationSpace                    requestTranslationSpace;

    /**
     * Provides a context for request processing.
     */
    protected ObjectRegistry                      objectRegistry;

    protected int                                 connectionCount           = 0;

    /**
     * Creates a Services Server Base. Sets internal variables, but does not
     * bind the port. Port binding is to be handled by sublcasses.
     * 
     * @param portNumber
     * @param requestTranslationSpace
     * @param objectRegistry
     *            Provides a context for request processing.
     * @throws IOException
     */
    protected NIONetworking(int portNumber,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry) throws IOException,
            java.net.BindException
    {
        this.portNumber = portNumber;
        this.requestTranslationSpace = requestTranslationSpace;
        if (objectRegistry == null)
            objectRegistry = new ObjectRegistry();
        this.objectRegistry = objectRegistry;

    }

    /**
     * Get the message passing context associated with this server.
     * 
     * @return
     */
    public ObjectRegistry getObjectRegistry()
    {
        return objectRegistry;
    }

    /**
     * @return the portNumber
     */
    public int getPortNumber()
    {
        return portNumber;
    }

    /**
     * @return Returns the requestTranslationSpace.
     */
    public TranslationSpace getRequestTranslationSpace()
    {
        return requestTranslationSpace;
    }

    /**
     * Perform the service associated with a RequestMessage, by calling the
     * performService() method on that message.
     * 
     * @param requestMessage
     *            Message to perform.
     * @return Response to the message.
     */
    public ResponseMessage performService(RequestMessage requestMessage)
    {
        ResponseMessage temp = requestMessage.performService(objectRegistry);
        if (temp != null)
            temp.setUid(requestMessage.getUid());

        return temp;
    }

    public final void run()
    {
        while (running)
        {
            // handle any selection op changes
            synchronized (this.pendingSelectionOpChanges)
            {
                for (ChangeRequest changeReq : pendingSelectionOpChanges)
                {
                	debug("making selection op changes...");
                	
                    if (changeReq.socket.isRegistered())
                    {
                        /*
                         * Perform any changes to the interest ops on the keys,
                         * before selecting.
                         */
                        switch (changeReq.type)
                        {
                            case ChangeRequest.CHANGEOPS:
                                try
                                {
                                    changeReq.socket.keyFor(this.selector)
                                            .interestOps(changeReq.ops);
                                }
                                catch (CancelledKeyException e)
                                {
                                    debug("tried to change ops after key was cancelled.");
                                }
                                break;
                            case ChangeRequest.INVALIDATE_PERMANENTLY:
                                invalidateKey(changeReq.socket
                                        .keyFor(this.selector), true);
                                break;
                            case ChangeRequest.INVALIDATE_TEMPORARILY:
                                invalidateKey(changeReq.socket
                                        .keyFor(this.selector), false);
                                break;
                        }
                    }
                }

                this.pendingSelectionOpChanges.clear();
            }

            try
            {
                // block until some connection has something to do
                // if ((timeSelecting = selector.select(selectInterval)) > 0)
            	int numSel = 0;
            	
                if ((numSel = selector.select()) > 0)
                {
                	debug("selector woke up with "+numSel+" selected.");
                	
                    // get an iterator of the keys that have something to do
                    Iterator<SelectionKey> selectedKeyIter = selector
                            .selectedKeys().iterator();

                    while (selectedKeyIter.hasNext())
                    {
                        /*
                         * get the key corresponding to the event and process it
                         * appropriately, then remove it
                         */
                        SelectionKey key = (SelectionKey) selectedKeyIter
                                .next();

                        selectedKeyIter.remove();

                        if (!key.isValid())
                        {
                            setPendingInvalidate((SocketChannel) key.channel(),
                                    false);
                            continue;
                        }

                        if (key.isAcceptable())
                        { // incoming connection; accept
                            acceptKey(key);
                        }
                        else if (key.isWritable())
                        {
                        	debug("time to write!");
                        	
                            try
                            {
                                writeKey(key);
                                finishWrite(key);
                            }
                            catch (IOException e)
                            {
                                debug("IO error when attempting to write to socket; stack trace follows.");

                                e.printStackTrace();
                            }

                        }
                        else
                        {
                            if (key.isReadable())
                            { /*
                                 * incoming readable, valid key have to check
                                 * validity here, because accept key may have
                                 * rejected an incoming connection
                                 */
                            	debug("time to read!");
                            	
                                if (key.channel().isOpen() && key.isValid())
                                {
                                    try
                                    {
                                        readKey(key);
                                    }
                                    catch (ClientOfflineException e)
                                    {
                                        error(e.getMessage());
                                        setPendingInvalidate(
                                                (SocketChannel) key.channel(),
                                                false);
                                    }
                                    catch (BadClientException e)
                                    {
                                        // close down this evil connection!
                                        error(e.getMessage());
                                        this.removeBadConnections(key);
                                    }
                                }
                                else
                                {
                                    debug("Channel closed on "
                                            + key.attachment() + ", removing.");
                                    invalidateKey(key, false);
                                }
                            }
                        }
                    }
                    
                    debug("done with all selections.");
                }
            }
            catch (IOException e)
            {
                this.stop();

                debug("attempted to access selector after it was closed! shutting down");

                e.printStackTrace();
            }

            // remove any that were idle for too long
            this.checkAndDropIdleKeys();
        }

        this.close();
    }

    /**
	 * Perform any actions necessary after all data has been written from the
	 * outgoing queue to the client for this key. This is a hook method so that
	 * subclasses can provide specific functionality (such as, for example,
	 * invalidating the connection once the data has been sent.
	 * 
	 * This method does not do anything.
	 * 
	 * @param key -
	 *            the SelectionKey that is finished writing.
	 */
    protected void finishWrite(SelectionKey key) 
    {
    	
	}

	/**
     * Queue up bytes to send.
     * 
     * @param socket
     * @param data
     */
    public void send(SocketChannel socket, ByteBuffer data)
    {
    	debug("Entering send.");
    	
        synchronized (this.pendingSelectionOpChanges)
        {
            // queue change
            this.pendingSelectionOpChanges.offer(new ChangeRequest(socket,
                    ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));

            // queue data to write
            synchronized (this.pendingWrites)
            {
                Queue<ByteBuffer> dataQueue = pendingWrites.get(socket);

                if (dataQueue == null)
                {
                    dataQueue = new LinkedList<ByteBuffer>();
                    pendingWrites.put(socket, dataQueue);
                }

                dataQueue.offer(data);
            }
        }

        debug("wakeup selector");
        
        selector.wakeup();
    }

    /**
     * Sets up a pending invalidate command for the given input.
     * 
     * @param chan -
     *            the SocketChannel to invalidate.
     */
    public void setPendingInvalidate(SocketChannel chan, boolean permanent)
    {
        if (permanent)
        {
            synchronized (pendingSelectionOpChanges)
            {
                this.pendingSelectionOpChanges.offer(new ChangeRequest(chan,
                        ChangeRequest.INVALIDATE_PERMANENTLY, 0));
            }
        }
        else
        {
        	synchronized(pendingSelectionOpChanges)
        	{
        		this.pendingSelectionOpChanges.offer(new ChangeRequest(chan,
        				ChangeRequest.INVALIDATE_TEMPORARILY, 0));
        	}
        }
        selector.wakeup();
    }

    public void start()
    {
        // start the server running
        running = true;

        if (thread == null)
        {
            thread = new Thread(this, "NIO Server running on port "
                    + portNumber);
        }

        thread.start();
    }

    public synchronized void stop()
    {
        running = false;

        this.close();
    }

    public RequestMessage translateXMLStringToRequestMessage(
            String messageString, boolean doRecursiveDescent)
            throws XmlTranslationException
    {
        RequestMessage requestMessage = (RequestMessage) ElementState
                .translateFromXMLString(messageString, requestTranslationSpace,
                        doRecursiveDescent);
        return requestMessage;
    }

    /**
     * Reads all the data from the key into the readBuffer, then pushes that
     * information to the action processor for processing.
     * 
     * @param key
     * @throws BadClientException
     */
    private final void readKey(SelectionKey key) throws BadClientException,
            ClientOfflineException
    {
        SocketChannel sc = (SocketChannel) key.channel();
        int bytesRead;

        this.readBuffer.clear();

        // read
        try
        {
            bytesRead = sc.read(readBuffer);
        }
        catch (BufferOverflowException e)
        {
            throw new BadClientException(sc.socket().getInetAddress()
                    .getHostAddress(), "Client overflowed the buffer.");
        }
        catch (IOException e)
        { // error trying to read; client disconnected
            throw new ClientOfflineException(
                    "Client forcibly closed connection.");
        }

        if (bytesRead == -1)
        { // connection closed cleanly
            throw new ClientOfflineException(
                    "Client closed connection cleanly.");
        }
        else if (bytesRead > 0)
        {
            byte[] bytes = new byte[bytesRead];

            readBuffer.flip();
            readBuffer.get(bytes);

            this.processReadData(key.attachment(), sc, bytes, bytesRead);
        }
    }

    /**
     * Writes the bytes from pendingWrites that belong to key.
     * 
     * @param key
     * @throws IOException
     */
    private final void writeKey(SelectionKey key) throws IOException
    {
    	debug("writing.");
        SocketChannel sc = (SocketChannel) key.channel();

        synchronized (this.pendingWrites)
        {
            Queue<ByteBuffer> writes = pendingWrites.get(sc);

            while (!writes.isEmpty())
            { // write everything
                ByteBuffer bytes = writes.poll();

                sc.write(bytes);

                if (bytes.remaining() > 0)
                { // the socket's buffer filled
                    // up! OH NOES!
                    break;
                }
            }

            	// nothing left to write, go back to
                // listening
                key.interestOps(SelectionKey.OP_READ);

                selector.wakeup();
        }
    }

    protected abstract void acceptKey(SelectionKey key);

    protected abstract void checkAndDropIdleKeys();

    protected void close()
    {
    }

    /**
     * Remove the argument passed in from the set of connections we know about.
     */
    protected void connectionTerminated()
    {
        connectionCount--;
        // When thread close by unexpected way (such as client just crashes),
        // this method will end the service gracefully.
        terminationAction();
    }

    protected abstract void invalidateKey(SelectionKey key, boolean permanent);

    /**
     * Shut down the connection associated with this SelectionKey. Subclasses
     * should override to do your own housekeeping, then call
     * super.invalidateKey(SelectionKey) to utilize the functionality here.
     * 
     * @param chan
     *            The SocketChannel that needs to be shut down.
     */
    protected void invalidateKey(SocketChannel chan, boolean permanent)
    {
        try
        {
            chan.close();
        }
        catch (IOException e)
        {
            debug(e.getMessage());
        }
        catch (NullPointerException e)
        {
            debug(e.getMessage());
        }

        if (chan.keyFor(selector) != null)
        { // it's possible that they key
            // was somehow disposed of
            // already,
            // perhaps it was already invalidated once
            chan.keyFor(selector).cancel();
        }
    }

    protected abstract void processReadData(Object sessionId, SocketChannel sc, byte[] bytes, int bytesRead) throws BadClientException;

    protected abstract void removeBadConnections(SelectionKey key);

    /**
     * This defines the actions that server needs to perform when the client
     * ends unexpected way. Detail implementations will be in subclasses.
     */
    protected void terminationAction()
    {

    }
}
