package ecologylab.services.distributed.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.generic.ResourcePool;
import ecologylab.services.distributed.common.NetworkingConstants;
import ecologylab.services.distributed.exception.MessageTooLargeException;
import ecologylab.services.messages.ServiceMessage;
import ecologylab.xml.ElementState;
import ecologylab.xml.TranslationScope;
import ecologylab.xml.XMLTranslationException;

public abstract class NIODatagramCore<S extends Scope> extends Debug implements
		NetworkingConstants
{
	protected long																								currentUIDIndex		= 1;

	private MessageWithMetadataPool<ServiceMessage<S>, MessageMetaData>						messagePool				= new MessageWithMetadataPool<ServiceMessage<S>, MessageMetaData>(
																																						4,
																																						4);

	private MessageMetaDataPool																			metaDataPool			= new MessageMetaDataPool();

	protected static final int																				MAX_DATAGRAM_SIZE		= 1500;

	protected static final int																				HEADER_SIZE				= Long.SIZE / 8;
	
	protected static final int																				UDP_HEADER_SIZE 		= 8;

	protected static final int																				MAX_MESSAGE_SIZE		= MAX_DATAGRAM_SIZE
																																						- HEADER_SIZE
																																						- UDP_HEADER_SIZE;

	private SynchronousQueue<MessageWithMetadata<ServiceMessage<S>, MessageMetaData>>	outgoingMessageQueue	= new SynchronousQueue<MessageWithMetadata<ServiceMessage<S>, MessageMetaData>>();

	protected Selector																						selector;

	protected TranslationScope	translationScope;
	
	protected S objectRegistry;
	
	private PacketHandlerPool																				handlerPool = new PacketHandlerPool();
	
	protected PacketSender																					sender = new PacketSender();
	
	protected PacketReciever																				reciever = new PacketReciever();
	
	protected boolean 																						doCompress = false;
	
	protected Deflater 																						deflater	= new Deflater();
	
	protected Inflater																						inflater = new Inflater();

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
		public SelectionKey	key;

		public SocketAddress	addr;

	}

	protected class PacketHandlerPool extends ResourcePool<PacketHandler>
	{
		public PacketHandlerPool()
		{
			super(true, 4, 4, true);
		}

		protected void clean(PacketHandler objectToClean)
		{
			objectToClean.reset();
		}

		protected PacketHandler generateNewResource()
		{
			PacketHandler handler = new PacketHandler(this);
			Thread newThread = new Thread(handler, "Packet Handler");
			newThread.start();

			return handler;
		}

		protected void onRemoval(PacketHandler handler)
		{
			handler.stop();
		}
	}

	protected class PacketHandler implements Runnable
	{
		private PacketHandlerPool	pool;

		private long					uid;

		private ServiceMessage<S>	message				= null;

		private SelectionKey			recievedOnSocket	= null;

		private SocketAddress		recievedFrom		= null;

		private boolean				done					= false;
		
		private Thread 				t 						= null;

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

		synchronized public void stop()
		{
			done = true;
			if(t != null)
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
				if(message != null)
				{
					try{
						handleMessage(uid, message, recievedOnSocket, recievedFrom);
					} catch(Exception e) {
						debug("Failed in message handler: " + e.getMessage());
						e.printStackTrace();
					} finally {
						message = null;
						pool.release(this);
					}
				}
			}
		}

		synchronized public void processMessage(long uid, ServiceMessage<S> message, SelectionKey key, SocketAddress address)
		{
			this.uid = uid;
			this.message = message;
			this.recievedOnSocket = key;
			this.recievedFrom = address;
			notify();
		}
	}

	protected class PacketSender implements Runnable
	{
		private boolean running	= false;
		private Thread t;
		
		synchronized public void start()
		{
			if(!running)
			{
				running = true;
				t = new Thread(this);
				t.setName("Packet Sender Thread");
				t.start();
			}
		}
		
		synchronized public void stop()
		{
			if(running)
			{
				running = false;
				while(t.isAlive())
				{
					t.interrupt();
					try
					{
						t.join();
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
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
			ByteBuffer buffer = ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE*(doCompress?5:1));
			CharBuffer builder = CharBuffer.allocate(MAX_MESSAGE_SIZE*(doCompress?5:1));
			byte[] inBuffer = new byte[MAX_MESSAGE_SIZE*(doCompress?5:1)];
			byte[] outBuffer = new byte[MAX_MESSAGE_SIZE*(doCompress?5:1)];

			if(buffer.hasArray())
				debug("Buffer has array!");
			
			while (running)
			{
				MessageWithMetadata<ServiceMessage<S>, MessageMetaData> mdataMessage = null;
				try
				{
					mdataMessage = outgoingMessageQueue.take();

					buffer.putLong(mdataMessage.getUid());
					
					mdataMessage.getMessage().translateToXML(builder);					
					
					builder.flip();

					ENCODER.encode(builder, buffer, true);
					buffer.flip();

					if(doCompress)
					{
						byte[] array = inBuffer; 
						buffer.get(inBuffer, 0, buffer.limit());
												
						deflater.reset();
						
						int uncompressedSize = buffer.limit();
						deflater.setInput(array, 0, uncompressedSize);
						deflater.finish();					
						
						int compressedSize;
						if(buffer.hasArray())
						{
							compressedSize = deflater.deflate(buffer.array(), 0, buffer.capacity());
							buffer.position(0);
							buffer.limit(compressedSize);
						} else {
							compressedSize = deflater.deflate(outBuffer, 0, outBuffer.length);
							buffer.clear();
							buffer.put(outBuffer, 0, compressedSize);
							buffer.flip();
						}
						if(compressedSize > MAX_MESSAGE_SIZE)
						{
							throw new MessageTooLargeException(MAX_MESSAGE_SIZE, compressedSize);
						}
						//debug("Input size: " + uncompressedSize + " and output size: " + compressedSize);
					}
					
					DatagramChannel channel = (DatagramChannel) mdataMessage
							.getAttachment().key.channel();
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
				catch (XMLTranslationException e)
				{
					debug("Failed to translate message!");
					e.printStackTrace();
				}
				catch (BufferOverflowException e)
				{
					debug("Message was too large!");
				}
				catch (MessageTooLargeException e)
				{
					debug("Message was too large!");
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
					buffer.clear();
					builder.clear();
					if(mdataMessage != null)
					{
						metaDataPool.release(mdataMessage.getAttachment());
						messagePool.release(mdataMessage);
					}
				}
			}
		}
	}

	protected class PacketReciever implements Runnable
	{
		private boolean running = false;
		Thread t;
		
		synchronized public void start()
		{
			if(!running)
			{
				running = true;
				t = new Thread(this);
				t.setName("Packet Reciever Thread");
				t.start();
			}
		}
		
		synchronized public void stop()
		{
			if(running)
			{
				running = false;
				while(t.isAlive())
				{
					selector.wakeup();
					try
					{
						t.join();
					}
					catch (InterruptedException e)
					{
						// TODO Auto-generated catch block
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
			ByteBuffer recieveBuffer = ByteBuffer.allocateDirect(MAX_MESSAGE_SIZE*(doCompress?5:1));
			CharBuffer messageBuffer = CharBuffer.allocate(MAX_MESSAGE_SIZE*(doCompress?5:1));
			byte[] inBuffer = new byte[MAX_MESSAGE_SIZE*(doCompress?5:1)];
			byte[] outBuffer = new byte[MAX_MESSAGE_SIZE*(doCompress?5:1)];
			
			while(running)
			{
				try
				{
					selector.select();
					
					Set<SelectionKey> keys = selector.selectedKeys();
					for(SelectionKey key: keys)
					{
						if(key.isReadable())
						{
							DatagramChannel channel = (DatagramChannel) key.channel();
							if(channel.isOpen())
							{
								try {
									SocketAddress address = channel.receive(recieveBuffer);
									recieveBuffer.flip();
									
									if(doCompress)
									{
										byte[] array = inBuffer; 
										recieveBuffer.get(inBuffer, 0, recieveBuffer.limit());
																										
										inflater.reset();
										
										int compressedSize = recieveBuffer.limit();
										inflater.setInput(array, 0, compressedSize);
										
										
										int decompressedSize;
										if(recieveBuffer.hasArray())
										{
											decompressedSize = inflater.inflate(recieveBuffer.array(), 0, recieveBuffer.capacity());
											
											if(decompressedSize == 0)
											{
												debug("Failed to decompress message!");
												continue;
											}
											recieveBuffer.position(0);
											recieveBuffer.limit(decompressedSize);
										} else {
											decompressedSize = inflater.inflate(outBuffer, 0, outBuffer.length);
											
											if(decompressedSize == 0)
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
																		
									DECODER.decode(recieveBuffer, messageBuffer, true);
									
									messageBuffer.flip();
									
									ServiceMessage<S> message = (ServiceMessage<S>)ElementState.translateFromXMLCharSequence(messageBuffer,
																																						  translationScope);
									message.setSender(((InetSocketAddress)address).getAddress());
									
									PacketHandler handler = handlerPool.acquire();
									handler.processMessage(uid, message, key, address);
								} catch (ClosedChannelException e)
								{
									debug("Channel closed!");
									waitForReconnect();
								} catch (IOException e) {
									e.printStackTrace(System.err);
								} catch (XMLTranslationException e)	{
									debug("Failed to translate message!");
									e.printStackTrace();
								} catch (DataFormatException e) {
									debug("Failed to unzip datagram!");
									e.printStackTrace();
								} finally {
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
		}
	}

	public void sendMessage(ServiceMessage<S> m, SelectionKey key, Long uid,
			 						SocketAddress addr)
	{
		MessageWithMetadata<ServiceMessage<S>, MessageMetaData> mdataMessage = messagePool
				.acquire();
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
	}
	
	protected boolean isRunning()
	{
		return sender.isAlive() || reciever.isAlive();
	}
		
	abstract protected void waitForReconnect();

	abstract protected void handleMessage(long uid, ServiceMessage<S> message, SelectionKey key,
										 			  SocketAddress address);
}
