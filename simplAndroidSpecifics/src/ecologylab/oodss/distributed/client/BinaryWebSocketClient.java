package ecologylab.oodss.distributed.client;


import java.io.*;
import java.net.HttpCookie;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javolution.util.FastList;
import javolution.util.FastMap;
import org.jwebsocket.api.*;
import org.jwebsocket.client.java.ReliabilityOptions;
import org.jwebsocket.client.token.WebSocketTokenClientEvent;
import org.jwebsocket.config.JWebSocketCommonConstants;
import org.jwebsocket.kit.*;
import org.jwebsocket.util.Tools;

public class BinaryWebSocketClient implements WebSocketClient{
	private static final int RECEIVER_SHUTDOWN_TIMEOUT = 3000;
	/**
	 * WebSocket connection URI
	 */
	private URI mURI = null;
	/**
	 * list of the listeners registered
	 */
	private List<WebSocketClientListener> mListeners = new FastList<WebSocketClientListener>();
	/**
	 * TCP socket
	 */
	private Socket mSocket = null;
	/**
	 * IO streams
	 */
	private InputStream mIn = null;
	private OutputStream mOut = null;
	/**
	 * Data receiver
	 */
	private WebSocketReceiver mReceiver = null;
	/**
	 * represents the WebSocket status
	 */
	protected volatile WebSocketStatus mStatus = WebSocketStatus.CLOSED;
	private List<WebSocketSubProtocol> mSubprotocols;
	private WebSocketSubProtocol mNegotiatedSubProtocol;
	/**
	 *
	 */
	public static String EVENT_OPEN = "open";
	/**
	 *
	 */
	public static String EVENT_CLOSE = "close";
	/**
	 *
	 */
	public static String DATA_CLOSE_ERROR = "error";
	/**
	 *
	 */
	public static String DATA_CLOSE_CLIENT = "client";
	/**
	 *
	 */
	public static String DATA_CLOSE_SERVER = "server";
	/**
	 *
	 */
	public static String DATA_CLOSE_SHUTDOWN = "shutdown";
	private static final String CR_CLIENT = "Client closed connection";
	private int mVersion = JWebSocketCommonConstants.WS_VERSION_DEFAULT;
	private WebSocketEncoding mEncoding = WebSocketEncoding.BINARY;
	private ReliabilityOptions mReliabilityOptions = null;
	private final ScheduledThreadPoolExecutor mExecutor = new ScheduledThreadPoolExecutor(1);
	private final Map<String, Object> mParams = new FastMap<String, Object>();
	private final Object mWriteLock = new Object();
	private String mCloseReason = null;
	private ScheduledFuture mReconnectorTask = null;
	private Boolean mIsReconnecting = false;
	private final Object mReconnectLock = new Object();
	private Headers mHeaders = null;
	private List<HttpCookie> mCookies = new ArrayList<HttpCookie>();

	/**
	 * Base constructor
	 */
	public BinaryWebSocketClient() {
	}

	public void setStatus(WebSocketStatus aStatus) throws Exception {
		if (aStatus.equals(WebSocketStatus.AUTHENTICATED)) {
			this.mStatus = aStatus;
		} else {
			throw new Exception("The value '" + aStatus.name()
					+ "' cannot be assigned. Restricted to internal usage only!");
		}
	}

	/**
	 * Constructor including reliability options
	 */
	public BinaryWebSocketClient(ReliabilityOptions aReliabilityOptions) {
		mReliabilityOptions = aReliabilityOptions;
	}

	/**
	 *
	 * @param aKey
	 * @param aDefault
	 * @return
	 */
	public Object getParam(String aKey, Object aDefault) {
		Object lValue = mParams.get(aKey);
		if (null == lValue) {
			lValue = aDefault;
		}
		return lValue;
	}

	/**
	 *
	 * @param aKey
	 * @return
	 */
	public Object getParam(String aKey) {
		return mParams.get(aKey);
	}

