package ecologylab.oodss.distributed.client;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.config.JWebSocketCommonConstants;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;

import android.os.AsyncTask;
import android.util.Log;
import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.oodss.distributed.common.ClientConstants;
import ecologylab.oodss.distributed.impl.MessageWithUid;
import ecologylab.oodss.messages.InitConnectionRequest;
import ecologylab.oodss.messages.InitConnectionResponse;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.oodss.messages.ServiceMessage;
import ecologylab.oodss.messages.UpdateMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class WebSocketOODSSClient <S extends Scope> extends Debug implements ClientConstants, WebSocketClientTokenListener
{
	private static final String TAG = "WebSocketOODSSClient";
	
	private BinaryWebSocketClient webSocketClient;
	private String currentMessage;
	private S objectRegistry;
	private SimplTypesScope translationScope;
	private String sessionId;
	private String serverAddress;
	private int portNumber;

	private int	uidIndex = 1;
	
	ResponseMessage initResponse;
	

	private final LinkedBlockingQueue<MessageWithUid>	blockingResponsesQueue	= new LinkedBlockingQueue<MessageWithUid>();
	private final LinkedBlockingQueue<MessageWithUid>	blockingRequestsQueue	= new LinkedBlockingQueue<MessageWithUid>();
	
	
	private volatile boolean running = false;
	private Thread MessageSenderWorker;
	
	private WebSocketOODSSConnectionCallbacks clientApplication;
	
	public WebSocketOODSSClient(String ipAddress, int portNumber, SimplTypesScope translationScope, S objectRegistry, WebSocketOODSSConnectionCallbacks clientApplication)
	{
		this.objectRegistry = objectRegistry;
		this.translationScope = translationScope;
		this.serverAddress = ipAddress;
		this.portNumber = portNumber;
		this.clientApplication = clientApplication;
		connect();
	}
	
	public void connect()
	{
		String webSocketPrefix = "ws://";
		String url = webSocketPrefix + serverAddress + ":" + portNumber;
		startSenderThreads();
		initializeWebSocketClient(url);
	}
	
	public void startSenderThreads()
	{
		running = true;

		MessageSenderWorker = new Thread(new MessageSender());
		MessageSenderWorker.start();
	}
	
	private class MessageSender implements Runnable
	{
		public void run() {
			while (running)
			{
				createPacketFromMessageAndSend();
			}
		}
	}
	
	private void initializeWebSocketClient(final String url)
	{
		webSocketClient = new BinaryWebSocketClient();
		webSocketClient.addListener(this);
		new EstablishConnectionTask().execute(url);
	}
	
	// do this asynchronously, because network code cannot be executed in main thread. 
	private class EstablishConnectionTask extends AsyncTask<String, Integer, Long>
	{

		@Override
		protected Long doInBackground(String... arg0) {
			Log.d(TAG, "to open websocket");
			webSocketClient.open(JWebSocketCommonConstants.WS_VERSION_DEFAULT, arg0[0], "basic");
			return null;
		}
		
		protected void onPostExecute (Long result)
		{
			//connect();
			if (webSocketClient.isConnected())
			{
				Log.d(TAG, "to send initConnectionInfo ");
				new SendAndReceiveInitConnectionInfo().execute(sessionId);
			}
				
		}
	}
	
	
	/**
	 *  disconnect the WebSocketOODSSClient from the server
	 *  stop the MessageSenderWorker thread then send websocket closing handshake 
	 */
	public void disconnect()
	{
		running = false;
		webSocketClient.close();
		webSocketClient.removeListener(this);
		new DisconnectTask().execute();
	}
	
	/**
	 *  reconnect the WebSocketOODSSClient to the same server
	 */
	public void reconnect()
	{
		if (!running)
			startSenderThreads();
		String webSocketPrefix = "ws://";
		String url = webSocketPrefix + serverAddress + ":" + portNumber;
		if (webSocketClient == null)
			initializeWebSocketClient(url);
		else
			new EstablishConnectionTask().execute(url);

	}
	
	private class DisconnectTask extends AsyncTask<Long, Long, Long>
	{
		@Override
		protected Long doInBackground(Long... params) {
			Log.d(TAG, "To close the websocket connection");
			webSocketClient.close();
			return null;
		}
	}
	
	private class SendAndReceiveInitConnectionInfo extends AsyncTask<String, Integer, ResponseMessage>
	{

		@Override
		protected ResponseMessage doInBackground(String... params) {
			// TODO Auto-generated method stub
			ResponseMessage initResponse = sendMessage((RequestMessage) new InitConnectionRequest(params[0]));
			return initResponse;
		}
		
		protected void onPostExecute(ResponseMessage initResponse)
		{
			if (initResponse instanceof InitConnectionResponse)
			{
				Log.d(TAG, "Received initial connection response");
				String receivedId = ((InitConnectionResponse) initResponse).getSessionId();
				if(sessionId == null)
				{
					sessionId = receivedId;
					Log.d(TAG, "SessionId: "+sessionId);
				}
				else if (sessionId == receivedId)
				{
					//do nothing;
				}
				else
				{
					unableToRestorePreviousConnection(sessionId, receivedId);
					sessionId = receivedId;
				}
				
				// connection is made. proceed. 
				clientApplication.webSocketConnected();
			}
		}
		
	}


	public boolean connected()
	{
		return webSocketClient.isConnected();
	}

	private boolean connectedImpl()
	{
		return connected();
	}
	
	private void unableToRestorePreviousConnection(String sessionId2, String receivedId)
	{
		// TODO Auto-generated method stub
		
	}
	
	public synchronized ResponseMessage sendMessage(RequestMessage request)
	{
		
		long uid = generateUid();
		
		blockingRequestsQueue.add(new MessageWithUid(request, uid));
		
		while (blockingResponsesQueue.isEmpty() || (blockingResponsesQueue.peek() != null && blockingResponsesQueue.peek().getUid() != uid))
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		MessageWithUid m;
		ResponseMessage responseMessage = null;
		try {
			m = blockingResponsesQueue.take();
			responseMessage = (ResponseMessage) m.getMessage();
			if (responseMessage != null)
				processResponse(responseMessage);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseMessage;		
	}
	
	private String generateStringFromRequest(RequestMessage request)
	{
		StringBuilder requestStringBuilder = new StringBuilder();
		try
		{
			SimplTypesScope.serialize(request, requestStringBuilder, StringFormat.XML);
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestStringBuilder.toString();
	}
	
	synchronized private long  generateUid()
	{
		return uidIndex ++;
	}

	private void createPacketFromMessageAndSend() 
	{	
		MessageWithUid m;
		try 
		{
			m = blockingRequestsQueue.take();
			RequestMessage request = (RequestMessage) m.getMessage();
			String requestString = generateStringFromRequest(request);
			
			long uid = m.getUid();
			
			Log.d(TAG, "out going message: " + requestString + " uid: " + (int)uid);
			
			byte[] uidBytes = longToBytes(uid);
			byte[] messageBytes = null;
			try {
				messageBytes = requestString.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] outMessage = new byte[uidBytes.length + messageBytes.length];
			if (uidBytes.length>0)
				System.arraycopy(uidBytes, 0, outMessage, 0, uidBytes.length);
			if (messageBytes.length>0)
				System.arraycopy(messageBytes, 0, outMessage, uidBytes.length, messageBytes.length);
			try {
				webSocketClient.send(outMessage);
			} catch (WebSocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// should put the messsage back 
				blockingRequestsQueue.put(m);
			}
		} 
		catch (InterruptedException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private byte[] longToBytes(long uid) {
		byte[] b = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(uid).array();
		return b;
	}


	private void processString(String incomingMessage, long incomingUid)
	{
		// TODO Auto-generated method stub
		ServiceMessage message = TranslateStringToServiceMessage(incomingMessage);
		if (message == null)
			Log.e(TAG, "no message. deserialization failed");
		else
		{
			if (message instanceof ResponseMessage)
			{
				Log.d(TAG, "got a response message");
				// add the response to the queue
				blockingResponsesQueue.add(new MessageWithUid(message, incomingUid));
				synchronized (this)
				{
					notify();
				}
			}
			else if (message instanceof UpdateMessage)
			{
				Log.d(TAG, "got an update message");
				processUpdate((UpdateMessage) message);
			}
		}
	}
	
	private void processResponse(ResponseMessage responseMessage)
	{
		responseMessage.processResponse(objectRegistry);
	}
	
	private void processUpdate(UpdateMessage updateMessage)
	{
		updateMessage.processUpdate(objectRegistry);
	}

	private ServiceMessage TranslateStringToServiceMessage(String incomingMessage)
	{
		try
		{
			ServiceMessage message = (ServiceMessage) translationScope.deserialize(incomingMessage, StringFormat.XML);
			return message;
		}
		catch (SIMPLTranslationException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void processOpening(WebSocketClientEvent aEvent)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "proecess opening...");
	}

	public void processOpened(WebSocketClientEvent aEvent)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "process opened...");
	}

	public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "process packet...");
		byte[] receivedData = aPacket.getByteArray();
		byte[] uidBytes = Arrays.copyOfRange(receivedData, 4, 12);
		byte[] messageBytes= Arrays.copyOfRange(receivedData, 12, receivedData.length);
		long uid = bytesToLong(uidBytes);
		currentMessage = new String();
		try
		{
			currentMessage = new String(messageBytes, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d(TAG, "Got the message: " + currentMessage + "uid: "+ uid);
		processString(currentMessage, uid);
	}

	private long bytesToLong(byte[] uidBytes)
	{	
		ByteBuffer bb = ByteBuffer.wrap(uidBytes);
		long l = bb.order(ByteOrder.LITTLE_ENDIAN).getLong();
		return l;
	}

	public void processClosed(WebSocketClientEvent aEvent)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "process closed...");
	}

	public void processReconnecting(WebSocketClientEvent aEvent)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "process reconnecting...");
	}

	public void processToken(WebSocketClientEvent aEvent, Token aToken)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "process token...");
	}
	
}
