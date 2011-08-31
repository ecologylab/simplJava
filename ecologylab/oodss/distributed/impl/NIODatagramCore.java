package ecologylab.oodss.distributed.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import ecologylab.collections.Scope;
import ecologylab.generic.CappedResourcePool;
import ecologylab.generic.Debug;
import ecologylab.generic.ResourcePool;
import ecologylab.oodss.distributed.common.NetworkingConstants;
import ecologylab.oodss.distributed.exception.MessageTooLargeException;
import ecologylab.oodss.messages.ServiceMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationScope;

/**
 * 
 * @author bilhamil
 * 
 *         Core class for datagram functionality. Handles sending and receiving messages. Is based
 *         on several threads: sending and receiving threads that handle the socket and a pool of
 *         threads that process requests in a parallel fashion.
 * 
 * @param <S>
 *          Scope parameterization
 */
public abstract class NIODatagramCore<S extends Scope> extends Debug implements NetworkingConstants
{
	protected long																																		currentUIDIndex				= 1;

	private MessageWithMetadataPool<ServiceMessage<S>, MessageMetaData>								messagePool						= new MessageWithMetadataPool<ServiceMessage<S>, MessageMetaData>(4,
																																																																																						4);

	private MessageMetaDataPool																												metaDataPool					= new MessageMetaDataPool();

	protected static final int																												MAX_DATAGRAM_SIZE			= 10000;

	protected static final int																												HEADER_SIZE						= Long.SIZE / 8;

	protected static final int																												UDP_HEADER_SIZE				= 8;

	protected static final int																												MAX_MESSAGE_SIZE			= MAX_DATAGRAM_SIZE
																																																							- HEADER_SIZE
																																																							- UDP_HEADER_SIZE;

	private SynchronousQueue<MessageWithMetadata<ServiceMessage<S>, MessageMetaData>>	outgoingMessageQueue	= new SynchronousQueue<MessageWithMetadata<ServiceMessage<S>, MessageMetaData>>();

	protected Selector																																selector;

	protected TranslationScope																												translationScope;

	protected S																																				objectRegistry;

	private PacketHandlerPool																													handlerPool						= new PacketHandlerPool();

	protected PacketSender																														sender								= new PacketSender();

	protected PacketReciever																													reciever							= new PacketReciever();

	protected boolean																																	doCompress						= false;

	protected Deflater																																deflater							= new Deflater(Deflater.BEST_COMPRESSION);

	protected Inflater																																inflater							= new Inflater();

	private CharsetDecoder																														decoder								= CHARSET.newDecoder();

	private CharsetEncoder																														encoder								= CHARSET.newEncoder();

	/**
	 * Base constructor. Opens the socket and sets up the state objects.
	 * 
	 * @param translationScope
	 * @param objectRegistry
	 * @param useCompression
	 */
	public NIODatagramCore(TranslationScope translationScope, S objectRegistry, boolean useCompression)
	{
		this.translationScope = translationScope;
		this.objectRegistry = objectRegistry;
		this.doCompress = useCompression;

		try
		{
			selector = Selector.open();
		}
		catch (IOException e)
		{
			debug("Failed to open selector!");
			e.printStackTrace();
		}
	}

	public NIODatagramCore(TranslationScope translationScope, S objectRegistry)
	{
		this(translationScope, objectRegistry, false);
	}

	/**
	 * Resource pool of MessageMetaData objects for messages in queue.
	 * 
	 * @author bilhamil
	 * 
	 */
	protected class MessageMetaDataPool extends ResourcePool<MessageMetaData>
	{
		public MessageMetaDataPool()
		{
			super(true, 4, 4, true);
		}

		@Override
		protected void clean(MessageMetaData objectToClean)
		{
			objectToClean.key = null;
			objectToClean.addr = null;
		}

		@Override
		protected MessageMetaData generateNewResource()
		{
			return new MessageMetaData();
		}
	}

	protected class MessageMetaData
	{
		public SelectionKey				key;

		public InetSocketAddress	addr;

	}

