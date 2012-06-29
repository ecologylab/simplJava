//  ---------------------------------------------------------------------------
//  jWebSocket - Copyright (c) 2010 jwebsocket.org
//  ---------------------------------------------------------------------------
//  This program is free software; you can redistribute it and/or modify it
//  under the terms of the GNU Lesser General Public License as published by the
//  Free Software Foundation; either version 3 of the License, or (at your
//  option) any later version.
//  This program is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for
//  more details.
//  You should have received a copy of the GNU Lesser General Public License along
//  with this program; if not, see <http://www.gnu.org/licenses/lgpl.html>.
//  ---------------------------------------------------------------------------
package org.jwebsocket.android.library;

import java.util.List;
import java.util.Properties;

import javolution.util.FastList;

import org.jwebsocket.api.WebSocketClientEvent;
import org.jwebsocket.api.WebSocketClientTokenListener;
import org.jwebsocket.api.WebSocketPacket;
import org.jwebsocket.client.token.BaseTokenClient;
import org.jwebsocket.kit.RawPacket;
import org.jwebsocket.kit.WebSocketException;
import org.jwebsocket.token.Token;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

/**
 * jWebSocket Android Service that runs locally in the same process as the
 * application.
 * 
 * Note that this service is very local to the current thread and can be used
 * by only one client (activity/service) at a time. 
 * 
 * For more complicated background jWebSocket service follow 
 * {@link JWSAndroidRemoteService} which allows multiple clients to use 
 * the same service which handles multiple connections via multiple 
 * processes.
 * 
 * The sample code to bid to this service using onCreate() method of Activity:
 * 
 * <pre>
 * private JWSLocalAndroidService mBoundService = null;
 * 
 * private ServiceConnection mConnection = new ServiceConnection() {
 *   public void onServiceConnected(ComponentName className, IBinder service) {
 *      // This is called when the connection with the service has been
 *      // established, giving us the service object we can use to
 *      // interact with the service.  Because we have bound to a explicit
 *      // service that we know is running in our own process, we can
 *      // cast its IBinder to a concrete class and directly access it.
 *      mBoundService = ((JWSLocalAndroidService.LocalBinder)service).getService();
 *       
 *   public void onServiceDisconnected(ComponentName className) {
 *      // This is called when the connection with the service has been
 *      // unexpectedly disconnected -- that is, its process crashed.
 *      // Because it is running in our same process, we should never
 *      // see this happen.
 *      mBoundService = null;
 *   };
 *      
 *   void doBindService() {
 *      // Establish a connection with the service.  We use an explicit
 *      // class name because we want a specific service implementation that
 *      // we know will be running in our own process (and thus won't be
 *      // supporting component replacement by other applications).
 *      bindService(new Intent(Binding.this, JWSLocalAndroidService.class), mConnection, Context.BIND_AUTO_CREATE);
 *           mIsBound = true;
 *   }
 *   
 *   void doUnbindService() {
 *      if (mIsBound) {
 *         // Detach our existing connection.
 *         unbindService(mConnection);
 *         mIsBound = false;
 *       }
 *    }
 *    
 *    @Override
 *    protected void onCreate(Bundle savedInstanceState) {
 *       super.onCreate(savedInstanceState);
 *       doBindService();
 *       
 *       //open the connection using default conf
 *       mBoundService.open();
 *    }
 * </pre>
 * @author aschulze 
 * @author <a href="http://www.purans.net/">Puran Singh</a>
 */
public class JWSLocalAndroidService extends Service {

	/** callback events */
	private final static int MT_OPENED = 0;
	private final static int MT_PACKET = 1;
	private final static int MT_CLOSED = 2;
	private final static int MT_TOKEN = 3;
	private final static String CONFIG_FILE = "jWebSocket";
	private static String baseJWSUrl = "ws://jwebsocket.org:8787";
	private static BaseTokenClient baseTokenClient;
	protected NotificationManager jwsNotification;
	/**
	 * Each client activity or service can be a listener to receives event notification of jwebsocket events.
	 */
	private static List<WebSocketClientTokenListener> mListeners = new FastList<WebSocketClientTokenListener>();
	private static String DEF_ENCODING = "UTF-8";

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {

		JWSLocalAndroidService getService() {
			return JWSLocalAndroidService.this;
		}
	}
	// This is the object that receives interactions from clients.
	private final IBinder mBinder = new LocalBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		jwsNotification = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Display a notification about us starting. 
		showStartingNotification();