	/**
	 *
	 * @param aKey
	 * @param aValue
	 */
	public void setParam(String aKey, Object aValue) {
		mParams.put(aKey, aValue);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param aURI
	 */
	public void open(String aURI) throws WebSocketException {
		open(JWebSocketCommonConstants.WS_VERSION_DEFAULT, aURI);
	}

	/**
	 * Make a sub protocol string for Sec-WebSocket-Protocol header. The result
	 * is something like this:
	 * <pre>
	 * org.jwebsocket.json org.websocket.text org.jwebsocket.binary
	 * </pre>
	 *
	 * @return sub protocol list in one string
	 */
	private String generateSubProtocolsHeaderValue() {
		if (mSubprotocols == null || mSubprotocols.size() <= 0) {
			return JWebSocketCommonConstants.WS_SUBPROT_DEFAULT;
		} else {
			StringBuilder lBuff = new StringBuilder();
			for (WebSocketSubProtocol lProt : mSubprotocols) {
				lBuff.append(lProt.getSubProt()).append(' ');
			}
			return lBuff.toString().trim();
		}
	}

	/**
	 *
	 * @param aVersion
	 * @param aURI
	 * @throws WebSocketException
	 */
	public void open(int aVersion, String aURI) {
		String lSubProtocols = generateSubProtocolsHeaderValue();
		open(aVersion, aURI, lSubProtocols);
	}

	/**
	 *
	 * @param aVersion
	 * @param aURI
	 * @param aSubProtocols
	 * @throws WebSocketException
	 */
	public void open(int aVersion, String aURI, String aSubProtocols) {
		try {
			mAbortReconnect();

			// set default close reason in case 
			// connection could not be established.
			mCloseReason = "Connection could not be established.";

			mVersion = aVersion;
			mURI = new URI(aURI);
			// the WebSocket Handshake here generates the initial client side Handshake only
			WebSocketHandshake lHandshake = new WebSocketHandshake(mVersion, mURI, aSubProtocols);
			// close current socket if still connected 
			// to avoid open connections on server
			if (mSocket != null && mSocket.isConnected()) {
				mSocket.close();
			}
			mSocket = createSocket();
			// don't gather packages here, reduce latency
			mSocket.setTcpNoDelay(true);
			mIn = mSocket.getInputStream();
			mOut = mSocket.getOutputStream();

			// pass session cookie, if already was set for this client instance
			byte[] lBA;
			List<HttpCookie> lTempCookies = new ArrayList();
			if (null != mCookies) {
				HttpCookie lCookie;
				for (int lIndex = 0; lIndex < mCookies.size(); lIndex++) {
					lCookie = mCookies.get(lIndex);
					boolean lValid = Tools.isCookieValid(mURI, lCookie);
					if (lValid) {
						// Cookie is valid
						lTempCookies.add(lCookie);
					}
				}
			}
			lBA = lHandshake.generateC2SRequest(lTempCookies);
			mOut.write(lBA);

			mStatus = WebSocketStatus.CONNECTING;

			mHeaders = new Headers();
			try {
				mHeaders.readFromStream(aVersion, mIn);
			} catch (Exception lEx) {
				// ignore exception here, will be processed afterwards
			}

			// registering new cookies from the server response
			String lSetCookie = mHeaders.getField("Set-Cookie");
			if (null != lSetCookie) {
				List<HttpCookie> lCookies = HttpCookie.parse(lSetCookie);
				if (mCookies.isEmpty()) {
					mCookies.addAll(lCookies);
				} else {
					for (HttpCookie lCookie : lCookies) {
						for (int lIndex = 0; lIndex < mCookies.size(); lIndex++) {
							if (null == mCookies.get(lIndex).getDomain()
									|| HttpCookie.domainMatches(mCookies.get(lIndex).getDomain(), mURI.getHost())
									&& (null == lCookie.getPath()
									|| (null != mURI.getPath()
									&& mURI.getPath().startsWith(lCookie.getPath())))) {
								mCookies.set(lIndex, lCookie);
							}
						}
						if (!mCookies.contains(lCookie)) {
							mCookies.add(lCookie);
						}
					}
				}
			}

			if (!mHeaders.isValid()) {
				WebSocketClientEvent lEvent =
						new WebSocketBaseClientEvent(this, EVENT_CLOSE, "Handshake rejected.");
				notifyClosed(lEvent);
				mCheckReconnect(lEvent);
				return;
			}

			// parse negotiated sub protocol
			String lProtocol = mHeaders.getField(Headers.SEC_WEBSOCKET_PROTOCOL);
			if (lProtocol != null) {
				mNegotiatedSubProtocol = new WebSocketSubProtocol(lProtocol, mEncoding);
			} else {
				mNegotiatedSubProtocol = new WebSocketSubProtocol(
						JWebSocketCommonConstants.WS_SUBPROT_BINARY,
						WebSocketEncoding.BINARY);
				
			}
			// create new thread to receive the data from the new client
			mReceiver = new WebSocketReceiver(this, mIn);
			// and start the receiver thread for the port
			mReceiver.start();
			// now set official status, may listeners ask for that
			mStatus = WebSocketStatus.OPEN;
			// and finally notify listeners for OnOpen event
			WebSocketClientEvent lEvent =
					new WebSocketBaseClientEvent(this, EVENT_OPEN, "");
			// notify listeners that client has opened.
			notifyOpened(lEvent);

			// reset close reason to be specified by next reason
			mCloseReason = null;
		} catch (Exception lEx) {
			WebSocketClientEvent lEvent =
					new WebSocketBaseClientEvent(this, EVENT_CLOSE, mCloseReason);
			notifyClosed(lEvent);
			mCheckReconnect(lEvent);
		}
	}

	private void sendInTransaction(byte[] aData) throws WebSocketException {
		if (isHixie()) {
			sendInternal(aData);
		} else {
			WebSocketPacket lPacket = new RawPacket(aData);
			lPacket.setFrameType(
					WebSocketProtocolAbstraction.encodingToFrameType(
					mNegotiatedSubProtocol.getEncoding()));
			sendInternal(
					WebSocketProtocolAbstraction.rawToProtocolPacket(
					mVersion, lPacket));
		}
	}

	public void send(byte[] aData) throws WebSocketException {
		synchronized (mWriteLock) {
			sendInTransaction(aData);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void send(String aData, String aEncoding) throws WebSocketException {
		synchronized (mWriteLock) {
			byte[] lData;
			try {
				lData = aData.getBytes(aEncoding);
			} catch (UnsupportedEncodingException lEx) {
				throw new WebSocketException(
						"Encoding exception while sending the data:"
						+ lEx.getMessage(), lEx);
			}
			send(lData);
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param aDataPacket
	 */
	public void send(WebSocketPacket aDataPacket) throws WebSocketException {
		synchronized (mWriteLock) {
			if (isHixie()) {
				sendInternal(aDataPacket.getByteArray());
			} else {
				sendInternal(WebSocketProtocolAbstraction.rawToProtocolPacket(mVersion, aDataPacket));
			}
		}
	}

	private void sendInternal(byte[] aData) throws WebSocketException {
		if (!mStatus.isWritable()) {
			throw new WebSocketException("Error while sending binary data: not connected");
		}
		try {
			if (isHixie()) {
				if (WebSocketEncoding.BINARY.equals(mNegotiatedSubProtocol.getEncoding())) {
					mOut.write(0x80);
					// what if frame is longer than 255 characters (8bit?) Refer to IETF spec!
					// won't fix since hixie is far outdated!
					mOut.write(aData.length);
					mOut.write(aData);
				} else {
					mOut.write(0x00);
					mOut.write(aData);
					mOut.write(0xff);
				}
			} else {
				mOut.write(aData);
			}
			mOut.flush();
		} catch (IOException lEx) {
			terminateReceiverThread();
			throw new WebSocketException("Error while sending socket data: ", lEx);
		}
	}

	private void terminateReceiverThread() throws WebSocketException {
		mReceiver.quit();
		try {
			mReceiver.join(RECEIVER_SHUTDOWN_TIMEOUT);
		} catch (InterruptedException lEx) {
			throw new WebSocketException(
					"Receiver thread did not stop within "
					+ RECEIVER_SHUTDOWN_TIMEOUT + " ms", lEx);
		}
		mReceiver = null;
	}

	private void setCloseReason(String aCloseReason) {
		if (null == mCloseReason) {
			mCloseReason = aCloseReason;
		}
	}

	public synchronized void close() {
		// on an explicit close operation ...
		// cancel all potential re-connection tasks.
		mAbortReconnect();
		if (null != mReceiver) {
			mReceiver.quit();
		}

		if (!mStatus.isWritable()) {
			return;
		}
		setCloseReason(CR_CLIENT);
		try {
			sendCloseHandshake();
		} catch (Exception lEx) {
			// ignore that, connection is about to be terminated
		}
		try {
			// stopping the receiver thread stops the entire client
			terminateReceiverThread();
		} catch (Exception lEx) {
			// ignore that, connection is about to be terminated
		}
	}

	private void sendCloseHandshake() throws WebSocketException {
		if (!mStatus.isClosable()) {
			throw new WebSocketException("Error while sending close handshake: not connected");
		}
		synchronized (mWriteLock) {
			try {
				if (isHixie()) {
					// old hixie close handshake
					mOut.write(0xff00);
					mOut.flush();
				} else {
					WebSocketPacket lPacket = new RawPacket(WebSocketFrameType.CLOSE, "BYE");
					send(lPacket);
				}
			} catch (IOException lIOEx) {
				throw new WebSocketException("Error while sending close handshake", lIOEx);
			}
		}
	}

	private Socket createSocket() throws WebSocketException {
		String lScheme = mURI.getScheme();
		String lHost = mURI.getHost();
		int lPort = mURI.getPort();

		mSocket = null;

		if (lScheme != null && lScheme.equals("ws")) {
			if (lPort == -1) {
				lPort = 80;
			}
			try {
				mSocket = new Socket(lHost, lPort);
			} catch (UnknownHostException lUHEx) {
				throw new WebSocketException("Unknown host: " + lHost,
						WebSocketExceptionType.UNKNOWN_HOST, lUHEx);
			} catch (IOException lIOEx) {
				throw new WebSocketException("Error while creating socket to " + mURI,
						WebSocketExceptionType.UNABLE_TO_CONNECT, lIOEx);
			}
		} else if (lScheme != null && lScheme.equals("wss")) {
			if (lPort == -1) {
				lPort = 443;
			}
			try {
				try {
					// TODO: Make acceptance of unsigned certificates optional!
					// This methodology is used to accept unsigned certficates
					// on the SSL server. Be careful with this in production environments!

					// Create a trust manager to accept unsigned certificates
					TrustManager[] lTrustManager = new TrustManager[]{
						new X509TrustManager() {

							public java.security.cert.X509Certificate[] getAcceptedIssuers() {
								return null;
							}

							public void checkClientTrusted(
									java.security.cert.X509Certificate[] aCerts, String aAuthType) {
							}

							public void checkServerTrusted(
									java.security.cert.X509Certificate[] aCerts, String aAuthType) {
							}
						}
					};
					// Use this trustmanager to not reject unsigned certificates
					SSLContext lSSLContext = SSLContext.getInstance("TLS");
					lSSLContext.init(null, lTrustManager, new java.security.SecureRandom());
					mSocket = (SSLSocket) lSSLContext.getSocketFactory().createSocket(lHost, lPort);
				} catch (NoSuchAlgorithmException lNSAEx) {
					throw new RuntimeException("Unable to initialize SSL context", lNSAEx);
				} catch (KeyManagementException lKMEx) {
					throw new RuntimeException("Unable to initialize SSL context", lKMEx);
				}
			} catch (UnknownHostException lUHEx) {
				throw new WebSocketException("Unknown host: " + lHost,
						WebSocketExceptionType.UNKNOWN_HOST, lUHEx);
			} catch (IOException lIOEx) {
				throw new WebSocketException("Error while creating secure socket to " + mURI,
						WebSocketExceptionType.UNABLE_TO_CONNECT_SSL, lIOEx);
			} catch (Exception lEx) {
				throw new WebSocketException(lEx.getClass().getSimpleName() + " while creating secure socket to " + mURI, lEx);
			}
		} else {
			throw new WebSocketException("Unsupported protocol: " + lScheme,
					WebSocketExceptionType.PROTOCOL_NOT_SUPPORTED);
		}

		return mSocket;
	}

	/**
	 * {@inheritDoc }
	 */
	public boolean isConnected() {
		return mStatus.isConnected();
	}

	public WebSocketStatus getStatus() {
		return mStatus;
	}

	/**
	 * {@inheritDoc }
	 *
	 * @return
	 */
	public WebSocketStatus getConnectionStatus() {
		return mStatus;
	}

	/**
	 * @return the client socket
	 */
	public Socket getConnectionSocket() {
		return mSocket;
	}

	/**
	 * {@inheritDoc}
	 */
	public void addListener(WebSocketClientListener aListener) {
		mListeners.add(aListener);
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeListener(WebSocketClientListener aListener) {
		mListeners.remove(aListener);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<WebSocketClientListener> getListeners() {
		return Collections.unmodifiableList(mListeners);
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyOpened(WebSocketClientEvent aEvent) {
		for (WebSocketClientListener lListener : getListeners()) {
			lListener.processOpened(aEvent);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyPacket(WebSocketClientEvent aEvent, WebSocketPacket aPacket) {
		for (WebSocketClientListener lListener : getListeners()) {
			lListener.processPacket(aEvent, aPacket);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyReconnecting(WebSocketClientEvent aEvent) {
		for (WebSocketClientListener lListener : getListeners()) {
			lListener.processReconnecting(aEvent);
		}
	}

	/**
	 * @return the mReliabilityOptions
	 */
	public ReliabilityOptions getReliabilityOptions() {
		return mReliabilityOptions;
	}

	/**
	 * @param mReliabilityOptions the mReliabilityOptions to set
	 */
	public void setReliabilityOptions(ReliabilityOptions mReliabilityOptions) {
		this.mReliabilityOptions = mReliabilityOptions;
	}
	/*
	 * class ReOpener implements Runnable {
	 *
	 * private WebSocketClientEvent mEvent;
	 *
	 * public ReOpener(WebSocketClientEvent aEvent) { mEvent = aEvent; }
	 *
	 * @Override public void run() { notifyReconnecting(mEvent); try {
	 * open(mURI.toString()); } catch (WebSocketException ex) { // TODO: process
	 * potential exception here! } } }
	 */

	/**
	 * @return the mHeaders
	 */
	public Headers getHeaders() {
		return mHeaders;
	}

	class ReOpener implements Runnable {

		private WebSocketClientEvent mEvent;

		public ReOpener(WebSocketClientEvent aEvent) {
			mEvent = aEvent;
		}

		public void run() {
			mIsReconnecting = false;
			notifyReconnecting(mEvent);
			try {
				open(mURI.toString());
				// did we configure reliability options?
				/*
				 * if (mReliabilityOptions != null &&
				 * mReliabilityOptions.getReconnectDelay() > 0) {
				 * mExecutor.schedule( new ReOpener(aEvent),
				 * mReliabilityOptions.getReconnectDelay(),
				 * TimeUnit.MILLISECONDS); }
				 */
			} catch (Exception lEx) {
				WebSocketClientEvent lEvent =
						new WebSocketBaseClientEvent(mEvent.getClient(), EVENT_CLOSE,
						lEx.getClass().getSimpleName() + ": "
						+ lEx.getMessage());
				notifyClosed(lEvent);
			}
		}
	}

	private void mAbortReconnect() {
		synchronized (mReconnectLock) {
			// cancel running re-connect task
			if (null != mReconnectorTask) {
				mReconnectorTask.cancel(true);
			}
			// reset internal re-connection flag
			mIsReconnecting = false;
			mReconnectorTask = null;
			// clean up all potentially old references to inactive tasks
			mExecutor.purge();
		}
	}

	private void mCheckReconnect(WebSocketClientEvent aEvent) {
		synchronized (mReconnectLock) {
			// first, purge all potentially old references to other tasks
			mExecutor.purge();
			// did we configure reliability options?
			// and is there now re-connection task already active?
			if (mReliabilityOptions != null
					&& mReliabilityOptions.getReconnectDelay() > 0
					&& !mIsReconnecting) {
				// schedule a re-connect action after the re-connect delay
				mIsReconnecting = true;
				mReconnectorTask = mExecutor.schedule(
						new ReOpener(aEvent),
						mReliabilityOptions.getReconnectDelay(),
						TimeUnit.MILLISECONDS);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void notifyClosed(WebSocketClientEvent aEvent) {
		for (WebSocketClientListener lListener : getListeners()) {
			lListener.processClosed(aEvent);
		}
	}

	public void addSubProtocol(WebSocketSubProtocol aSubProt) {
		if (mSubprotocols == null) {
			mSubprotocols = new ArrayList<WebSocketSubProtocol>(3);
		}
		mSubprotocols.add(aSubProt);
	}

	public String getNegotiatedSubProtocol() {
		return mNegotiatedSubProtocol == null ? null : mNegotiatedSubProtocol.getSubProt();
	}

	public WebSocketEncoding getNegotiatedEncoding() {
		return mNegotiatedSubProtocol == null ? null : mNegotiatedSubProtocol.getEncoding();
	}

	public void setVersion(int aVersion) {
		this.mVersion = aVersion;
	}

	private boolean isHixie() {
		return WebSocketProtocolAbstraction.isHixieVersion(mVersion);
	}

	class WebSocketReceiver extends Thread {

		private WebSocketClient mClient = null;
		private InputStream mIS = null;
		private volatile boolean mIsRunning = false;

		public WebSocketReceiver(WebSocketClient aClient, InputStream aInput) {
			mClient = aClient;
			mIS = aInput;
		}

		@Override
		public void run() {
			Thread.currentThread().setName("jWebSocket-Client " + getId());

			mIsRunning = true;

			// the hixie and hybi processors handle potential exceptions
			if (isHixie()) {
				processHixie();
			} else {
				processHybi();
			}

			// set status AFTER close frame was sent, otherwise sending
			// close frame leads to an exception.
			mStatus = WebSocketStatus.CLOSING;
			String lExMsg = "";
			try {
				// shutdown methods are not implemented for SSL sockets
				if (!(mSocket instanceof SSLSocket)) {
					if (!mSocket.isOutputShutdown()) {
						mSocket.shutdownInput();
					}
				}
			} catch (IOException lIOEx) {
				lExMsg += "Shutdown input: " + lIOEx.getMessage() + ", ";
			}
			try {
				// shutdown methods are not implemented for SSL sockets
				if (!(mSocket instanceof SSLSocket)) {
					if (!mSocket.isOutputShutdown()) {
						mSocket.shutdownOutput();
					}
				}
			} catch (IOException lIOEx) {
				lExMsg += "Shutdown output: " + lIOEx.getMessage() + ", ";
			}
			try {
				if (!mSocket.isClosed()) {
					mSocket.close();
				}
			} catch (IOException lIOEx) {
				lExMsg += "Socket close: " + lIOEx.getMessage() + ", ";
			}

			// now the connection is really closed
			// set the status accordingly
			mStatus = WebSocketStatus.CLOSED;

			WebSocketClientEvent lEvent =
					new WebSocketBaseClientEvent(mClient, EVENT_CLOSE, mCloseReason);
			// notify listeners that client has closed
			notifyClosed(lEvent);

			quit();

			if (!CR_CLIENT.equals(mCloseReason)) {
				mCheckReconnect(lEvent);
			}
		}

		private void processHixie() {
			boolean lFrameStart = false;
			ByteArrayOutputStream lBuff = new ByteArrayOutputStream();
			while (mIsRunning) {
				try {
					int lB = mIS.read();
					// TODO: support binary frames
					if (lB == 0x00) {
						lFrameStart = true;
					} else if (lB == 0xff && lFrameStart == true) {
						lFrameStart = false;

						WebSocketClientEvent lWSCE = new WebSocketTokenClientEvent(mClient, null, null);
						RawPacket lPacket = new RawPacket(lBuff.toByteArray());

						lBuff.reset();
						notifyPacket(lWSCE, lPacket);
					} else if (lFrameStart == true) {
						lBuff.write(lB);
					} else if (lB == -1) {
						setCloseReason("Inbound stream terminated");
						mIsRunning = false;
					}
				} catch (Exception lEx) {
					mIsRunning = false;
					setCloseReason(lEx.getClass().getName() + " in hybi processor: " + lEx.getMessage());
				}
			}
		}

		private void processHybi() {
			WebSocketClientEvent lWSCE;
			WebSocketFrameType lFrameType;

			while (mIsRunning) {
				try {
					WebSocketPacket lPacket = WebSocketProtocolAbstraction.protocolToRawPacket(mVersion, mIS);
					lFrameType = (lPacket != null ? lPacket.getFrameType() : WebSocketFrameType.INVALID);
					if (null == lFrameType) {
						if (mIsRunning) {
							setCloseReason("Connection broken");
						} else {
							setCloseReason("Client terminated");
						}
						mIsRunning = false;
					} else if (WebSocketFrameType.INVALID == lFrameType) {
						mIsRunning = false;
						setCloseReason("Invalid hybi frame type detected");
					} else if (WebSocketFrameType.CLOSE == lFrameType) {
						mIsRunning = false;
						setCloseReason("Server closed connection");
					} else if (WebSocketFrameType.PING == lFrameType) {
						WebSocketPacket lPong = new RawPacket(
								WebSocketFrameType.PONG, "");
						send(lPong);
					} else if (WebSocketFrameType.PONG == lFrameType) {
						// TODO: need to process connection management here!
					} else if (WebSocketFrameType.TEXT == lFrameType) {
						lWSCE = new WebSocketTokenClientEvent(mClient, null, null);
						notifyPacket(lWSCE, lPacket);
					} else if (WebSocketFrameType.BINARY == lFrameType) {
						lWSCE = new WebSocketTokenClientEvent(mClient, null, null);
						notifyPacket(lWSCE, lPacket);
					}
				} catch (Exception lEx) {
					mIsRunning = false;
					setCloseReason(lEx.getClass().getName() + " in hybi processor: " + lEx.getMessage());
				}
			}
		}

		public void quit() {
			// ensure that reader loops are not continued
			mIsRunning = false;
			try {
				mIS.close();
			} catch (IOException ex) {
				// just to force client reader to stop
			}
		}

		public boolean isRunning() {
			return mIsRunning;
		}
	}
}
