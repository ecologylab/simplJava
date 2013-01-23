package ecologylab.oodss.distributed.server.clientsessionmanager;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;

import org.java_websocket.WebSocket;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.oodss.distributed.common.SessionObjects;
import ecologylab.oodss.distributed.server.WebSocketOodssServer;
import ecologylab.oodss.distributed.server.WebSocketServerProcessor;
import ecologylab.oodss.messages.BadSemanticContentResponse;
import ecologylab.oodss.messages.InitConnectionRequest;
import ecologylab.oodss.messages.InitConnectionResponse;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.oodss.messages.UpdateMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class WebSocketClientSessionManager extends Debug
{
	/**
	 * Session handle available to use by clients
	 */
	protected SessionHandle				handle;
	
	/**
	 * sessionId uniquely identifies this ContextManager. It is used to restore the state of a lost
	 * connection.
	 */
	protected String	sessionId	= null;
	
	protected Scope localScope;
	protected SimplTypesScope translationScope;
	
	protected long lastActivity = System.currentTimeMillis();
	
	/** The selection key for this context manager. */
	protected SelectionKey				socketKey;
	protected WebSocket 				conn;
	/**
	 * The frontend for the server that is running the ContextManager. This is needed in case the
	 * client attempts to restore a session, in which case the frontend must be queried for the old
	 * ContextManager.
	 */
	protected WebSocketServerProcessor frontend  = null;
	
	/**
	 * Indicates whether the first request message has been received. The first request may be an
	 * InitConnection, which has special properties.
	 */
	protected boolean							initialized			= false;

	/**
	 * Used for disconnecting. A disconnect message will call the setInvalidating method, which will
	 * set this value to true. The processing method will set itself as pending invalidation after it
	 * has produces the bytes for the response to the disconnect message.
	 */
	private boolean								invalidating		= false;

	private boolean 				isShutdown = false;
	
	public static final String		SESSION_ID			= "SESSION_ID";

	public static final String		CLIENT_MANAGER	= "CLIENT_MANAGER";
	
	public WebSocketClientSessionManager(String sessionId,
			SelectionKey key, WebSocket conn, SimplTypesScope translationScope, Scope baseScope,
			WebSocketServerProcessor frontend) 
	{
		super();
		this.frontend = frontend;
		this.socketKey = key;
		this.sessionId = sessionId;
		this.conn = conn;
		this.localScope = generateContextScope(baseScope);
		this.localScope.put(SESSION_ID, sessionId);
		this.localScope.put(CLIENT_MANAGER, this);
		this.translationScope = translationScope;
		this.handle = new WebSocketSessionHandle(this);
		this.localScope.put(SessionObjects.SESSION_HANDLE, this.handle);
	}

	/**
	 * Provides the context scope for the client attached to this session manager. The base
	 * implementation instantiates a new Scope<?> with baseScope as the argument. Subclasses may
	 * provide specific subclasses of Scope as the return value. They must still incorporate baseScope
	 * as the lexically chained application object scope.
	 * 
	 * @param baseScope
	 * @return
	 */
	private Scope generateContextScope(Scope baseScope) {
		return new Scope(baseScope);
	}

	/**
	 * process serialized request message.
	 * 
	 * @param message
	 * @param uid
	 * @return
	 */
	public ResponseMessage processString(String message, long uid) {
		ResponseMessage responseMessage = null;
		Object o;
		try {
			o = translationScope.deserialize(message, StringFormat.XML);
			if (o instanceof RequestMessage)
			{
				responseMessage = processRequest((RequestMessage) o);
			}
		} catch (SIMPLTranslationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseMessage;
	}
	
	/**
	 * performService(RequestMessage) may be overridden by subclasses to provide more specialized
	 * functionality. Generally, overrides should then call super.performService(RequestMessage) so
	 * that the IP address is appended to the message.	 
	 *
	 * @param requestMessage
	 * @return
	 */
	protected ResponseMessage performService(RequestMessage requestMessage)
	{
		try
		{
			return requestMessage.performService(localScope);
		}
		catch (Exception e)
		{
			e.printStackTrace();

			return new BadSemanticContentResponse("The request, "
					+ requestMessage.toString()
					+ " caused an exception on the server.");
		}
	}
	
	/**
	 * Calls RequestMessage.performService(Scope) and returns the result.
	 * 
	 * @param requestMessage
	 * @return
	 */
	protected ResponseMessage processRequest(RequestMessage requestMessage)
	{
		this.lastActivity = System.currentTimeMillis();
		
		ResponseMessage response = null;
		
		if (requestMessage == null)
		{
			debug("No request.");
		}
		else
		{
			if (!isInitialized())
			{
				// special processing for InitConnectionRequest
				if (requestMessage instanceof InitConnectionRequest)
				{
					String incomingSessionId = ((InitConnectionRequest) requestMessage).getSessionId();

					if (incomingSessionId == null)
					{ // client is not expecting an old ContextManager
						response = new InitConnectionResponse(this.sessionId);
					}
					else
					{ // client is expecting an old ContextManager
						if (frontend.restoreContextManagerFromSessionId(incomingSessionId, this))
						{
							response = new InitConnectionResponse(incomingSessionId);
						}
						else
						{
							response = new InitConnectionResponse(this.sessionId);
						}
					}

					initialized = true;
				}
			}
			else
			{
				// perform the service being requested
				response = performService(requestMessage);
			}

			if (response == null)
			{
				debug("context manager did not produce a response message.");
			}
		}

		return response;		
	}
	
	public void sendUpdateToClient(UpdateMessage update)
	{
		if (!isShutDown())
		{
			WebSocketOodssServer server = (WebSocketOodssServer) localScope.get(SessionObjects.OODSS_WEBSOCKET_SERVER);
			server.sendUpdateMessage(update, conn);			
		}
	}
	
	/**
	 * Indicates the last System timestamp was when the ContextManager had any activity.
	 * 
	 * @return the last System timestamp indicating when the ContextManager had any activity.
	 */
	public final long getLastActivity()
	{
		return lastActivity;
	}
	
	/**
	 * Indicates whether or not this context manager has been initialized. Normally, this means that
	 * it has shared a session id with the client.
	 * 
	 * @return
	 */
	private boolean isInitialized() {
		return initialized;
	}

	public SessionHandle getHandle() 
	{
		// TODO Auto-generated method stub
		return handle;
	}

	/**
	 * @return the socket
	 */
	public SelectionKey getSocketKey() 
	{
		return socketKey;
	}

	public void setSocket(Object socketKey) 
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param invalidating
	 *          the invalidating to set
	 */
	public void setInvalidating(boolean invalidating)
	{
		this.invalidating = invalidating;
	}
	
	/**
	 * Indicates whether or not the client manager is expecting a disconnect. If this method returns
	 * true, then this client manager should be disposed of when the client disconnects; otherwise, it
	 * should be retained until the client comes back, or the client managers are cleaned up.
	 * 
	 * @return true if the client manager is expecting the client to disconnect, false otherwise
	 */
	public boolean isInvalidating()
	{
		return invalidating;
	}


	public String getSessionId()
	{
		return this.sessionId;
	}

	public Scope getScope()
	{
		return this.localScope;
	}

	/**
	 * Hook method for having shutdown behavior.
	 * 
	 * This method is called whenever the server is closing down the connection to this client.
	 */
	public void shutdown()
	{
		isShutdown = true;
	}

	public boolean isShutDown()
	{
		return isShutdown;
	}
	
	public InetSocketAddress getAddress() {
		// TODO Auto-generated method stub
		return conn.getRemoteSocketAddress();
	}


}
