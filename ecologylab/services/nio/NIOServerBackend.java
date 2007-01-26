/*
 * Created on May 3, 2006
 */
package ecologylab.services.nio;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.services.BadClientException;
import ecologylab.services.ServerConstants;
import ecologylab.services.ServerEvent;
import ecologylab.services.ServicesServerBase;
import ecologylab.services.exceptions.ClientOfflineException;
import ecologylab.services.nio.servers.NIOServerFrontend;
import ecologylab.xml.TranslationSpace;

/**
 * The NIO Server. Uses a ServerActionProcessor instance to handle the
 * processing of all requests.
 * 
 * Re-written based on the Rox Java NIO Tutorial
 * (http://rox-xmlrpc.sourceforge.net/niotut/index.html).
 * 
 * @author Zach Toups
 * 
 */
public class NIOServerBackend extends ServicesServerBase implements
        ServerConstants
{
    /**
     * 
     * @author James Greenfield
     * 
     */
    private class ChangeRequest
    {
        public static final int REGISTER  = 1;

        public static final int CHANGEOPS = 2;

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

    public static NIOServerBackend getInstance(int portNumber,
            InetAddress inetAddress, NIOServerFrontend sAP,
            TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleSocketTimeout)
            throws IOException, BindException
    {
        return new NIOServerBackend(portNumber, inetAddress, sAP,
                requestTranslationSpace, objectRegistry, idleSocketTimeout);
    }

    protected Selector                            selector;

    private boolean                               running;

    private long                                  interval                  = 0;

    private long                                  selectInterval            = 0;

    private Thread                                thread;

    private int                                   numConnections;

    private NIOServerFrontend                     sAP;

    private ByteBuffer                            readBuffer                = ByteBuffer
                                                                                    .allocate(1024);

    private Map<SocketChannel, Queue<ByteBuffer>> pendingWrites             = new HashMap<SocketChannel, Queue<ByteBuffer>>();

    private Queue<ChangeRequest>                  pendingSelectionOpChanges = new LinkedList<ChangeRequest>();

    private InetAddress                           hostAddress;

    private int                                   idleSocketTimeout;

    private Map<SelectionKey, Long>               keyActivityTimes          = new HashMap<SelectionKey, Long>();

    private Map<String, Object>                   ipToKeyOrKeys             = new HashMap<String, Object>();

    protected NIOServerBackend(int portNumber, InetAddress inetAddress,
            NIOServerFrontend sAP, TranslationSpace requestTranslationSpace,
            ObjectRegistry objectRegistry, int idleSocketTimeout)
            throws IOException, BindException
    {
        super(portNumber, requestTranslationSpace, objectRegistry);

        this.hostAddress = inetAddress;

        this.sAP = sAP;

        this.selector = initSelector();

        this.idleSocketTimeout = idleSocketTimeout;
    }

    public InetAddress getHostAddress()
    {
        return hostAddress;
    }

    /**
     * Shut down the connection associated with this SelectionKey. Subclasses
     * should override to do your own housekeeping, then call
     * super.invalidateKey(SelectionKey) to utilize the functionality here.
     * 
     * @param key
     *            The SelectionKey that needs to be shut down.
     */
    public void invalidate(SocketChannel chan)
    {
        try
        {
            chan.close();
            Object keyOrKeys = this.ipToKeyOrKeys.get(chan.socket()
                    .getInetAddress().getHostAddress());

            if (keyOrKeys instanceof HashMap)
            {
                ((HashMap) keyOrKeys).remove(chan.socket().getInetAddress());
            }
            else
            { // just the one
                ipToKeyOrKeys.remove(chan.socket().getInetAddress()
                        .getHostAddress());
            }
        }
        catch (IOException e)
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

        // decrement numConnections &
        // if the server disabled new connections due to hitting
        // max_connections, re-enable
        if (numConnections-- == MAX_CONNECTIONS)
        {
            // acquire the static ServerSocketChannel object
            ServerSocketChannel channel;
            try
            {
                channel = ServerSocketChannel.open();

                // disable blocking
                channel.configureBlocking(false);

                // get the socket associated with the channel
                serverSocket = channel.socket();
                serverSocket.setReuseAddress(true);

                // bind to the port for this server
                serverSocket
                        .bind(new InetSocketAddress(hostAddress, portNumber));
                serverSocket.setReuseAddress(true);

                // register the channel with the selector to look for incoming
                // accept requests
                channel.register(selector, SelectionKey.OP_ACCEPT);
            }
            catch (IOException e)
            {
                debug("Unable to re-open socket for accepts; critical failure.");
                e.printStackTrace();
                System.exit(-1);
            }

        }
    }

    public void run()
    {
        long time = System.currentTimeMillis();
        int timeSelecting;

        while (running)
        {
            // if there is an interval set, get off the processor for awhile
            if (interval != 0)
            {
                if ((time = (System.currentTimeMillis() - time)) < interval)
                {
                    try
                    {
                        synchronized (thread)
                        {
                            thread.wait(interval - time);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            time = System.currentTimeMillis();

            // handle any selection op changes
            synchronized (this.pendingSelectionOpChanges)
            {
                for (ChangeRequest req : pendingSelectionOpChanges)
                {
                    switch (req.type)
                    {
                    case ChangeRequest.CHANGEOPS:
                        try
                        {
                            req.socket.keyFor(this.selector).interestOps(
                                    req.ops);
                        }
                        catch (CancelledKeyException e)
                        {
                            debug("tried to change ops after key was cancelled.");
                        }
                        break;
                    }
                }

                this.pendingSelectionOpChanges.clear();
            }

            try
            {
                // block until some connection has something to do
                if ((timeSelecting = selector.select(selectInterval)) > 0)
                {
                    // get an iterator of the keys that have something to do
                    Iterator<SelectionKey> selectedKeyIter = selector
                            .selectedKeys().iterator();

                    while (selectedKeyIter.hasNext())
                    {
                        // get the key corresponding to the event and process it
                        // appropriately
                        SelectionKey key = (SelectionKey) selectedKeyIter
                                .next();

                        selectedKeyIter.remove();

                        // see if the connection has been idle too long
                        if (idleSocketTimeout > -1
                                && this.keyActivityTimes.containsKey(key)
                                && System.currentTimeMillis()
                                        - this.keyActivityTimes.get(key) > idleSocketTimeout)
                        {
                            invalidate((SocketChannel) key.channel());
                        }

                        if (!key.isValid())
                        {
                            invalidate((SocketChannel) key.channel());
                            continue;
                        }

                        if (key.isAcceptable())
                        { // incoming connection;
                            // accept if not already
                            // full
                            accept(key);
                        }
                        else
                            if (key.isWritable())
                            {
                                try
                                {
                                    write(key);
                                }
                                catch (IOException e)
                                {
                                    debug("IO error when attempting to write to socket; stack trace follows.");

                                    e.printStackTrace();
                                }

                            }
                            else
                                if (key.isReadable())
                                { // incoming readable,
                                    // valid key
                                    // have to check validity here, because
                                    // accept
                                    // key may have rejected an incoming
                                    // connection
                                    if (key.channel().isOpen())
                                    {
                                        try
                                        {
                                            read(key);
                                        }
                                        catch (ClientOfflineException e)
                                        {
                                            error(e.getMessage());
                                            invalidate((SocketChannel) key
                                                    .channel());
                                        }
                                        catch (BadClientException e)
                                        {
                                            // close down this evil connection!
                                            error(e.getMessage());

                                            // shut them ALL down!
                                            Object keyOrKeys = ipToKeyOrKeys
                                                    .get(((SocketChannel) key
                                                            .channel())
                                                            .socket()
                                                            .getInetAddress()
                                                            .getHostAddress());

                                            if (keyOrKeys instanceof HashMap)
                                            {
                                                Iterator<SelectionKey> allKeysForIp = ((HashMap<InetAddress, SelectionKey>) keyOrKeys)
                                                        .values().iterator();

                                                while (allKeysForIp.hasNext())
                                                {
                                                    SelectionKey keyForIp = allKeysForIp
                                                            .next();
                                                    
                                                    System.out.println("class: "+keyForIp
                                                                    .channel().getClass());
                                                    
                                                    this
                                                            .invalidate((SocketChannel) keyForIp
                                                                    .channel());
                                                }
                                            }
                                            else
                                            {
                                                invalidate((SocketChannel) key
                                                        .channel());
                                            }
                                        }
                                    }
                                    else
                                    {
                                        debug("Channel closed on "
                                                + key.attachment()
                                                + ", removing.");
                                        invalidate((SocketChannel) key
                                                .channel());
                                    }
                                }
                    }
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
     * Queue up bytes to send.
     * 
     * @param socket
     * @param data
     */
    public void send(SocketChannel socket, ByteBuffer data)
    {
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
    }

    public void setInterval(long newInterval)
    {
        interval = newInterval;

        try
        {
            selector.selectNow();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setSelectInterval(long newInterval)
    {
        selectInterval = newInterval;

        try
        {
            selector.selectNow();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see ecologylab.services.Shutdownable#shutdown()
     */
    public void shutdown()
    {
        // TODO Auto-generated method stub

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

        this.fireServerEvent(ServerEvent.SERVER_STARTED);
    }

    public synchronized void stop()
    {
        running = false;

        this.close();

        this.fireServerEvent(ServerEvent.SERVER_SHUTTING_DOWN);
    }

    /**
     * Checks all of the current keys to see if they have been idle for too long
     * and drops them if they have.
     * 
     */
    private void checkAndDropIdleKeys()
    {
        if (idleSocketTimeout > -1)
        { // after we select, we'll check to see
            // if we need to boot any idle
            // keys
            LinkedList<SelectionKey> keysToInvalidate = new LinkedList<SelectionKey>();
            long timeStamp = System.currentTimeMillis();

            for (SelectionKey sKey : keyActivityTimes.keySet())
            {
                if (timeStamp - keyActivityTimes.get(sKey).longValue() > idleSocketTimeout)
                {
                    keysToInvalidate.add(sKey);
                }
            }

            // remove all the invalid keys
            for (SelectionKey keyToInvalidate : keysToInvalidate)
            {
                debug(keyToInvalidate.attachment()
                        + " took too long to request; disconnecting.");
                keyActivityTimes.remove(keyToInvalidate);
                this.invalidate((SocketChannel) keyToInvalidate.channel());
            }
        }
    }

    protected final SocketChannel accept(SelectionKey key)
    {
        try
        {
            int numConn = selector.keys().size() - 1;

            debug("connections running: " + numConn);

            if ((numConn < MAX_CONNECTIONS)
                    || (BadClientException
                            .isEvilHostByNumber(((ServerSocketChannel) key
                                    .channel()).socket().getInetAddress()
                                    .getHostAddress())))
            { // the keyset
                // includes this
                // side of the
                // connection

                numConnections++;

                if (numConnections == MAX_CONNECTIONS)
                {
                    debug("Maximum connections reached; disabling accept until a client drops.");

                    key.cancel();
                    ((ServerSocketChannel) key.channel()).socket().close();
                    key.channel().close();
                }

                SocketChannel tempChannel = ((ServerSocketChannel) key
                        .channel()).accept();

                tempChannel.configureBlocking(false);

                // when we register, we want to attach the proper
                // session token to all of the keys associated with
                // this connection, so we can sort them out later.
                SelectionKey newKey = tempChannel.register(selector,
                        SelectionKey.OP_READ, this
                                .generateSessionToken(tempChannel.socket()));

                this.keyActivityTimes.put(newKey, new Long(System
                        .currentTimeMillis()));

                String address = ((SocketChannel) newKey.channel()).socket()
                        .getInetAddress().getHostAddress();
                Object keyOrKeys;
                if ((keyOrKeys = ipToKeyOrKeys.get(address)) == null)
                {
                    ipToKeyOrKeys.put(address, newKey);
                }
                else
                {
                    synchronized (ipToKeyOrKeys)
                    {
                        if (keyOrKeys instanceof HashMap)
                        {
                            ((HashMap<InetAddress, SelectionKey>) keyOrKeys)
                                    .put(((SocketChannel) newKey.channel())
                                            .socket().getInetAddress(), newKey);
                        }
                        else
                        {
                            HashMap<InetAddress, SelectionKey> keys = new HashMap<InetAddress, SelectionKey>();
                            keys.put(((SocketChannel) newKey.channel())
                                    .socket().getInetAddress(), newKey);
                            keys
                                    .put(
                                            ((SocketChannel) ((SelectionKey) keyOrKeys)
                                                    .channel()).socket()
                                                    .getInetAddress(),
                                            (SelectionKey) keyOrKeys);

                            ipToKeyOrKeys.put(address, keys);
                        }
                    }
                }

                debug("Now connected to " + tempChannel + ", "
                        + (MAX_CONNECTIONS - numConn - 1)
                        + " connections remaining.");

                return tempChannel;
            }
            else
            {
                SocketChannel tempChannel = ((ServerSocketChannel) key
                        .channel()).accept();
                tempChannel.socket().shutdownInput();
                tempChannel.socket().shutdownOutput();
                tempChannel.socket().close();
                tempChannel.close();

                if (numConn < MAX_CONNECTIONS)
                    debug("Rejected connection; already fulfilled max connections.");
                else
                    debug("Evil host attempted to connect: "
                            + ((ServerSocketChannel) key.channel()).socket()
                                    .getInetAddress());

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    protected void close()
    {
        try
        {
            debug("Closing selector.");
            selector.close();

            debug("Unbinding.");
            this.serverSocket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sets up a Selector.
     * 
     * @return
     * @throws IOException
     */
    private Selector initSelector() throws IOException
    {
        // acquire the static Selector object
        Selector sSelector = SelectorProvider.provider().openSelector();

        // acquire the static ServerSocketChannel object
        ServerSocketChannel channel = ServerSocketChannel.open();

        // disable blocking
        channel.configureBlocking(false);

        // get the socket associated with the channel
        serverSocket = channel.socket();
        serverSocket.setReuseAddress(true);

        // bind to the port for this server
        serverSocket.bind(new InetSocketAddress(hostAddress, portNumber));
        serverSocket.setReuseAddress(true);

        // register the channel with the selector to look for incoming
        // accept requests
        channel.register(sSelector, SelectionKey.OP_ACCEPT);

        return sSelector;
    }

    /**
     * Reads all the data from the key into the readBuffer, then pushes that
     * information to the action processor for processing.
     * 
     * @param key
     * @throws BadClientException
     */
    private void read(SelectionKey key) throws BadClientException,
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

        if (bytesRead > 0)
        {
            byte[] bytes = new byte[bytesRead];

            readBuffer.flip();
            readBuffer.get(bytes);

            this.sAP.process(key.attachment(), this, sc, bytes, bytesRead);
        }

        this.keyActivityTimes.put(key, new Long(System.currentTimeMillis()));
    }

    private void write(SelectionKey key) throws IOException
    {
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

            if (writes.isEmpty())
            { // nothing left to write, go back to
                // listening
                key.interestOps(SelectionKey.OP_READ);
            }
        }
    }
}
