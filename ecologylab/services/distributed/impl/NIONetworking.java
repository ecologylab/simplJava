/*
 * Created on Mar 2, 2007
 */
package ecologylab.services.distributed.impl;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.naming.OperationNotSupportedException;

import ecologylab.appframework.ObjectRegistry;
import ecologylab.io.ByteBufferPool;
import ecologylab.services.exceptions.BadClientException;
import ecologylab.services.exceptions.ClientOfflineException;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.TranslationSpace;

/**
 * Handles backend, low-level communication between distributed programs, using NIO. This is the basis for servers for
 * handling network communication.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public abstract class NIONetworking extends NIOCore
{
	/** ByteBuffer that holds all incoming communication temporarily, immediately after it is read. */
	private ByteBuffer										readBuffer			= ByteBuffer.allocateDirect(MAX_PACKET_SIZE_BYTES);

	/**
	 * Maps SocketChannels (connections) to their write Queues of ByteBuffers. Whenever a SocketChannel is marked for
	 * writing, and comes up for writing, the server will write the set of ByteBuffers to the socket.
	 */
	private Map<SocketChannel, Queue<ByteBuffer>>	pendingWrites		= new HashMap<SocketChannel, Queue<ByteBuffer>>();

	protected boolean											shuttingDown		= false;

	/** Space that defines mappings between xml names, and Java class names, for request messages. */
	protected TranslationSpace								translationSpace;

	/** Provides a context for request processing. */
	protected ObjectRegistry<?>							objectRegistry;

	protected int												connectionCount	= 0;

	protected ByteBufferPool								byteBufferPool;

	/**
	 * Creates a Services Server Base. Sets internal variables, but does not bind the port. Port binding is to be handled
	 * by sublcasses.
	 * 
	 * @param portNumber
	 *           the port number to use for communicating.
	 * @param translationSpace
	 *           the TranslationSpace to use for incoming messages; if this is null, uses DefaultServicesTranslations
	 *           instead.
	 * @param objectRegistry
	 *           Provides a context for request processing; if this is null, creates a new ObjectRegistry.
	 * @throws IOException
	 *            if an I/O error occurs while trying to open a Selector from the system.
	 */
	protected NIONetworking(String networkIdentifier, int portNumber, TranslationSpace translationSpace,
			ObjectRegistry<?> objectRegistry) throws IOException
	{
		super(networkIdentifier, portNumber);

		if (translationSpace == null)
			translationSpace = DefaultServicesTranslations.get();

		this.translationSpace = translationSpace;

		if (objectRegistry == null)
			objectRegistry = new ObjectRegistry<Object>();

		this.objectRegistry = objectRegistry;

		this.byteBufferPool = new ByteBufferPool(10, 10, MAX_PACKET_SIZE_BYTES);
	}

	/**
	 * Perform the service associated with a RequestMessage, by calling the performService() method on that message.
	 * 
	 * @param requestMessage
	 *           Message to perform.
	 * @return Response to the message.
	 */
	protected ResponseMessage performService(RequestMessage requestMessage)
	{
		ResponseMessage temp = requestMessage.performService(objectRegistry);

		if (temp != null)
			temp.setUid(requestMessage.getUid());

		return temp;
	}

	/**
	 * @see ecologylab.services.distributed.impl.NIOCore#readReady(java.nio.channels.SelectionKey)
	 */
	@Override protected void readReady(SelectionKey key) throws ClientOfflineException, BadClientException
	{
		readKey(key);
	}

	/**
	 * @see ecologylab.services.distributed.impl.NIOCore#writeReady(java.nio.channels.SelectionKey)
	 */
	@Override protected void writeReady(SelectionKey key) throws IOException
	{
		writeKey(key);
	}

	/**
	 * Queue up bytes to send on a particular socket. This method is typically called by some outside context manager,
	 * that has produced an encoded message to send out.
	 * 
	 * @param socket
	 * @param data
	 */
	public void enqueueBytesForWriting(SocketChannel socket, ByteBuffer data)
	{
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

		this.queueForWrite(socket.keyFor(selector));

		selector.wakeup();
	}

	/**
	 * Reads all the data from the key into the readBuffer, then pushes that information to the action processor for
	 * processing.
	 * 
	 * @param key
	 * @throws BadClientException
	 */
	private final void readKey(SelectionKey key) throws BadClientException, ClientOfflineException
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
			throw new BadClientException(sc.socket().getInetAddress().getHostAddress(), "Client overflowed the buffer.");
		}
		catch (IOException e)
		{ // error trying to read; client disconnected
			throw new ClientOfflineException("Client forcibly closed connection.");
		}

		if (bytesRead == -1)
		{ // connection closed cleanly
			throw new ClientOfflineException("Client closed connection cleanly.");
		}
		else if (bytesRead > 0)
		{
			readBuffer.flip();

			this.processReadData(key.attachment(), sc, readBuffer, bytesRead);
		}
	}

	/**
	 * Writes the bytes from pendingWrites that belong to key.
	 * 
	 * @param key
	 * @throws IOException
	 */
	protected void writeKey(SelectionKey key) throws IOException
	{
		SocketChannel sc = (SocketChannel) key.channel();

		synchronized (this.pendingWrites)
		{
			Queue<ByteBuffer> writes = pendingWrites.get(sc);

			while (!writes.isEmpty())
			{ // write everything
				ByteBuffer bytes = writes.poll();

				bytes.flip();
				sc.write(bytes);

				if (bytes.remaining() > 0)
				{ // the socket's buffer filled up!; should go out again next time
					writes.offer(bytes);
					break;
				}
				else
				{
					bytes = this.byteBufferPool.release(bytes);
				}
			}
		}
	}

	/**
	 * Optional operation.
	 * 
	 * Called when a key has been marked for accepting. This method should be implemented by servers, but clients should
	 * leave this blank, unless they are also acting as servers (accepting incoming connections).
	 * 
	 * @param key
	 * @throws OperationNotSupportedException
	 */
	protected abstract void acceptKey(SelectionKey key);

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

	/**
	 * This method is called whenever bytes have been read from a socket. There is no guaranty that the bytes will be a
	 * valid or complete message, nor is there a guaranty about what said bytes encode. Implementations should be
	 * prepared to handle incomplete messages, multiple messages, or malformed messages in this method.
	 * 
	 * @param sessionId
	 *           the id being use for this session.
	 * @param sc
	 *           the SocketChannel from which the bytes originated.
	 * @param bytes
	 *           the bytes read from the SocketChannel.
	 * @param bytesRead
	 *           the number of bytes in the bytes array.
	 * @throws BadClientException
	 *            if the client from which the bytes were read has transmitted something inappropriate, such as data too
	 *            large for a buffer or a possibly malicious message.
	 */
	protected abstract void processReadData(Object sessionId, SocketChannel sc, ByteBuffer bytes, int bytesRead)
			throws BadClientException;

	/**
	 * This defines the actions that server needs to perform when the client ends unexpected way. Detail implementations
	 * will be in subclasses.
	 */
	protected void terminationAction()
	{

	}

	/**
	 * Retrieves a ByteBuffer object from this's pool of ByteBuffers. Typically used by a ContextManager to store bytes
	 * that will be later enqueued to write (and thus released by that method).
	 * 
	 * @return
	 */
	public ByteBuffer acquireByteBufferFromPool()
	{
		return this.byteBufferPool.acquire();
	}
}
