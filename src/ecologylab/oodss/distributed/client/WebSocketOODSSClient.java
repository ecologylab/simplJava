package ecologylab.oodss.distributed.client;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.token.BaseTokenClient;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;

import com.sun.corba.se.impl.protocol.giopmsgheaders.RequestMessage;

import android.util.Log;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.oodss.distributed.common.ClientConstants;
import ecologylab.oodss.messages.InitConnectionRequest;
import ecologylab.oodss.messages.InitConnectionResponse;
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
	
	private void initializeWebSocketClient(String url)
	{
		webSocketClient = new BaseTokenClient();
		webSocketClient.addListener(this);
		
		try
		{
			webSocketClient.open(url);
			Log.d(TAG, "Client connected to: "+url);
		}
		catch (WebSocketException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean connect()
	{
		if (connectedImpl())
		{
			// TODO: open up a new thread to do this. 
			ResponseMessage initResponse = sendMessage((RequestMessage) new InitConnectionRequest(sessionId));
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
		return connected();
	}


	private boolean connected()
	{
		// TODO Auto-generated method stub
		return true;
	}

	private boolean connectedImpl()
	{
		//TODO
		return true;
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
		
		return null;		
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
	
	private long generateUid()
	{
		return uidIndex ++;
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
		long value = 0;
		for (int i = 0; i< uidBytes.length; i++)
		{
			value+=((long) uidBytes[i] & 0xffL) << (8*i);
		}	
		return value;
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
