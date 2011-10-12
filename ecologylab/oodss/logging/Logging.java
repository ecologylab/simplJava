package ecologylab.oodss.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import ecologylab.appframework.ApplicationEnvironment;
import ecologylab.appframework.Memory;
import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.generic.Generic;
import ecologylab.generic.StartAndStoppable;
import ecologylab.io.Files;
import ecologylab.oodss.distributed.client.NIOClient;
import ecologylab.oodss.distributed.common.NetworkingConstants;
import ecologylab.oodss.distributed.common.ServicesHostsAndPorts;
import ecologylab.oodss.distributed.exception.MessageTooLargeException;
import ecologylab.oodss.messages.DefaultServicesTranslations;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.StringFormat;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scope;

/**
 * Provides a framework for interaction logging. Uses ecologylab.serialization to serialize user and
 * agent actions, and write them either to a file on the user's local machine, or, across the
 * network, to the LoggingServer.
 * 
 * @author andruid
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
public class Logging<T extends MixedInitiativeOp> extends ElementState implements
		StartAndStoppable, ServicesHostsAndPorts
{
	public static final String	MIXED_INITIATIVE_OP_TRANSLATION_SCOPE	= "MIXED_INITIATIVE_OP_TRANSLATION_SCOPE";

	/**
	 * This field is used for reading a log in from a file, but not for writing one, because we dont
	 * the write the log file all at once, and so can't automatically translate the start tag and end
	 * tag for this element.
	 */
	@simpl_collection
	@simpl_scope(MIXED_INITIATIVE_OP_TRANSLATION_SCOPE)
	protected ArrayList<T>			opSequence;

	Thread											thread;

	/**
	 * Does all the work of logging, if there is any work to be done. If this is null, then there is
	 * no logging; conversely, if there is no logging, this is null.
	 */
	ArrayList<LogWriter>				logWriters														= null;

	/**
	 * This is the Vector for the operations that are being queued up before they can go to
	 * outgoingOps.
	 */
	private StringBuilder				incomingOpsBuffer;

	/**
	 * This is the Vector for the operations that are in the process of being written out.
	 */
	private StringBuilder				outgoingOpsBuffer;

	/** Stores the pointer to outgoingOpsBuffer for swapQueues. */
	private StringBuilder				tempOpsBuffer;

	static final int						THREAD_PRIORITY												= 1;

	/** Amount of time for writer thread to sleep; 15 seconds */
	static final int						SLEEP_TIME														= 15000;

	static final long						sessionStartTime											= System.currentTimeMillis();

	long												lastGcTime;

	/** Amount of time to wait before booting the garbage collector; 5 minutes */
	static final long						KICK_GC_INTERVAL											= 300000;

	private static final String	SESSION_LOG_START											= "\n<session_log>\n ";

	static final String					OP_SEQUENCE_START											= "\n\n<op_sequence>\n\n";

	static final String					OP_SEQUENCE_END												= "\n</op_sequence>\n";

	/** Logging closing message string written to the logging file at the end */
	public static final String	LOG_CLOSING														= "\n</op_sequence></session_log>\n\n";

	/** Logging Header message string written to the logging file in the begining */
	static final String					BEGIN_EMIT														= XMLTools.xmlHeader()
																																				+ SESSION_LOG_START;

	/** Preference setting for no logging. */
	public static final int			NO_LOGGING														= 0;

	/** Preference setting for logging to a file using normal IO. */
	public static final int			LOG_TO_FILE														= 1;

	/** Preference setting for logging to a remote server. */
	public static final int			LOG_TO_SERVICES_SERVER								= 2;

	/** Preference setting for logging to a file using memory-mapped IO. */
	public static final int			LOG_TO_MEMORY_MAPPED_FILE							= 4;

	/** Preference setting for logging both to a memory-mapped file and a server. */
	public static final int			LOG_TO_MM_FILE_AND_SERVER_REDUNDANT		= LOG_TO_MEMORY_MAPPED_FILE
																																				& LOG_TO_SERVICES_SERVER;

	/** Preference setting for logging both to a normal IO file and a server. */
	public static final int			LOG_TO_FILE_AND_SERVER_REDUNDANT			= LOG_TO_FILE
																																				& LOG_TO_SERVICES_SERVER;

	static final int						MAX_OPS_BEFORE_WRITE									= 10;

	public static final String	LOGGING_HOST_PARAM										= "logging_host";

	public static final String	LOGGING_PORT_PARAM										= "logging_port";

	public static final String	LOGGING_MODE_PARAM										= "log_mode";

	final int										maxOpsBeforeWrite;

	final int										maxBufferSizeToWrite;

	/** used to prevent writes from getting interrupt()'ed */
	private Object							threadSemaphore												= new Object();

	private volatile boolean		runMethodDone													= false;

	volatile boolean						finished;

	private boolean							running																= false;

	/**
	 * Instantiates a Logging object based on the given log file name. This constructor assumes that a
	 * set of loaded {@link ecologylab.appframework.types.prefs.Pref Pref}s will handle other
	 * settings, indicating how logging will be performed, and the server setup (if logging over a
	 * network).
	 */
	public Logging(String logFileName)
	{
		this(logFileName, MAX_OPS_BEFORE_WRITE);
	}

	/**
	 * Instantiates a Logging object based on the given log file name and the maximum operations
	 * before write. This constructor assumes that a set of loaded
	 * {@link ecologylab.appframework.types.prefs.Pref Pref}s will handle other settings, indicating
	 * how logging will be performed, and the server setup (if logging over a network).
	 */
	public Logging(String logFileName, int maxOpsBeforeWrite)
	{
		this(	logFileName,
					false,
					maxOpsBeforeWrite,
					Pref.lookupInt(LOGGING_MODE_PARAM, NO_LOGGING),
					Pref.lookupString(LOGGING_HOST_PARAM, "localhost"),
					Pref.lookupInt(LOGGING_PORT_PARAM, ServicesHostsAndPorts.LOGGING_PORT),
					null);
	}

	/**
	 * Instantiates a Logging object based on the supplied parameters. This constructor does not rely
	 * on {@link ecologylab.appframework.types.prefs.Pref Pref}s.
	 * 
	 * @param logFileName
	 *          the name of the file to which the log will be written.
	 * @param logFileNameAbsolute
	 *          TODO
	 * @param maxOpsBeforeWrite
	 *          the maximum number of ops to record in memory before writing them to the set media.
	 * @param logMode
	 *          the media to which the logger will write, such as a memory-mapped file or a server.
	 * @param loggingHost
	 *          the host to which to log if using networked logging (may be null if local logging is
	 *          desired).
	 * @param loggingPort
	 *          the port of the host to which to log if using networked logging (may be 0 if local
	 *          logging is desired).
	 * @deprecated Use {@link #Logging(String,boolean,int,int,String,int,ApplicationEnvironment)}
	 *             instead
	 */
	public Logging(	String logFileName,
									boolean logFileNameAbsolute,
									int maxOpsBeforeWrite,
									int logMode,
									String loggingHost,
									int loggingPort)
	{
		this(	logFileName,
					logFileNameAbsolute,
					maxOpsBeforeWrite,
					logMode,
					loggingHost,
					loggingPort,
					null);
	}

	/**
	 * Instantiates a Logging object based on the supplied parameters. This constructor does not rely
	 * on {@link ecologylab.appframework.types.prefs.Pref Pref}s.
	 * 
	 * @param logFileName
	 *          the name of the file to which the log will be written.
	 * @param logFileNameAbsolute
	 *          TODO
	 * @param maxOpsBeforeWrite
	 *          the maximum number of ops to record in memory before writing them to the set media.
	 * @param logMode
	 *          the media to which the logger will write, such as a memory-mapped file or a server.
	 * @param loggingHost
	 *          the host to which to log if using networked logging (may be null if local logging is
	 *          desired).
	 * @param loggingPort
	 *          the port of the host to which to log if using networked logging (may be 0 if local
	 *          logging is desired).
	 * @param environment
	 *          TODO
	 */
	public Logging(	String logFileName,
									boolean logFileNameAbsolute,
									int maxOpsBeforeWrite,
									int logMode,
									String loggingHost,
									int loggingPort,
									ApplicationEnvironment environment)
	{
		this(maxOpsBeforeWrite);

		if (logMode == NO_LOGGING)
		{
			debug("logging disabled; NO_LOGGING specified");
		}
		else
		{
			logWriters = new ArrayList<LogWriter>(1);

			if ((logMode & LOG_TO_FILE) == LOG_TO_FILE)
			{
				if (logFileName == null)
				{
					debug("Logging disabled; no file name specified");
				}
				else
				{
					File logDir;
					if (!logFileNameAbsolute)
					{
						if (environment == null)
							logDir = PropertiesAndDirectories.logDir();
						else
							logDir = PropertiesAndDirectories.logDir(environment);
					}
					else
					{
						logDir = new File(logFileName);
					}

					if (logDir == null)
					{
						debug("Can't write to logDir=" + logDir);
					}
					else
					{
						File logFile;

						if (!logFileNameAbsolute)
						{
							logFile = new File(logDir, logFileName);
						}
						else
						{
							logFile = logDir;
						}
						debug("Logging to file: " + logFile.getAbsolutePath());

						try
						{
							XMLTools.createParentDirs(logFile);
						}
						catch (SIMPLTranslationException e)
						{
							e.printStackTrace();
						}

						BufferedWriter bufferedWriter = Files.openWriter(logFile);

						if (bufferedWriter != null)
						{
							try
							{
								logWriters.add(new FileLogWriter(logFile, bufferedWriter));
							}
							catch (IOException e)
							{
								e.printStackTrace();
							}
						}
						else
						{
							debug("ERROR: cant open writer to " + logFile);
						}
					}
				}
			}

			if ((logMode & LOG_TO_MEMORY_MAPPED_FILE) == LOG_TO_MEMORY_MAPPED_FILE)
			{
				if (logFileName == null)
				{
					debug("Logging disabled; no file name specified");
				}
				else
				{
					File logDir;
					if (!logFileNameAbsolute)
					{
						logDir = PropertiesAndDirectories.logDir();
					}
					else
					{
						logDir = new File(logFileName);
					}

					if (logDir == null)
					{
						debug("Can't write to logDir=" + logDir);
					}
					else
					{
						File logFile;

						if (!logFileNameAbsolute)
						{
							logFile = new File(logDir, logFileName);
						}
						else
						{
							logFile = logDir;
						}

						try
						{
							XMLTools.createParentDirs(logFile);
						}
						catch (SIMPLTranslationException e)
						{
							e.printStackTrace();
						}
						debug("Logging to file: " + logFile.getAbsolutePath());

						try
						{
							logWriters.add(new MemoryMappedFileLogWriter(logFile));
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			if ((logMode & LOG_TO_SERVICES_SERVER) == LOG_TO_SERVICES_SERVER)
			{
				/**
				 * Create the logging client which communicates with the logging server
				 */
				if (loggingHost == null)
					loggingHost = LOGGING_HOST;

				NIOClient loggingClient = null;
				try
				{
					loggingClient = new NIOClient(loggingHost,
																				loggingPort,
																				DefaultServicesTranslations.get(),
																				new Scope(),
																				NIOLoggingServer.MAX_MESSAGE_SIZE_CHARS_LOGGING);

					// CONNECT TO SERVER
					if (loggingClient.connect())
					{
						logWriters.add(new NetworkLogWriter(loggingClient, this));

						debug("logging to server: " + loggingHost + ":" + loggingPort);
					}
					else
					{
						loggingClient = null;
						debug("Logging disabled: cannot reach server");
					}
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}

				debug("**************************************************************connecting to server.");

			}
		}
	}

	/**
	 * Constructor for automatic translation from XML
	 * 
	 */
	public Logging()
	{
		this(MAX_OPS_BEFORE_WRITE);
	}

	protected Logging(int maxOpsBeforeWrite)
	{
		this.maxOpsBeforeWrite = maxOpsBeforeWrite;
		final int maxBufferSizeToWrite = maxOpsBeforeWrite * 1024;
		incomingOpsBuffer = new StringBuilder(maxBufferSizeToWrite);
		outgoingOpsBuffer = new StringBuilder(maxBufferSizeToWrite);
		this.maxBufferSizeToWrite = maxBufferSizeToWrite;
	}

	private void swapBuffers()
	{
		synchronized (outgoingOpsBuffer)
		{
			synchronized (incomingOpsBuffer)
			{
				if (outgoingOpsBuffer.length() == 0)
				{
					tempOpsBuffer = outgoingOpsBuffer;
					outgoingOpsBuffer = incomingOpsBuffer;
					incomingOpsBuffer = tempOpsBuffer;
				}
			}
		}
	}

	/**
	 * Translates op to XML then logs it, if this Logging object is running. Returns true if the
	 * operation was placed in the buffer to be written, false if it failed (because the op could not
	 * be translated to XML).
	 * 
	 * @param op
	 *          - the operation to be logged.
	 */
	public boolean logAction(MixedInitiativeOp op)
	{
		if (!this.finished && logWriters != null)
		{
			try
			{
				synchronized (incomingOpsBuffer)
				{
					SimplTypesScope.serialize(op, incomingOpsBuffer, StringFormat.XML);					
				}

				// final int bufferLength = incomingOpsBuffer.length();
				// if ((thread != null) && (bufferLength > maxBufferSizeToWrite))
				// {
				// synchronized (threadSemaphore)
				// {
				// debugA("interrupting thread to do i/o now: " + bufferLength
				// + "/" + maxBufferSizeToWrite);
				// thread.interrupt();
				// // end sleep in that thread prematurely to do i/o
				// }
				// }

				return true;
			}
			catch (SIMPLTranslationException e)
			{
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Returns the size of the list of log ops. May not be the correct value if called during logging.
	 * This method should only be used for playback purposes.
	 * 
	 * @return the size of opSequence.
	 */
	public int size()
	{
		return this.opSequence.size();
	}

	/**
	 * Write the start of the log header out to the log file OR, send the begining logging file
	 * message so that logging server write the start of the log header.
	 * <p/>
	 * Then start the looping thread that periodically wakes up and performs log i/o.
	 */
	public void start()
	{
		debug("Logging starting up...");
		if ((logWriters != null) && (thread == null))
		{
			thread = new Thread(this);
			thread.setPriority(THREAD_PRIORITY);
			thread.start();

			this.running = true;
		}
	}

	/**
	 * Finishes writing any queued actions, then sends the epilogue; then shuts down.
	 * 
	 */
	public synchronized void stop()
	{
		debug("shutting down...");

		if (!finished && thread != null)
		{
			finished = true;

			debug("initiating shutdown sequence...");

			int timesToWait = 100;

			while (!this.runMethodDone && timesToWait-- > 0)
			{
				debug("waiting on run method to finish log writing (attempts remaining " + timesToWait
						+ ")...");
				thread.interrupt();
				Generic.sleep(500);
			}

			if (timesToWait == 0)
			{
				debug("...giving up on waiting for run thread; continuing shutdown.");
			}
			else
			{
				debug("...done.");
			}

			this.thread = null;

			synchronized (threadSemaphore)
			{ // since we only write from run() if we're low on memory, we may
				// have all the ops still unwritten.
				writeBufferedOps();
			}

			if (logWriters != null)
			{
				final Epilogue epilogue = getEpilogue();
				// necessary for acquiring wrapper characters, like </op_sequence>
				final SendEpilogue sendEpilogue = new SendEpilogue(this, epilogue);

				for (LogWriter logWriter : logWriters)
				{
					logWriter.setPriority(9);
					debug("stop() writing epilogue to " + logWriter);
					logWriter.writeLogMessage(sendEpilogue);
				}

				// try
				// {
				// debug("epilogue contents: " + sendEpilogue.serialize());
				// }
				// catch (SIMPLTranslationException e)
				// {
				// e.printStackTrace();
				// }

				synchronized (threadSemaphore)
				{
					debug("forcing final write");
					writeBufferedOps();
				}

				for (LogWriter logWriter : logWriters)
				{
					debug("stop() closing " + logWriter);
					logWriter.close();
				}

				debug("...stop() finished.");

				logWriters = null;
			}
		}
		debug("...shutdown complete.");

		this.running = false;
	}

	/**
	 * Logging to a file is delayed to the actions of this thread, because otherwise, it can mess up
	 * priorities in the system, because events get logged in the highest priority thread.
	 * <p/>
	 * This MUST be the only thread that ever calls writeQueuedActions().
	 */
	public void run()
	{
		this.runMethodDone = false;
		this.finished = false;

		final Prologue prologue = getPrologue();
		String uid = Pref.lookupString("uid", "0");
		Logging.this.debug("Logging: Sending Prologue userID:" + uid);
		prologue.setUserID(uid);

		// necessary for acquiring wrapper characters, like <op_sequence>
		final SendPrologue sendPrologue = new SendPrologue(Logging.this, prologue);

		for (LogWriter logWriter : logWriters)
		{
			debug("sending prologue");
			logWriter.writeLogMessage(sendPrologue);
		}

		lastGcTime = System.currentTimeMillis();
		while (!finished)
		{
			Thread.interrupted();
			Generic.sleep(SLEEP_TIME, false);

			if (finished)
				debug("run thread awakened for final run");

			if (!Memory.reclaimIfLow())
			{
				synchronized (threadSemaphore)
				{
					writeBufferedOps();
				}
			}

			if (finished)
				debug("run thread finishing");
		}

		debug("run thread finished.");
		// now that we are finished, we let everyone else know
		this.runMethodDone = true;
	}

	/**
	 * Use the LogWriter, if there is one, to output queued actions to the log.
	 * <p/>
	 * NB: This method is SINGLE Threaded! It is not thread safe. It must only be called from the
	 * run() method.
	 */
	protected void writeBufferedOps()
	{
		StringBuilder bufferToWrite = incomingOpsBuffer;
		synchronized (bufferToWrite)
		{
			int size = bufferToWrite.length();

			if (size == 0)
				return;
			swapBuffers();
			// what was incomingOps is now outgoing!
			if (logWriters != null)
			{
				for (LogWriter logWriter : logWriters)
				{
					logWriter.writeBufferedOps(bufferToWrite);
				}
			}
			// debug("Logging: writeQueuedActions() after output loop.");
			bufferToWrite.setLength(0);
		}
	}

	/**
	 * A message at the beginnging of the log. This method may be overridden to return a subclass of
	 * Prologue, by subclasses of this, that wish to emit application specific information at the
	 * start of a log.
	 * 
	 * @return
	 */
	public Prologue getPrologue()
	{
		return new Prologue();
	}

	/**
	 * A message at the end of the log. This method may be overridden to return a subclass of
	 * Epilogue, by subclasses of this, that wish to emit application specific information at the end
	 * of a log.
	 * 
	 * @return
	 */
	protected Epilogue getEpilogue()
	{
		Epilogue epilogue = new Epilogue();
		return epilogue;
	}

	/**
	 * Return our session start timestamp.
	 * 
	 * @return
	 */
	public static final long sessionStartTime()
	{
		return sessionStartTime;
	}

	/**
	 * Objects that process queuedActions for writing, either to a local file, or, using the network,
	 * to the LoggingServer.
	 * 
	 * @author andruid
	 * @author Zachary O. Toups (zach@ecologylab.net)
	 */
	protected abstract class LogWriter extends Debug
	{
		LogWriter() throws IOException
		{

		}

		/**
		 * Get the bufferToLog() from the LogRequestMessage. Write it out!
		 * 
		 * @param logRequestMessage
		 */
		void writeLogMessage(LogEvent logRequestMessage)
		{
			StringBuilder buffy = logRequestMessage.bufferToLog();
			writeLogMessage(buffy);

		}

		abstract void writeLogMessage(StringBuilder xmlBuffy);

		void writeBufferedOps(StringBuilder opsBuffer)
		{
			writeLogMessage(opsBuffer);
		}

		abstract void close();

		public void setPriority(int priority)
		{

		}
	}

	/**
	 * LogWriter that uses a memory-mapped local file for logging.
	 * 
	 * @author andruid
	 * @author Zachary O. Toups (zach@ecologylab.net)
	 */
	protected class MemoryMappedFileLogWriter extends LogWriter
	{
		// BufferedWriter bufferedWriter;
		MappedByteBuffer				buffy								= null;

		FileChannel							channel							= null;

		private CharsetEncoder	encoder							= Charset.forName(NetworkingConstants.CHARACTER_ENCODING)
																													.newEncoder();

		private File						logFile;

		/**
		 * The base size for the log file, and the amount it will be incremented whenever its buffer
		 * overflows.
		 */
		static final int				LOG_FILE_INCREMENT	= 1024 * 512;

		private int							endOfMappedBytes		= LOG_FILE_INCREMENT;

		MemoryMappedFileLogWriter(File logFile) throws IOException
		{
			this.logFile = logFile;

			channel = new RandomAccessFile(logFile, "rw").getChannel();

			// allocate LOG_FILE_INCREMENT for the file size; this will be
			// incremented as necessary
			buffy = channel.map(MapMode.READ_WRITE, 0, LOG_FILE_INCREMENT);

			// if (bufferedWriter == null)
			// throw new IOException("Can't log to File with null
			// buffereredWriter.");
			// this.bufferedWriter = bufferedWriter;
			Logging.this.debugA("Logging to " + logFile + " " + buffy);
		}

		/**
		 * Get the bufferToLog() from the LogRequestMessage. Write it out!
		 * 
		 * @param buffer
		 */
		@Override
		void writeLogMessage(StringBuilder buffer)
		{
			try
			{
				putInBuffer(encoder.encode(CharBuffer.wrap(buffer)));
			}
			catch (CharacterCodingException e)
			{
				e.printStackTrace();
			}

		}

		@Override
		public void close()
		{
			try
			{
				buffy.force();

				// shrink the file to the appropriate size
				int fileSize = 0;

				fileSize = endOfMappedBytes - LOG_FILE_INCREMENT + buffy.position();

				buffy = null;

				channel.close();

				channel = null;

				// do garbage collection to ensure that the file is no longer
				// mapped
				System.runFinalization();
				System.gc();

				debug("final log file size is " + fileSize + " bytes.");

				debug("truncating log.");

				boolean truncated = false;
				int numTries = 0;

				while (!truncated)
				{
					try
					{
						new RandomAccessFile(logFile, "rw").getChannel().truncate(fileSize);

						truncated = true;
					}
					catch (IOException e)
					{
						debug("truncation failed because the file is still mapped, attempting to garbage collect...AGAIN.");

						// do garbage collection to ensure that the file is no
						// longer mapped
						System.runFinalization();
						System.gc();

						numTries++;
					}

					if (numTries == 100)
					{
						debug("Tried to unmap file 100 times; failing now.");

						truncated = true;
					}
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}

		private void putInBuffer(ByteBuffer incoming)
		{

			try
			{
				int remaining = buffy.remaining();

				if (remaining < incoming.remaining())
				{ // we want to write more than will fit; so we just write
					// what we can first...
					debug("not enough space in the buffer: " + remaining + " remaining, "
							+ incoming.remaining() + " needed.");
					debug("last range file range: " + (endOfMappedBytes - LOG_FILE_INCREMENT) + "-"
							+ endOfMappedBytes);
					debug("new range will be: " + (endOfMappedBytes) + "-"
							+ (endOfMappedBytes + LOG_FILE_INCREMENT));

					byte[] temp = new byte[remaining];
					incoming.get(temp, 0, remaining);

					buffy.put(temp);

					// ensure that the buffer has been written out
					buffy.force();

					// then shift buffy to map to the next segment of the file
					buffy = channel.map(MapMode.READ_WRITE, endOfMappedBytes, LOG_FILE_INCREMENT);
					endOfMappedBytes += LOG_FILE_INCREMENT;

					// recursively call on the remainder of incoming
					putInBuffer(incoming);
				}
				else
				{
					if (show(5))
						debug("writing to buffer: " + remaining + "bytes remaining before resize");

					buffy.put(incoming);
				}
			}
			catch (NullPointerException e)
			{
				debug("null pointer; data to be written:");
				try
				{
					debug(Charset.forName("ASCII").newDecoder().decode(incoming).toString());
				}
				catch (CharacterCodingException e1)
				{
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
			catch (CharacterCodingException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * LogWriter that uses a local file for logging.
	 * 
	 * @author andruid
	 * @author Zachary O. Toups (zach@ecologylab.net)
	 */
	protected class FileLogWriter extends LogWriter
	{
		BufferedWriter	bufferedWriter;

		FileLogWriter(File logFile, BufferedWriter bufferedWriter) throws IOException
		{
			if (bufferedWriter == null)
				throw new IOException("Can't log to File with null buffereredWriter.");
			this.bufferedWriter = bufferedWriter;
			Logging.this.debugA("Logging to " + logFile + " " + bufferedWriter);
		}

		/**
		 * Write the opsBuffer to a file.
		 */
		@Override
		void writeLogMessage(StringBuilder buffy)
		{
			try
			{
				bufferedWriter.append(buffy);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		void close()
		{
			try
			{
				bufferedWriter.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			bufferedWriter = null;
		}
	}

	/**
	 * LogWriter that connects to the ServicesServer over the network for logging.
	 * 
	 * @author andruid
	 * @author Zachary O. Toups (zach@ecologylab.net)
	 */
	protected class NetworkLogWriter extends LogWriter
	{
		NIOClient			loggingClient;

		final int			maxMessageLengthChars;

		/** Object for sending a batch of ops to the LoggingServer. */
		final LogOps	logOps;

		/**
		 * The owner of this Logging object, used to ensure that, during shutdown, this object blocks
		 * until it is finished sending; needed to determine the current status.
		 */
		final Logging	loggingParent;

		NetworkLogWriter(NIOClient loggingClient, Logging loggingParent) throws IOException
		{
			if (loggingClient == null)
				throw new IOException("Can't log to Network with null loggingClient.");
			this.loggingClient = loggingClient;
			Logging.this.debug("Logging to service via connection: " + loggingClient);

			// logOps = new LogOps(maxBufferSizeToWrite);
			logOps = new LogOps();

			this.maxMessageLengthChars = loggingClient.getMaxMessageLengthChars();
			this.loggingParent = loggingParent;
		}

		@Override
		public void setPriority(int priority)
		{
			NIOClient loggingClient = this.loggingClient;
			if (loggingClient != null)
				loggingClient.setPriority(priority);
		}

		/**
		 * Writes the given message to the logging server. Recursively calls itself if the message is
		 * too large to send at once.
		 */
		@Override
		void writeLogMessage(LogEvent message)
		{
			try
			{
				if (!this.loggingParent.finished)
				{
					debug(">> sending a normal log message (non-blocking): ");
					try
					{
						debug(SimplTypesScope.serialize(message, StringFormat.XML).toString());
					}
					catch (SIMPLTranslationException e)
					{
						e.printStackTrace();
					}
					loggingClient.nonBlockingSendMessage(message);
				}
				else
				{
					// finishing, wait 50 seconds
					int timeOutMillis = 50000;

					debug(">> network logging finishing, waiting up to " + (timeOutMillis / 1000)
							+ " seconds...");

					ResponseMessage rm = loggingClient.sendMessage(message, timeOutMillis);

					if (rm == null)
						debug("!!! gave up waiting for server response after " + (timeOutMillis / 1000)
								+ " seconds.");
					else
						debug(">>> final log ops sent successfully!");
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (MessageTooLargeException e)
			{
				StringBuilder bufferToLog = message.bufferToLog();
				int half = bufferToLog.length() / 2;

				warning("!!!! attempted to send a message that was too large, splitting in half and trying again recursively ("
						+ bufferToLog.length() + "/2 == " + half + ")");

				LogOps firstHalf = new LogOps();
				firstHalf.setBuffer(new StringBuilder(bufferToLog.subSequence(0, half)));

				LogOps secondHalf = new LogOps();
				secondHalf.setBuffer(new StringBuilder(bufferToLog.subSequence(half, bufferToLog.length())));

				this.writeLogMessage(firstHalf);
				this.writeLogMessage(secondHalf);

				// if (message instanceof SendPrologue)
				// { // if this is a send prologue, send prologue has to happen first
				// message.setBuffer(new StringBuilder(bufferToLog.subSequence(0, half)));
				// this.writeLogMessage(message);
				// }
				// else
				// {
				// this.writeBufferedOps(new StringBuilder(bufferToLog.subSequence(0, half)));
				// }
				//
				// if (message instanceof SendEpilogue)
				// { // if this is a send epilogue, send epilogue has to happen last
				// message.setBuffer(new StringBuilder(bufferToLog.subSequence(half,
				// bufferToLog.length())));
				// this.writeLogMessage(message);
				// }
				// else
				// {
				// this.writeBufferedOps(new StringBuilder(bufferToLog.subSequence(half, bufferToLog
				// .length())));
				// }
			}
		}

		@Override
		void writeBufferedOps(StringBuilder buffy)
		{
			logOps.setBuffer(buffy);

			writeLogMessage(logOps);

			logOps.clear();
			buffy.setLength(0);
		}

		/**
		 * Close the connection to the loggingServer.
		 */
		@Override
		void close()
		{
			debug("close.");

			loggingClient.disconnect();
			loggingClient = null;
		}

		@Override
		void writeLogMessage(StringBuilder xmlBuffy)
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the opSequence
	 */
	public ArrayList<T> getOpSequence()
	{
		return opSequence;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning()
	{
		return running;
	}
}
