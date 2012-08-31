package ecologylab.oodss.distributed.impl;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.java_websocket.WebSocket;
import org.java_websocket.framing.CloseFrame;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import ecologylab.generic.Debug;
import ecologylab.oodss.distributed.server.WebSocketOodssServer;

public class WebSocketServerImpl extends WebSocketServer
{
	WebSocketOodssServer oodssServer;
	
	public WebSocketServerImpl(InetSocketAddress address, WebSocketOodssServer oodssServer) throws UnknownHostException
	{
		super(address);
		this.oodssServer = oodssServer;
	}

	@Override
	public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
		if (remote)
			System.out.println("WebSocket client closing: " + conn.getRemoteSocketAddress() 
				+ " because of " + reason);
		else 
			System.out.println("WebSocket server closing, because of: " + reason);
		if (conn!= null && conn.isClosed())
			oodssServer.shutdownClient(conn);
	}

	@Override
	public void onError( WebSocket conn, Exception ex ) {
		System.out.println("WebSocket error: " + conn.getRemoteSocketAddress() + " " + ex.getMessage());
	}

	@Override
	public void onMessage( WebSocket conn, String message ) {
		System.out.println("WebSocket receives message from: " + conn.getRemoteSocketAddress());
		ByteBuffer messageBytes = Charset.forName("UTF8").encode(message);
		oodssServer.processReceivedMessage(conn, messageBytes);
	}

	@Override
	public void onOpen( WebSocket conn, ClientHandshake handshake ) {
		System.out.println("WebSocket client connected: " + conn.getRemoteSocketAddress()
				+ " handshake: " + handshake);
	}
	
	@Override
	public void onMessage( WebSocket conn, ByteBuffer message) {
		System.out.println("WebSocket receives message from: " + conn.getRemoteSocketAddress());
		oodssServer.processReceivedMessage(conn, message);
	}

}
