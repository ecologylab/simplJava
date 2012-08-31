package ecologylab.oodss.distributed.server;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SelectionKey;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Arrays;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;

import ecologylab.collections.Scope;
import ecologylab.generic.HashMapArrayList;
import ecologylab.oodss.distributed.common.ServerConstants;
import ecologylab.oodss.distributed.common.SessionObjects;
import ecologylab.oodss.distributed.impl.Manager;
import ecologylab.oodss.distributed.impl.WebSocketServerImpl;
import ecologylab.oodss.distributed.server.clientsessionmanager.SessionHandle;
import ecologylab.oodss.distributed.server.clientsessionmanager.WebSocketClientSessionManager;
import ecologylab.oodss.exceptions.BadClientException;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.oodss.messages.ServiceMessage;
import ecologylab.oodss.messages.UpdateMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

/**
 * An OODSS server based on websocket
 * 
 * Subclasses should generally override the generateContextManager hook method, so that they can use
 * their own, specific ContextManager in place of the default.
 
 * @author shenfeng
 */
public class WebSocketOodssServer extends Manager implements Runnable, WebSocketServerProcessor, ServerConstants
{
	Thread t = null;
	boolean running = false;
	
	protected SimplTypesScope translationScope;
	protected Scope applicationObjectScope;
	
	public static final int DEFAULT_PORT = 2018;
	
	private WebSocketServerImpl webSocketServer;
	
	private String currentMessage;
	
	/**
	 * Map in which keys are sessionTokens, and values are associated ClientSessionManagers
	 */
	private HashMapArrayList<Object, WebSocketClientSessionManager> clientSessionManagerMap = new HashMapArrayList<Object, WebSocketClientSessionManager>();
	
	/**
	 * Map in which keys are sessionTokens, and values are associated SessionHandles
	 */
	private HashMapArrayList<Object, SessionHandle>	clientSessionHandleMap	= new HashMapArrayList<Object, SessionHandle>();

	private static final Charset	ENCODED_CHARSET		= Charset.forName(CHARACTER_ENCODING);
	private static CharsetDecoder	DECODER		= ENCODED_CHARSET.newDecoder();
	
	public WebSocketOodssServer(SimplTypesScope serverTranslationScope, Scope applicationObjectScope) throws UnknownHostException
	{
		this(new InetSocketAddress(DEFAULT_PORT), serverTranslationScope, applicationObjectScope);
	}
	
	public WebSocketOodssServer(int port, SimplTypesScope serverTranslationScope, Scope applicationObjectScope) throws UnknownHostException 
	{
		this(new InetSocketAddress(port), serverTranslationScope, applicationObjectScope);
	}
	
	public WebSocketOodssServer(InetSocketAddress address, SimplTypesScope serverTranslationScope, Scope applicationObjectScope) throws UnknownHostException 
	{
		this.webSocketServer = new WebSocketServerImpl(address, this);
		this.applicationObjectScope = applicationObjectScope;
		this.translationScope = serverTranslationScope;
		applicationObjectScope.put(SessionObjects.SESSIONS_MAP, clientSessionHandleMap);
		applicationObjectScope.put(SessionObjects.OODSS_WEBSOCKET_SERVER, this);
	}

	/**
	 * @return the global scope for this server
	 */
	public Scope getGlobalScope()
	{
		return applicationObjectScope;
	}

	/**
	 * @return the translationScope
	 */
	public SimplTypesScope getTranslationSpace()
	{
		return translationScope;
	}

	public void start()
	{
		debug("Server starting.");
		running = true;
		webSocketServer.start();
		if (t == null)
		{
			t = new Thread(this);
		}
		t.start();
	}
	
	public void stop()
	{
		debug("Server stopping.");
		running = false;
		synchronized(this)
		{
			this.notify();
			synchronized(t)
			{
				t = null;
			}
		}
	}
	
	@Override
	protected void shutdownImpl() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public boolean isRunning()
	{
		return running;
	}
	
