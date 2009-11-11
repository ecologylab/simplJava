package ecologylab.services.distributed.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentHashMap;

import ecologylab.collections.Scope;
import ecologylab.services.distributed.impl.NIODatagramCore;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.services.messages.InitConnectionResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.ServiceMessage;
import ecologylab.xml.TranslationScope;

public class NIODatagramClient<S extends Scope> extends NIODatagramCore<S>
{
	protected String sid = null;
	
	protected SelectionKey key;
	
	protected int timeoutPeriod;
	
	protected InetSocketAddress serverAddress;
	
	protected ConcurrentHashMap<Long, Thread> pendingThreadsByUID = new ConcurrentHashMap<Long, Thread>();
	
	protected ConcurrentHashMap<Long, ResponseMessage<S>> responsesByUID = new ConcurrentHashMap<Long, ResponseMessage<S>>();

	public NIODatagramClient(InetSocketAddress serverAddress, InetSocketAddress localAddress, TranslationScope translationScope, S objectRegistry, boolean useCompression, int timeout)
	{
		super(translationScope, objectRegistry, useCompression);
		
		this.serverAddress = serverAddress;
		
		DatagramChannel chan;
		try
		{
			chan = DatagramChannel.open();
			chan.socket().bind(localAddress);
			chan.connect(serverAddress);
			chan.configureBlocking(false);
			
			key = chan.register(selector, SelectionKey.OP_READ);
		}
		catch (ClosedChannelException e)
		{
			debug("Channel isn't open but it should be!: " + e.getMessage());
			e.printStackTrace();
		}
		catch (SocketException e)
		{
			debug("Failed to open socket!: " + e.getMessage());
			e.printStackTrace();
		}
		catch (IOException e)
		{
			debug("Failed to open socket!: " + e.getMessage());
			e.printStackTrace();
		}
		
		this.timeoutPeriod = timeout;
		
		this.start();
		
		InitConnectionResponse initResponse = (InitConnectionResponse) this.sendMessage(new InitConnectionRequest());
		
		this.sid = initResponse.getSessionId();
	}

	public NIODatagramClient(InetSocketAddress serverAddress, InetSocketAddress localAddress, TranslationScope translationScope, S objectRegistry, int timeout)
	{
		this(serverAddress, localAddress, translationScope, objectRegistry, false, timeout);
	}
	
	public NIODatagramClient(InetSocketAddress serverAddress, TranslationScope translationScope, S objectRegistry, boolean useCompression, int timeout)
	{
		this(serverAddress, null, translationScope, objectRegistry, useCompression, timeout);
	}
	
	public NIODatagramClient(InetSocketAddress serverAddress, TranslationScope translationScope, S objectRegistry, int timeout)
	{
		this(serverAddress, translationScope, objectRegistry, false, timeout);
	}
	
	@Override
	protected void handleMessage(long uid, ServiceMessage<S> message,
			SelectionKey key, SocketAddress address)
	{
		Thread t;
		if(message instanceof InitConnectionRequest)
		{
			InitConnectionRequest connInit = new InitConnectionRequest(sid);
			InitConnectionResponse initResp = (InitConnectionResponse) this.sendMessage(connInit);
			if (!this.sid.equals(initResp.getSessionId()))
			{
				debug("Changing session id from: " + this.sid + " to " + initResp.getSessionId());
				this.sid = initResp.getSessionId();
			}
			return;
		}
		
		synchronized(pendingThreadsByUID)
		{
			t = pendingThreadsByUID.remove(uid);
		}
		if(message instanceof ResponseMessage)
		{
			ResponseMessage<S> response = (ResponseMessage<S>)message;
			response.processResponse(objectRegistry);
			
			if(t != null && message instanceof ResponseMessage)
			{				
				synchronized(t)
				{
					responsesByUID.put(uid, response);
					t.notify();
				}
			}
			
		}
	}

	@Override
	protected void waitForReconnect()
	{
	
	}

	public void sendMessageAsync(ServiceMessage message)
	{
		this.sendMessage(message, key, getNextUID(), null);
	}
	
	public ResponseMessage<S> sendMessage(RequestMessage message, int transmissionCount)
	{
		long myUid = this.getNextUID();
		Thread t = Thread.currentThread();
		
		pendingThreadsByUID.put(myUid, t);
		
		//don't need socket address since this should be connected
		this.sendMessage(message, key, myUid , null);
		transmissionCount--;
		
		synchronized(t)
		{
			while(!responsesByUID.containsKey(myUid))
			{
				try
				{
					t.wait(this.timeoutPeriod); 
					if(responsesByUID.containsKey(myUid))
					{
						pendingThreadsByUID.remove(myUid);
						return responsesByUID.remove(myUid);
					} 
					else
					{
						if(transmissionCount <= 0)
						{
							pendingThreadsByUID.remove(myUid);
							return null;
						}
						else
						{
							//retransmitting
							this.sendMessage(message, key, myUid, null);
							transmissionCount--;
						}
					}
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	public ResponseMessage<S> sendMessage(RequestMessage message)
	{
		long myUid = this.getNextUID();
		Thread t = Thread.currentThread();
		
		pendingThreadsByUID.put(myUid, t);
		
		//don't need socket address since this should be connected
		this.sendMessage(message, key, myUid , null);
		
		synchronized(t)
		{
			while(!responsesByUID.containsKey(myUid))
			{
				try
				{
					t.wait(this.timeoutPeriod); 
					if(responsesByUID.containsKey(myUid))
					{
						pendingThreadsByUID.remove(myUid);
						return responsesByUID.remove(myUid);
					} 
					else
					{
						if(message.isDisposable())
						{
							pendingThreadsByUID.remove(myUid);
							return null;
						}
						else
						{
							//retransmitting
							this.sendMessage(message, key, myUid, null);
						}
					}
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	protected void clearSessionId()
	{
		this.sid = null;
	}
	
	public boolean connected()
	{
		return key.channel().isOpen() && super.isRunning();
	}
	
	public InetSocketAddress getServer()
	{
		return serverAddress;
	}
	
	public void setServer(String server)
	{
		InetSocketAddress addr = new InetSocketAddress(server, serverAddress.getPort());
		
		DatagramChannel chan = (DatagramChannel) key.channel();
		
		try
		{
			chan.disconnect();
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try
		{
			chan.connect(addr);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.serverAddress = addr;
		
	}
}
