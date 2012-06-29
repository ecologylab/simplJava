//	---------------------------------------------------------------------------
//	jWebSocket - Copyright (c) 2010 jwebsocket.org
//	---------------------------------------------------------------------------
//	This program is free software; you can redistribute it and/or modify it
//	under the terms of the GNU Lesser General Public License as published by the
//	Free Software Foundation; either version 3 of the License, or (at your
//	option) any later version.
//	This program is distributed in the hope that it will be useful, but WITHOUT
//	ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//	FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
//	more details.
//	You should have received a copy of the GNU Lesser General Public License along
//	with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
//	---------------------------------------------------------------------------
package org.jwebsocket.android.library;

import java.util.Properties;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.token.BaseTokenClient;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * jWebSocket android service that runs in a different process than the
 * application. Because it runs in another process, the client code using this
 * process must use IPC to interact with it. Please follow the sample
 * application to see the usage of this service.
 * <p>
 * Note that the most applications do not need to deal with the complexity shown
 * here. If your application simply has a service running in its own process,
 * the {@code JWC} is a simpler way to interact with it.
 * </p>
 * 
 * @author puran
 */
public class JWSAndroidRemoteService extends Service {

	/**
	 * This is the list of callbacks that have been registered with the service.
	 */
	final RemoteCallbackList<IJWSAndroidRemoteServiceCallback> jwsCallbackList = new RemoteCallbackList<IJWSAndroidRemoteServiceCallback>();
	private final static int MT_OPENED = 0;
	private final static int MT_PACKET = 1;
	private final static int MT_CLOSED = 2;
	private final static int MT_TOKEN = 3;
	private final static int MT_ERROR = -1;
	private final static String CONFIG_FILE = "jWebSocket";
	private static String jwsURL = "ws://jwebsocket.org:8787";
	private static BaseTokenClient mTokenClient;