	/**
	 * Attempt to invalidate sessions that are permanently disconnected
	 * 
	 * @param sessionId
	 * @param forcePermanent
	 * @return true if the session is invalidated. false if not
	 */
	@Override
	public boolean invalidate(String sessionId, boolean forcePermanent)
	{
		WebSocketClientSessionManager cm = clientSessionManagerMap.get(sessionId);
		
		// figure out if the disconnect is permanent; will be permanent if forcing
		// (usually bad client), if there is no context manager (client never sent
		// data), or if the client manager says it is invalidating (client
		// disconnected properly)
		boolean permanent = (forcePermanent ? true : (cm == null ? true : cm.isInvalidating()));
		
		// get the context manager...
		if (permanent)
		{
			synchronized (clientSessionManagerMap)
			{ // ...if this session will not be restored, remove the context
				// manager
				clientSessionManagerMap.remove(sessionId);
				clientSessionHandleMap.remove(sessionId);
			}
		}

		if (cm != null)
		{
			/*
			 * if we've gotten here, then the client has disconnected already, no reason to deal w/ the
			 * remaining messages // finish what the context manager was working on while
			 * (cm.isMessageWaiting()) { try { cm.processAllMessagesAndSendResponses(); } catch
			 * (BadClientException e) { e.printStackTrace(); } }
			 */
			cm.shutdown();
		}

		return permanent;
	}
	
	/**
	 * Attempts to switch the ContextManager for a SocketChannel. oldId indicates the session id that
	 * was used for the connection previously (in order to find the correct ContextManager) and
	 * newContextManager is the recently-created (and now, no longer necessary) ContextManager for the
	 * connection.
	 * 
	 * @param oldSessionId
	 * @param newSessionManager
	 * @return true if the restore was successful, false if it was not.
	 */
	@Override
	public boolean restoreContextManagerFromSessionId(String oldSessionId, WebSocketClientSessionManager newSessionManager)
	{
		debug("attempting to restore old session...");
		WebSocketClientSessionManager oldSessionManager;
		
		synchronized (clientSessionManagerMap)
		{
			oldSessionManager = this.clientSessionManagerMap.get(oldSessionId);
		}
		if (oldSessionManager == null)
		{
			// cannot restore old context
			debug("restore failed");
			return false;
		}
		else
		{
			//TODO: replace the sessionManager's socket with the new one
			oldSessionManager.setSocket(newSessionManager.getSocketKey());
			
			synchronized (clientSessionManagerMap)
			{
				/* remove pointers to new session manager since we're using the old one */
				this.clientSessionManagerMap.remove(newSessionManager.getSessionId());
				this.clientSessionHandleMap.remove(newSessionManager.getSessionId());
			}
			this.debug("old session restored");
			return true;
		}
	}

	/**
	 * send update message to a websocket client, uid is 0 for update message
	 * @param update
	 * 			update message
	 * @param conn
	 * 			websocket client
	 */
	public void sendUpdateMessage(UpdateMessage update, WebSocket conn) {
		createPacketFromMessageAndSend(0, update, conn);
	}