	/**
	 * Pool of threads for handling the call backs of request messages. Benefit of being able to have
	 * more computationally intensive callbacks without holding up the system.
	 * 
	 * @author bilhamil
	 * 
	 */
	protected class PacketHandlerPool extends CappedResourcePool<PacketHandler>
	{
		private boolean	shuttingDown	= false;

		public PacketHandlerPool()
		{
			super(true, 1, 1, 32, true);
		}

		/**
		 * Causes this pool to shut down by removing all its threads (causing them to stop()). Any
		 * further releases of PacketHandlers to this will result in them stop()'ing.
		 */
		@Override
		public synchronized void shutdown()
		{
			this.shuttingDown = true;
			super.shutdown();
		}

		@Override
		protected void clean(PacketHandler objectToClean)
		{
			objectToClean.reset();
		}

		@Override
		protected PacketHandler generateNewResource()
		{
			PacketHandler handler = new PacketHandler(this);
			Thread newThread = new Thread(handler, "Packet Handler");
			newThread.start();

			return handler;
		}

		@Override
		protected void onRemoval(PacketHandler handler)
		{
			handler.stop();
		}

		/**
		 * @see ecologylab.generic.CappedResourcePool#onRelease(java.lang.Object)
		 */
		@Override
		protected synchronized void onRelease(PacketHandler resourceToRelease)
		{
			super.onRelease(resourceToRelease);

			if (shuttingDown)
			{
				this.onRemoval(resourceToRelease);
			}
		}
	}

	/**
	 * Packet handling class. Is basically a handle for a thread that processes message handling.
	 * 
	 * @author bilhamil
	 * 
	 */
	protected class PacketHandler implements Runnable
	{
		private PacketHandlerPool	pool;

		private long							uid;

		private ServiceMessage<S>	message						= null;

		private SelectionKey			recievedOnSocket	= null;

		private InetSocketAddress	recievedFrom			= null;

		private boolean						done							= false;

		private Thread						t									= null;

		public PacketHandler(PacketHandlerPool pool)
		{
			this.pool = pool;
		}

		synchronized public void reset()
		{
			uid = 0;
			message = null;
			recievedOnSocket = null;
			recievedFrom = null;
		}

		/**
		 * Asynchronously stop the PacketHandler.
		 */
		synchronized public void stop()
		{
			done = true;
			if (t != null)
				t.interrupt();
		}

		synchronized public void run()
		{
			t = Thread.currentThread();
			while (!done)
			{
				while (message == null && !done)
				{
					try
					{
						wait();
					}
					catch (InterruptedException e)
					{
					}
				}
				if (message != null)
				{
					try
					{
						handleMessage(uid, message, recievedOnSocket, recievedFrom);
					}
					catch (Exception e)
					{
						debug("Failed in message handler: " + e.getMessage());
						e.printStackTrace();
					}
					finally
					{
						message = null;
						pool.release(this);
					}
				}
			}
		}

		synchronized public void processMessage(long uid, ServiceMessage<S> message, SelectionKey key,
				InetSocketAddress address)
		{
			this.uid = uid;
			this.message = message;
			this.recievedOnSocket = key;
			this.recievedFrom = address;
			notify();
		}
	}

	/**
	 * Packet Sending Class. Pulls messages that are queued to be sent out, serializes them, and puts
	 * them on the socket.
	 * 
	 * @author bilhamil
	 * 
	 */
	protected class PacketSender implements Runnable
	{
		private boolean	running	= false;

		private Thread	t;

		synchronized public void start()
		{
			if (!running)
			{
				running = true;
				t = new Thread(this);
				t.setName("Packet Sender Thread");
				t.start();
			}
		}