		baseTokenClient = new BaseTokenClient();
		baseTokenClient.addListener(new Listener());
	}

	@Override
	public void onDestroy() {
	}

	public void loadSettings(Activity aActivity) {
		Properties lProps = new Properties();
		try {
			lProps.load(aActivity.openFileInput(CONFIG_FILE));
		} catch (Exception ex) {
			Toast.makeText(aActivity.getApplicationContext(), ex.getClass().getSimpleName() + ":" + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
		baseJWSUrl = (String) lProps.getProperty("url", "http://jwebsocket.org:8787/");
	}

	public void saveSettings(Activity aActivity) {
		Properties lProps = new Properties();
		try {
			lProps.put("url", baseJWSUrl);
			lProps.save(aActivity.openFileOutput(CONFIG_FILE, Context.MODE_PRIVATE), "jWebSocketClient Configuration");
		} catch (Exception ex) {
			Toast.makeText(aActivity.getApplicationContext(), ex.getClass().getSimpleName() + ":" + ex.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	public void open() throws WebSocketException {
		baseTokenClient.open(baseJWSUrl);
	}

	public void close() throws WebSocketException {
		baseTokenClient.close();
	}

	public void send(String aString) throws WebSocketException {
		baseTokenClient.send(baseJWSUrl, DEF_ENCODING);
	}

	public void sendToken(Token aToken) throws WebSocketException {
		baseTokenClient.sendToken(aToken);
	}

	public void sendText(String aTarget, String aData) throws WebSocketException {
		baseTokenClient.sendText(aTarget, aData);

	}

	public void broadcastText(String aData) throws WebSocketException {
		baseTokenClient.broadcastText(aData);
	}

	public void saveFile(byte[] aData, String aFilename, String aScope, Boolean aNotify) throws WebSocketException {
		baseTokenClient.saveFile(aData, aFilename, aScope, aNotify);
	}

	public void addListener(WebSocketClientTokenListener aListener) {
		mListeners.add(aListener);
	}

	public void removeListener(WebSocketClientTokenListener aListener) {
		mListeners.remove(aListener);
	}
	private Handler messageHandler = new Handler() {

		@Override
		public void handleMessage(Message message) {

			switch (message.what) {
				case MT_OPENED:
					notifyOpened(null);
					break;
				case MT_PACKET:
					notifyPacket(null, (RawPacket) message.obj);
					break;
				case MT_TOKEN:
					notifyToken(null, (Token) message.obj);
					break;
				case MT_CLOSED:
					notifyClosed(null);
					break;
			}
		}
	};

	public void notifyOpened(WebSocketClientEvent aEvent) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processOpened(aEvent);
		}
	}

	public void notifyPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processPacket(aEvent, aPacket);
		}
	}

	public void notifyToken(WebSocketClientEvent aEvent, Token aToken) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processToken(aEvent, aToken);
		}
	}

	public void notifyClosed(WebSocketClientEvent aEvent) {
		for (WebSocketClientTokenListener lListener : mListeners) {
			lListener.processClosed(aEvent);
		}
	}

	/**
	 * @return the URL
	 */
	public String getURL() {
		return baseJWSUrl;
	}

	/**
	 * private listener for receiving notification from the server 
	 *
	 */
	class Listener implements WebSocketClientTokenListener {

		public void processOpened(WebSocketClientEvent aEvent) {
			Message lMsg = new Message();
			lMsg.what = MT_OPENED;
			messageHandler.sendMessage(lMsg);
		}

		public void processPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
			Message lMsg = new Message();
			lMsg.what = MT_PACKET;
			lMsg.obj = aPacket;
			messageHandler.sendMessage(lMsg);
		}

		public void processToken(WebSocketClientEvent aEvent, Token aToken) {
			Message lMsg = new Message();
			lMsg.what = MT_TOKEN;
			lMsg.obj = aToken;
			messageHandler.sendMessage(lMsg);
		}

		public void processClosed(WebSocketClientEvent aEvent) {
			Message lMsg = new Message();
			lMsg.what = MT_CLOSED;
			messageHandler.sendMessage(lMsg);
		}

		public void processOpening(WebSocketClientEvent aEvent) {
		}

		public void processReconnecting(WebSocketClientEvent aEvent) {
		}
	}

	/**
	 * client should override this method to show notification
	 */
	public void showStartingNotification() {
	}
}