	@Override
	public void onCreate() {
		mTokenClient = new BaseTokenClient();
		Properties lProps = new Properties();
		try {
			lProps.load(openFileInput(CONFIG_FILE));
		} catch (Exception ex) {
			Toast.makeText(getApplicationContext(), ex.getClass().getSimpleName() + ":" + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
		jwsURL = (String) lProps.getProperty("url", "http://jwebsocket.org:8787/");
	}
	/**
	 * Handler used to invoke the callback methods based on the remote interface operations. 
	 */
	private final Handler jwsHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MT_ERROR:
					broadCastMessageToCallback(msg);
					break;
				case MT_OPENED:
					broadCastMessageToCallback(msg);

				default:
					super.handleMessage(msg);
			}
		}
	};

	/**
	 * Broadcast the given message to all the callback listeners
	 * @param message the message object
	 */
	private void broadCastMessageToCallback(Message message) {
		final int N = jwsCallbackList.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				String error = (String) message.obj;
				jwsCallbackList.getBroadcastItem(i).onError(error);
			} catch (RemoteException lEx) {
				// The RemoteCallbackList will take care of removing
				// the dead object for us.
			}
		}
		jwsCallbackList.finishBroadcast();
	}
	/**
	 * This is the actual <tt>jWebSocket</tt> {@code BaseTokenClient} based
	 * implementation of the remote {@code JWSAndroidRemoteService} interface.
	 * Note that no exception thrown by the remote process can be sent back to the
	 * client.
	 */
	private final IJWSAndroidRemoteService.Stub mBinder = new IJWSAndroidRemoteService.Stub() {

		private void handleException(String tag, String error, Throwable lEx) {
			Log.e(tag, error, lEx);
			Message errorMsg = Message.obtain(jwsHandler, MT_ERROR, lEx.getMessage());
			jwsHandler.sendMessage(errorMsg);
		}

		//@Override
		public void open() throws RemoteException {
			try {
				mTokenClient.open(jwsURL);
			} catch (WebSocketException lEx) {
				handleException("OPEN", "Error opening jWebSocket connection", lEx);
			}
		}

		//@Override
		public void close() throws RemoteException {
			mTokenClient.close();
		}

		//@Override
		public void disconnect() throws RemoteException {
			try {
				mTokenClient.disconnect();
			} catch (WebSocketException lEx) {
				handleException("DISCONNECT", "Error disconnecting from the jWebSocket server", lEx);
			}
		}

		//@Override
		public void send(String data) throws RemoteException {
			try {
				mTokenClient.send(data, "UTF-8");
			} catch (WebSocketException lEx) {
				handleException("SEND", "Error sending data to the jWebSocket server", lEx);
			}
		}

		//@Override
		public void sendText(String target, String data) throws RemoteException {
			try {
				mTokenClient.sendText(target, data);
			} catch (WebSocketException lEx) {
				handleException("SENDTEXT", "Error sending text data to the jWebSocket server", lEx);
			}
		}

		//@Override
		public void broadcastText(String data) throws RemoteException {
			try {
				mTokenClient.broadcastText(data);
			} catch (WebSocketException lEx) {
				handleException("BROADCAST", "Error broadcasting data to the jWebSocket server", lEx);
			}
		}

		//@Override
		public void sendToken(ParcelableToken token) throws RemoteException {
			try {
				mTokenClient.sendToken(token.getToken());
			} catch (WebSocketException lEx) {
				handleException("SENDTOKEN", "Error sending token data to the jWebSocket server", lEx);
			}
		}

		//@Override
		public void saveFile(String fileName, String scope, boolean notify, byte[] data) throws RemoteException {
			try {
				mTokenClient.saveFile(data, fileName, scope, notify);
			} catch (WebSocketException lEx) {
				handleException("FILESAVE", "Error saving file to jWebSocket server", lEx);
			}
		}

		//@Override
		public String getUsername() throws RemoteException {
			return mTokenClient.getUsername();
		}

		//@Override
		public void login(String aUsername, String aPassword) throws RemoteException {
			try {
				mTokenClient.login(aUsername, aPassword);
			} catch (WebSocketException lEx) {
				handleException("LOGIN", "Error login to jWebSocket server", lEx);
			}
		}

		//@Override
		public void logout() throws RemoteException {
			try {
				mTokenClient.logout();
			} catch (WebSocketException lEx) {
				handleException("LOGOUT", "Error logout from jWebSocket server", lEx);
			}
		}

		//@Override
		public void ping(boolean echo) throws RemoteException {
			//TODO://fix this either display a notification or something visible to the user
			try {
				mTokenClient.ping(echo);
			} catch (WebSocketException lEx) {
				handleException("PING", "Error ping operation to jWebSocket server", lEx);
			}
		}

		//@Override
		public void getConnections() throws RemoteException {
			//TODO: implement this it should return something.. 
			//need a fix in TokenClient itself
		}

		//@Override
		public boolean isAuthenticated() throws RemoteException {
			return mTokenClient.isAuthenticated();
		}

		//@Override
		public void registerCallback(IJWSAndroidRemoteServiceCallback cb) throws RemoteException {
			jwsCallbackList.register(cb);
		}

		//@Override
		public void unregisterCallback(IJWSAndroidRemoteServiceCallback cb) throws RemoteException {
			jwsCallbackList.unregister(cb);
		}
	};

	/**
	 * When binding to the service, we return an interface to our messenger for
	 * sending messages to the service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		if (IJWSAndroidRemoteService.class.getName().equals(intent.getAction())) {
			return mBinder;
		}
		return null;
	}

	/**
	 * Token listener to receives the callback event from jWebSocket token client
	 */
	class Listener implements WebSocketClientTokenListener {

		public void processOpened(WebSocketClientEvent aEvent) {
			Message lMsg = new Message();
			lMsg.what = MT_OPENED;
			jwsHandler.sendMessage(lMsg);
		}

		public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
			Message lMsg = new Message();
			lMsg.what = MT_PACKET;
			lMsg.obj = aPacket;
			jwsHandler.sendMessage(lMsg);
		}

		public void processToken(WebSocketClientEvent aEvent, Token aToken) {
			Message lMsg = new Message();
			lMsg.what = MT_TOKEN;
			lMsg.obj = aToken;
			jwsHandler.sendMessage(lMsg);
		}

		public void processClosed(WebSocketClientEvent aEvent) {
			Message lMsg = new Message();
			lMsg.what = MT_CLOSED;
			jwsHandler.sendMessage(lMsg);
		}

		public void processOpening(WebSocketClientEvent aEvent) {
		}

		public void processReconnecting(WebSocketClientEvent aEvent) {
		}
	}
}
