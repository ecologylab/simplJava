package ecologylab.oodss.distributed.client;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.token.BaseTokenClient;
import org.jwebsocket.config.JWebSocketCommonConstants;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;

import android.os.AsyncTask;
import android.util.Log;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.oodss.distributed.common.ClientConstants;
import ecologylab.oodss.distributed.exception.MessageTooLargeException;
import ecologylab.oodss.distributed.impl.PreppedRequest;
import ecologylab.oodss.distributed.impl.PreppedRequestPool;
import ecologylab.oodss.messages.InitConnectionRequest;
import ecologylab.oodss.messages.InitConnectionResponse;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.SendableRequest;
import ecologylab.oodss.messages.ServiceMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.oodss.messages.UpdateMessage;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.formatenums.StringFormat;

public class WebSocketOODSSClient <S extends Scope> extends Debug implements ClientConstants, WebSocketClientTokenListener
{
	private static final String TAG = "WebSocketOODSSClient";
	
	private BaseTokenClient webSocketClient;
	private String currentMessage;
	private S objectRegistry;
	private SimplTypesScope translationScope;
	private String sessionId;
	private String serverAddress;
	private int portNumber;

	private int	uidIndex = 1;
	
	ResponseMessage initResponse;
	
	Map<Long, RequestMessage> unfulfilledRequests = new HashMap<Long, RequestMessage>();
	Map<Long, ResponseMessage> unprocessedResponse = new HashMap<Long, ResponseMessage>();

	public WebSocketOODSSClient(String ipAddress, int portNumber, SimplTypesScope translationScope, S objectRegistry)
	{
		this.objectRegistry = objectRegistry;
		this.translationScope = translationScope;
		this.serverAddress = ipAddress;
		this.portNumber = portNumber;
		
		String webSocketPrefix = "ws://";
		String url = webSocketPrefix + ipAddress + ":" + portNumber + "/websocket";
		initializeWebSocketClient(url);
	}
	
	private void initializeWebSocketClient(final String url)
	{
		webSocketClient = new BaseTokenClient();
		webSocketClient.addListener(this);
		new EstablishConnectionTask().execute(url);
	}
	
	private class EstablishConnectionTask extends AsyncTask<String, Integer, Long>
	{

		@Override
		protected Long doInBackground(String... arg0) {
			webSocketClient.open(JWebSocketCommonConstants.WS_VERSION_DEFAULT, arg0[0], "basic");
			return null;
		}
		
		protected void onPostExecute (Long result)
		{
			Log.i(TAG, "to run connect()");
			//connect();
			if (webSocketClient.isConnected())
				new SendAndReceiveInitConnectionInfo().execute(sessionId);
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
			}
		}
		
	}
	
//	public boolean connect()
//	{
//		String connected = connectedImpl()?"connected":"notConnected";
//		Log.i(TAG, connected);
//		if (connectedImpl())
//		{
//			// TODO: open up a new thread to do this. 
//			new Thread(new Runnable() {public void run() {
//			initResponse = sendMessage((RequestMessage) new InitConnectionRequest(sessionId));
//			}}).start();
//			if (initResponse instanceof InitConnectionResponse)
//			{
//				Log.d(TAG, "Received initial connection response");
//				String receivedId = ((InitConnectionResponse) initResponse).getSessionId();
//				if(sessionId == null)
//				{
//					sessionId = receivedId;
//					Log.d(TAG, "SessionId: "+sessionId);
//				}
//				else if (sessionId == receivedId)
//				{
//					//do nothing;
//				}
//				else
//				{
//					unableToRestorePreviousConnection(sessionId, receivedId);
//					sessionId = receivedId;
//				}
//			}
//		}
//		return connected();
//	}


	private boolean connected()
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
	
	public ResponseMessage sendMessage(RequestMessage request)
	{
		String requestString = generateStringFromRequest(request);
		long uid = generateUid();
		
		// TODO: 
		addToUnfulfilledRequests(uid, request);
		createPacketFromMessageAndSend(uid, requestString);
		
		ResponseMessage responseMessage = getResponseMessage(uid);
		
		
		processResponse(responseMessage);
		
		removeFromUnprocessedResponse(uid);
		removeFromUnfulfilledRequests(uid);
		
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

	synchronized private void addToUnfulfilledRequests(long uid, RequestMessage request) {
		unfulfilledRequests.put(uid, request);
	}

	private void createPacketFromMessageAndSend(long uid, String requestString) {
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
		}
	}

	private byte[] longToBytes(long uid) {
		byte[] b = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(uid).array();
		return b;
	}

	private ResponseMessage getResponseMessage(long uid) {
		ResponseMessage message;
		while(!unprocessedResponse.containsKey(uid))
		{
			try {
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.e(TAG, "intercepted");
				e.printStackTrace();
			}
		}
		synchronized(unprocessedResponse)
		{
			message = unprocessedResponse.get(uid);
		}
		return message;
	}

	synchronized private void removeFromUnprocessedResponse(long uid) {
		unprocessedResponse.remove(uid);
	}

	synchronized private void removeFromUnfulfilledRequests(long uid) {
		unfulfilledRequests.remove(uid);
	}

	synchronized private void addToUnprocessedResponse(long uid, ResponseMessage response) {
		unprocessedResponse.put(uid, response);
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
				// TODO: add the response to the queue
				addToUnprocessedResponse(incomingUid, (ResponseMessage) message);
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
		byte[] uidBytes = Arrays.copyOfRange(receivedData, 0, 8);
		byte[] messageBytes= Arrays.copyOfRange(receivedData, 8, receivedData.length);
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
//		long value = 0;
//		for (int i = 0; i< uidBytes.length; i++)
//		{
//			value+=((long) uidBytes[i] & 0xffL) << (8*i);
//		}	
//		
//		return value;
		
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