		synchronized public void stop()
		{
			if (running)
			{
				running = false;
				while (t.isAlive())
				{
					t.interrupt();
					try
					{
						t.join();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public boolean isAlive()
		{
			return t != null && t.isAlive();
		}

		public void run()
		{
			ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE * (doCompress ? 10 : 1));
			CharBuffer builder = CharBuffer.allocate(MAX_MESSAGE_SIZE * (doCompress ? 10 : 1));
			byte[] inBuffer = new byte[MAX_MESSAGE_SIZE * (doCompress ? 10 : 1)];
			byte[] outBuffer = new byte[MAX_MESSAGE_SIZE * (doCompress ? 10 : 1)];

			if (buffer.hasArray())
				debug("Buffer has array!");

			while (running)
			{
				MessageWithMetadata<ServiceMessage<S>, MessageMetaData> mdataMessage = null;
				try
				{
					// debug("1");
					mdataMessage = outgoingMessageQueue.take();

					buffer.putLong(mdataMessage.getUid());

					mdataMessage.getMessage().serialize(builder);

					builder.flip();

					// debug("2");
					encoder.encode(builder, buffer, true);
					buffer.flip();

					if (doCompress)
					{
						// debug("3");
						/* Compress message */
						byte[] array = inBuffer;
						buffer.get(inBuffer, 0, buffer.limit());

						deflater.reset();

						int uncompressedSize = buffer.limit();
						deflater.setInput(array, 0, uncompressedSize);
						deflater.finish();

						int compressedSize;
						if (buffer.hasArray())
						{
							compressedSize = deflater.deflate(buffer.array(), 0, buffer.capacity());
							buffer.position(0);
							buffer.limit(compressedSize);
						}
						else
						{
							// debug("4");
							compressedSize = deflater.deflate(outBuffer, 0, outBuffer.length);
							buffer.clear();
							buffer.put(outBuffer, 0, compressedSize);
							buffer.flip();
						}
						if (compressedSize > MAX_MESSAGE_SIZE)
						{
							throw new MessageTooLargeException(MAX_MESSAGE_SIZE, compressedSize);
						}
						/* debug("Input size: " + uncompressedSize + " and output size: " + compressedSize); */
					}

					// debug("5");
					DatagramChannel channel = (DatagramChannel) mdataMessage.getAttachment().key.channel();
					if (channel.isConnected())
					{
						channel.write(buffer);
					}
					else
					{
						channel.send(buffer, mdataMessage.getAttachment().addr);
					}
				}
				catch (InterruptedException e)
				{
					debug("Stopping Packet Sender!");
				}
				catch (PortUnreachableException e)
				{
					debug("Failed to send datagram, port unreachable!");
				}
				catch (NoRouteToHostException e)
				{
					debug("Failed to send datagram, route unknown!");
				}
				catch (SIMPLTranslationException e)
				{
					debug("Failed to translate message!");
					e.printStackTrace();
				}
				catch (BufferOverflowException e)
				{
					debug("Message was too large: " + e.getLocalizedMessage());
				}
				catch (MessageTooLargeException e)
				{
					debug("Message was too large: " + e.getActualMessageSize());
				}
				catch (ClosedChannelException e)
				{
					debug("Disconnected Waiting for a reconnect");
					waitForReconnect();
				}
				catch (IOException e)
				{
					debug("Failed to send message: " + e.getMessage());
				}
				finally
				{
					// debug("6");
					buffer.clear();
					// debug("7");
					builder.clear();
					// debug("8");
					if (mdataMessage != null)
					{
						// debug("9");
						metaDataPool.release(mdataMessage.getAttachment());
						messagePool.release(mdataMessage);
					}
				}
			}
		}
	}

	/**
	 * Packet receiving thread. Deserializes incoming messages and passes them onto the packet handler
	 * threads.
	 * 
	 * @author bilhamil
	 * 
	 */
	protected class PacketReciever implements Runnable
	{
		private boolean	running	= false;

		Thread					t;

		synchronized public void start()
		{
			if (!running)
			{
				running = true;
				t = new Thread(this);
				t.setName("Packet Reciever Thread");
				t.start();
			}
		}

		/**
		 * Asynchronously stop the receiver thread.
		 */
		synchronized public void stop()
		{
			if (running)
			{
				running = false;
				while (t.isAlive())
				{
					selector.wakeup();
					try
					{
						t.join();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}

		public boolean isAlive()
		{
			return t != null && t.isAlive();
		}

		public void run()
		{
			ByteBuffer recieveBuffer = ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE * (doCompress ? 5 : 1));
			CharBuffer messageBuffer = CharBuffer.allocate(MAX_MESSAGE_SIZE * (doCompress ? 5 : 1));
			byte[] inBuffer = new byte[MAX_MESSAGE_SIZE * (doCompress ? 5 : 1)];
			byte[] outBuffer = new byte[MAX_MESSAGE_SIZE * (doCompress ? 5 : 1)];

			while (running)
			{
				try
				{
					selector.select();

					Set<SelectionKey> keys = selector.selectedKeys();
					for (SelectionKey key : keys)
					{
						if (key.isReadable())
						{
							DatagramChannel channel = (DatagramChannel) key.channel();
							if (channel.isOpen())
							{
								try
								{
									InetSocketAddress address = (InetSocketAddress) channel.receive(recieveBuffer);
									recieveBuffer.flip();

									if (doCompress)
									{
										/* Decompress */
										byte[] array = inBuffer;
										recieveBuffer.get(inBuffer, 0, recieveBuffer.limit());

										inflater.reset();

										int compressedSize = recieveBuffer.limit();
										inflater.setInput(array, 0, compressedSize);

										int decompressedSize;
										if (recieveBuffer.hasArray())
										{
											decompressedSize = inflater.inflate(recieveBuffer.array(),
																													0,
																													recieveBuffer.capacity());

											if (decompressedSize == 0)
											{
												debug("Failed to decompress message!");
												continue;
											}
											recieveBuffer.position(0);
											recieveBuffer.limit(decompressedSize);
										}
										else
										{
											decompressedSize = inflater.inflate(outBuffer, 0, outBuffer.length);

											if (decompressedSize == 0)
											{
												debug("Failed to decompress message!");
												continue;
											}
											recieveBuffer.clear();
											recieveBuffer.put(outBuffer, 0, decompressedSize);
											recieveBuffer.flip();
										}
									}

									long uid = recieveBuffer.getLong();

									decoder.decode(recieveBuffer, messageBuffer, true);

									messageBuffer.flip();

									ServiceMessage<S> message = (ServiceMessage<S>) translationScope.deserializeCharSequence(messageBuffer);
									message.setSender(address.getAddress());

									PacketHandler handler = handlerPool.acquire();
									handler.processMessage(uid, message, key, address);
								}
								catch (ClosedChannelException e)
								{
									debug("Channel closed!");
									waitForReconnect();
								}
								catch (IOException e)
								{
									e.printStackTrace(System.err);
								}
								catch (SIMPLTranslationException e)
								{
									debug("Failed to translate message!");
									e.printStackTrace();
								}
								catch (DataFormatException e)
								{
									debug("Failed to unzip datagram!");
									e.printStackTrace();
								}
								finally
								{
									recieveBuffer.clear();
									messageBuffer.clear();
								}
							}
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			
			handlerPool.shutdown();
		}
	}

	/**
	 * Queue up out going message
	 * 
	 * @param m
	 *          message being sent
	 * @param key
	 * @param uid
	 *          message uid
	 * @param addr
	 *          socket address to send the message to.
	 */
	public void sendMessage(ServiceMessage<S> m, SelectionKey key, Long uid, InetSocketAddress addr)
	{
		MessageWithMetadata<ServiceMessage<S>, MessageMetaData> mdataMessage = messagePool.acquire();
		MessageMetaData metaData = metaDataPool.acquire();
		mdataMessage.setMessage(m);
		mdataMessage.setUid(uid);

		metaData.addr = addr;
		metaData.key = key;
		mdataMessage.setAttachment(metaData);

		try
		{
			outgoingMessageQueue.put(mdataMessage);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	synchronized protected long getNextUID()
	{
		return currentUIDIndex++;
	}

	public void start()
	{
		sender.start();
		reciever.start();
	}

	public void stop()
	{
		sender.stop();
		reciever.stop();

		try
		{
			this.selector.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	protected boolean isRunning()
	{
		return sender.isAlive() || reciever.isAlive();
	}

	abstract protected void waitForReconnect();

	/**
	 * Abstract message to be overriden to specify how to handle incomeing messages. Gets called from
	 * with a Packet Handler.
	 * 
	 * @param uid
	 * @param message
	 * @param key
	 * @param address
	 */
	abstract protected void handleMessage(long uid, ServiceMessage<S> message, SelectionKey key,
			InetSocketAddress address);
}
