package ecologylab.services.distributed.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import lib.Base64Coder;
import ecologylab.collections.Scope;
import ecologylab.services.distributed.impl.NIODatagramCore;
import ecologylab.services.distributed.server.clientsessionmanager.AbstractClientSessionManager;
import ecologylab.services.messages.InitConnectionRequest;
import ecologylab.services.messages.InitConnectionResponse;
import ecologylab.services.messages.MultiRequestMessage;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.services.messages.ServiceMessage;
import ecologylab.xml.TranslationScope;

public class NIODatagramServer<S extends Scope> extends NIODatagramCore<S>
{
	private SelectionKey key;
	
	private long sidIndex = 1;
	
	private ConcurrentHashMap<String, Scope> sidsToObjectRegistry = new ConcurrentHashMap<String, Scope>();
	
	private ConcurrentHashMap<SocketAddress, String> socketAddressesToSids = new ConcurrentHashMap<SocketAddress, String>();
	
	private ConcurrentHashMap<String, SocketAddress> sidsToSocketAddresses = new ConcurrentHashMap<String, SocketAddress>();
	
	private ConcurrentHashMap<String, String> reassignedSessions = new ConcurrentHashMap<String, String>();
	
	protected S applicationObjectScope;
	
	private MessageDigest													digester;

	private int	dispensedTokens = 1; 
	
	private int portNumber;
	
	public NIODatagramServer(int portNumber, TranslationScope translationScope, S objectRegistry, boolean useCompression)
	{
		super(translationScope, objectRegistry, useCompression);
		
		this.applicationObjectScope = objectRegistry;
		
		DatagramChannel chan;
		
		this.portNumber = portNumber;
		try
		{
			chan = DatagramChannel.open();
			chan.socket().bind(new InetSocketAddress(portNumber));
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
		
		try
		{
			digester = MessageDigest.getInstance("SHA-256");
		}
		catch (NoSuchAlgorithmException e)
		{
			weird("This can only happen if the local implementation does not include the given hash algorithm.");
			e.printStackTrace();
		}
		
		this.start();
	}
	
	public NIODatagramServer(int portNumber, TranslationScope translationScope, S objectRegistry)
	{
		this(portNumber, translationScope, objectRegistry, false);
	}

	@Override
	protected final void handleMessage(long uid, ServiceMessage<S> message,
			SelectionKey key, SocketAddress address)
	{
		Scope clientRegistry = null;
		String sid;
		
		if(message instanceof InitConnectionRequest)
		{
			InitConnectionRequest initReq = (InitConnectionRequest)message;
			synchronized(socketAddressesToSids)
			{
				if(initReq.getSessionId() == null)
				{
					//client expecting a new sid back
					//try to restore previous sid
					if((sid = socketAddressesToSids.get(address)) != null)
					{
						debug("Restoring session id: " + sid + " at :" + address);
						this.sendMessage(new InitConnectionResponse(socketAddressesToSids.get(address)),
											  key, uid, address);
					} 
					else
					{
						sid = this.generateSessionToken((InetSocketAddress) address);
						
						debug("New session: " + sid + " at: " + address);
						
						socketAddressesToSids.put(address, sid);
						sidsToSocketAddresses.put(sid, address);
						
						clientRegistry = new Scope(this.applicationObjectScope);
						clientRegistry.put(AbstractClientSessionManager.SESSION_ID, sid);
						onSessionCreation(sid, clientRegistry);
						
						sidsToObjectRegistry.put(sid, clientRegistry);
						this.sendMessage(new InitConnectionResponse(sid),
								  			  key, uid, address);
					}
				} 
				else
				{
					if(sidsToObjectRegistry.containsKey((initReq.getSessionId())))
					{
						sid = initReq.getSessionId();
						
						debug("Session: " + sid + " moved to " + address);
						
						socketAddressesToSids.put(address, sid);
						sidsToSocketAddresses.put(sid, address);
						this.sendMessage(new InitConnectionResponse(sid),
					  			  			  key, uid, address);
					} 
					else
					{
						if((sid = reassignedSessions.get(initReq.getSessionId())) == null)
						{
							sid = this.generateSessionToken((InetSocketAddress) address);
							reassignedSessions.put(initReq.getSessionId(), sid);
							
							socketAddressesToSids.put(address, sid);
							sidsToSocketAddresses.put(sid, address);
							
							clientRegistry = new Scope(this.applicationObjectScope);
							clientRegistry.put(AbstractClientSessionManager.SESSION_ID, sid);
							sidsToObjectRegistry.put(sid, new Scope(this.applicationObjectScope));
						}
						
						debug("Unknown session: " + initReq.getSessionId() + " at " + address + " reassinged to " + sid);
												
						this.sendMessage(new InitConnectionResponse(sid),
								  			  key, uid, address);
					}
				}
			}
		}
		else {
			synchronized(socketAddressesToSids)
			{
				if((sid = socketAddressesToSids.get(address)) != null)
				{
					clientRegistry = sidsToObjectRegistry.get(sid);
				} else {
					this.sendMessage(new InitConnectionRequest(), key, uid, address);
				}
			}
							
			if(clientRegistry != null)
			{
				handleAssociatedMessage(message, clientRegistry, key, uid, address);
			}
			
		}
		
	}
	
	protected void handleAssociatedMessage(ServiceMessage<S> message, Scope clientRegistry, 
														SelectionKey key, Long uid, SocketAddress address)
	{
		if(message instanceof RequestMessage)
		{
			ResponseMessage<S> response = ((RequestMessage)message).performService(clientRegistry);
			if(response != null)
			{
				this.sendMessage(response, key, uid, address);
			}
		}
		if(message instanceof MultiRequestMessage)
		{
			Collection<ResponseMessage> responses = ((MultiRequestMessage)message).performService(clientRegistry);
			for(ResponseMessage response : responses)
			{
				this.sendMessage(response, key, uid, address);
			}
		}
	}

	@Override
	protected void waitForReconnect()
	{
		// TODO Auto-generated method stub
		
	}
	
	synchronized protected long getNextSid()
	{
		return sidIndex++;
	}
	
	protected String generateSessionToken(InetSocketAddress incomingSocket)
	{
		// clear digester
		digester.reset();

		// we make a string consisting of the following:
		// time of initial connection (when this method is called),
		// client ip, client actual port
		digester.update(String.valueOf(System.currentTimeMillis()).getBytes());
		// digester.update(String.valueOf(System.nanoTime()).getBytes());
		// digester.update(this.incomingConnectionSockets[0].getInetAddress()
		// .toString().getBytes());
		digester.update(incomingSocket.getAddress().toString().getBytes());
		digester.update(String.valueOf(incomingSocket.getPort()).getBytes());

		digester.update(String.valueOf(this.dispensedTokens).getBytes());

		dispensedTokens++;

		// convert to normal characters and return as a String
		return new String(Base64Coder.encode(digester.digest()));
	}
	
	protected void onSessionCreation(String sid, Scope objectRegisry)
	{
		
	}
	
	public int getPortNumber()
	{
		return portNumber;
	}
	
	/**
	 * @return the global scope for this server
	 */
	public Scope getGlobalScope()
	{
		return applicationObjectScope;
	}
}