	/**
	 * serialize the oodss message, attach the uid, convert to bytes, and send to the websocket client
	 * the bytes array has its first 8 bits dedicated to uid, and the rest bits to the body of the 
	 * serialized oodss message
	 * the message is encoded in UTF-8
	 * 
	 * @param uid
	 * 		uid of the request message
	 * @param message
	 * 		oodss message
	 * @param conn
	 * 		websocket client
	 */
	private void createPacketFromMessageAndSend(long uid, ServiceMessage message, WebSocket conn)
	{
		StringBuilder messageStringBuilder = new StringBuilder();
		try {
			SimplTypesScope.serialize(message, messageStringBuilder, StringFormat.XML);
			String messageString = messageStringBuilder.toString();
			byte[] uidBytes = longToBytes(uid);
			byte[] messageBytes = messageString.getBytes("UTF-8");
			byte[] outMessage = new byte[uidBytes.length + messageBytes.length];
			System.arraycopy(uidBytes, 0, outMessage, 0, uidBytes.length);
			System.arraycopy(messageBytes, 0, outMessage, uidBytes.length, messageBytes.length);
			conn.send(outMessage);
			
		} catch (SIMPLTranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotYetConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * process the received raw message from websocket client
	 * the raw message is encoded in UTF-8, the first 8 bits are converted to uid, the rest are oodss message.
	 * 
	 * @param conn
	 * @param messageBytes
	 */
	public void processReceivedMessage(WebSocket conn, ByteBuffer messageBytes) 
	{
		// obtain uid
		byte[] messageByteArray = messageBytes.array();
		byte[] uidArray = Arrays.copyOfRange(messageByteArray, 0, 8);
		byte[] messageArray = Arrays.copyOfRange(messageByteArray, 8, messageByteArray.length);
		long uid = bytesToLong(uidArray);
		currentMessage = new String();
		try {
			currentMessage = new String(messageArray, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		debug("Got the message: " + currentMessage + " from uid: " + uid);
		try {
			processRead(conn, uid, currentMessage);
		} catch (BadClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * find webSocket client's corresponding session manager, and let the session manager process the oodss message. 
	 * If the session manager does not exist, a new session manager will be generated.
	 * 
	 * @param conn
	 * @param uid
	 * @param message
	 * @throws BadClientException
	 */
	protected void processRead(WebSocket conn, long uid, String message) throws BadClientException
	{
		if (currentMessage.length() > 0)
		{
			if (conn instanceof WebSocketImpl)
			{
				SelectionKey key = ((WebSocketImpl)conn).key;
				if (key != null)
				{
					String sessionToken =  ((WebSocketImpl)conn).socket.toString();
					if (!sessionToken.isEmpty() && sessionToken != null)
					{
						synchronized (clientSessionManagerMap)
						{		
							WebSocketClientSessionManager cm = clientSessionManagerMap.get(sessionToken);
							if (cm == null)
							{
								debug("server creating context manager for " + sessionToken);
								//TODO: 
								cm = generateContextManager(sessionToken, key, conn, translationScope, applicationObjectScope);
								clientSessionManagerMap.put(sessionToken, cm);
								clientSessionHandleMap.put(sessionToken, cm.getHandle());
							}
							
							ResponseMessage responseMessage = cm.processString(message, uid);
							
							createPacketFromMessageAndSend(uid, responseMessage, conn);
						}
					}
				}
			}
			synchronized (this)
			{
				this.notify();
			}
		}
	}
		
	/**
	 * Hook method to allow changing the ContextManager to enable specific extra functionality.
	 * 
	 * @param sessionToken
	 * @param translationScope
	 * @param applicationObjectScope
	 * @return
	 */
	protected WebSocketClientSessionManager generateContextManager(
			String sessionToken, SelectionKey key, WebSocket conn, SimplTypesScope translationScope,
			Scope applicationObjectScope) {
		return new WebSocketClientSessionManager(sessionToken, key, conn, translationScope, applicationObjectScope, this);
	}
	
	/**
	 * convenience method to convert long to bytes
	 * 
	 * @param uid
	 * @return
	 */
	private byte[] longToBytes(long uid)
	{
		return ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(uid).array();
	}
	
	/**
	 * convenience method to convert bytes to long
	 * 
	 * @param bytes
	 * @return
	 */
	private long bytesToLong(byte[] bytes)
	{
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		return bb.order(ByteOrder.LITTLE_ENDIAN).getLong();
	}

	public void shutdownClient(WebSocket conn) {
		String sessionToken =  ((WebSocketImpl)conn).socket.toString();
		if (sessionToken!=null)
			clientSessionManagerMap.get(sessionToken).shutdown();
	}
}
