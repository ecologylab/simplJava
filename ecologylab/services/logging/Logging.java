package ecologylab.services.logging;

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

import ecologylab.appframework.Memory;
import ecologylab.appframework.ObjectRegistry;
import ecologylab.appframework.PropertiesAndDirectories;
import ecologylab.appframework.types.prefs.Pref;
import ecologylab.generic.Generic;
import ecologylab.io.Files;
import ecologylab.services.ServicesHostsAndPorts;
import ecologylab.services.distributed.client.NIOClient;
import ecologylab.services.messages.DefaultServicesTranslations;
import ecologylab.xml.ElementState;
import ecologylab.xml.XMLTools;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Provides a framework for interaction logging. Uses ecologylab.xml to serialize user and agent actions, and write them
 * either to a file on the user's local machine, or, across the network, to the LoggingServer.
 * 
 * @author andruid
 */
public class Logging<T extends MixedInitiativeOp> extends ElementState implements Runnable, ServicesHostsAndPorts
{
	/**
	 * This field is used for reading a log in from a file, but not for writing one, because we dont the write the log
	 * file all at once, and so can't automatically translate the start tag and end tag for this element.
	 */
	@xml_nested protected ArrayListState<T>	opSequence;

	Thread												thread;

	/**
	 * Does all the work of logging, if there is any work to be done. If this is null, then there is no logging;
	 * conversely, if there is no longging, this is null.
	 */
	ArrayList<LogWriter>								logWriters									= null;

	/**
	 * This is the Vector for the operations that are being queued up before they can go to outgoingOps.
	 */
	private StringBuilder							incomingOpsBuffer;

	/**
	 * This is the Vector for the operations that are in the process of being written out.
	 */
	private StringBuilder							outgoingOpsBuffer;

	/** Stores the pointer to outgoingOpsBuffer for swapQueues. */
	private StringBuilder							tempOpsBuffer;

	boolean												finished;

	static final int									THREAD_PRIORITY							= 1;

	/** Amount of time for writer thread to sleep; 15 seconds */
	static final int									SLEEP_TIME									= 15000;

	static final long									sessionStartTime							= System.currentTimeMillis();

	long													lastGcTime;

	/** Amount of time to wait before booting the garbage collector; 5 minutes */
	static final long									KICK_GC_INTERVAL							= 300000;

	private static final String					SESSION_LOG_START							= "\n<session_log>\n ";

	static final String								OP_SEQUENCE_START							= "\n\n<op_sequence>\n\n";

	static final String								OP_SEQUENCE_END							= "\n</op_sequence>\n";

	/** Logging closing message string written to the logging file at the end */
	public static final String						LOG_CLOSING									= "\n</op_sequence></session_log>\n\n";

	/** Logging Header message string written to the logging file in the begining */
	static final String								BEGIN_EMIT									= XMLTools.xmlHeader()
																													+ SESSION_LOG_START;

	/** Preference setting for no logging. */
	public static final int							NO_LOGGING									= 0;

	/** Preference setting for logging to a file using normal IO. */
	public static final int							LOG_TO_FILE									= 1;

	/** Preference setting for logging to a remote server. */
	public static final int							LOG_TO_SERVICES_SERVER					= 2;

	/** Preference setting for logging to a file using memory-mapped IO. */
	public static final int							LOG_TO_MEMORY_MAPPED_FILE				= 4;

	/** Preference setting for logging both to a memory-mapped file and a server. */
	public static final int							LOG_TO_MM_FILE_AND_SERVER_REDUNDANT	= LOG_TO_MEMORY_MAPPED_FILE
																													& LOG_TO_SERVICES_SERVER;

	/** Preference setting for logging both to a normal IO file and a server. */
	public static final int							LOG_TO_FILE_AND_SERVER_REDUNDANT		= LOG_TO_FILE & LOG_TO_SERVICES_SERVER;

	static final int									MAX_OPS_BEFORE_WRITE						= 10;

	public static final String						LOGGING_HOST_PARAM						= "logging_host";

	public static final String						LOGGING_PORT_PARAM						= "logging_port";

	public static final String						LOGGING_MODE_PARAM						= "log_mode";

	final int											maxOpsBeforeWrite;

	final int											maxBufferSizeToWrite;

	/** used to prevent writes from getting interrupt()'ed */
	private Object										threadSemaphore							= new Object();

	/**
	 * Instantiates a Logging object based on the given log file name. This constructor assumes that a set of loaded
	 * {@link ecologylab.appframework.types.prefs.Pref Pref}s will handle other settings, indicating how logging will be
	 * performed, and the server setup (if logging over a network).
	 */
	public Logging(String logFileName)
	{
		this(logFileName, MAX_OPS_BEFORE_WRITE);
	}

	/**
	 * Instantiates a Logging object based on the given log file name and the maximum operations before write. This
	 * constructor assumes that a set of loaded {@link ecologylab.appframework.types.prefs.Pref Pref}s will handle other
	 * settings, indicating how logging will be performed, and the server setup (if logging over a network).
	 */
	public Logging(String logFileName, int maxOpsBeforeWrite)
	{
		this(logFileName, maxOpsBeforeWrite, Pref.lookupInt(LOGGING_MODE_PARAM, NO_LOGGING), Pref.lookupString(
				LOGGING_HOST_PARAM, "localhost"), Pref.lookupInt(LOGGING_PORT_PARAM, ServicesHostsAndPorts.LOGGING_PORT));
	}

	/**
	 * Instantiates a Logging object based on the supplied parameters. This constructor does not rely on
	 * {@link ecologylab.appframework.types.prefs.Pref Pref}s.
	 * 
	 * @param logFileName
	 *           the name of the file to which the log will be written.
	 * @param maxOpsBeforeWrite
	 *           the maximum number of ops to record in memory before writing them to the set media.
	 * @param logMode
	 *           the media to which the logger will write, such as a memory-mapped file or a server.
	 * @param loggingHost
	 *           the host to which to log if using networked logging (may be null if local logging is desired).
	 * @param loggingPort
	 *           the port of the host to which to log if using networked logging (may be 0 if local logging is desired).
	 */
	public Logging(String logFileName, int maxOpsBeforeWrite, int logMode, String loggingHost, int loggingPort)
	{
		this(maxOpsBeforeWrite);
		finished = false;

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
					File logDir = PropertiesAndDirectories.logDir();
					if (logDir == null)
					{
						debug("Can't write to logDir=" + logDir);
					}
					else
					{
						File logFile = new File(logDir, logFileName);
						debug("Logging to file: " + logFile.getAbsolutePath());

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
					File logDir = PropertiesAndDirectories.logDir();

					if (logDir == null)
					{
						debug("Can't write to logDir=" + logDir);
					}
					else
					{
						File logFile = new File(logDir, logFileName);
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

				NIOClient loggingClient = new NIOClient(loggingHost, loggingPort, DefaultServicesTranslations.get(),
						new ObjectRegistry());

				debug("**************************************************************connecting to server.");

				// CONNECT TO SERVER
				if (loggingClient.connect())
				{
					try
					{
						logWriters.add(new NetworkLogWriter(loggingClient));
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}

					debug("logging to server: " + loggingHost + ":" + loggingPort);
				}
				else
				{
					loggingClient = null;
					debug("Logging disabled: cannot reach server");
				}
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
	 * Translates op to XML then logs it.
	 * 
	 * @param op -
	 *           the operation to be logged.
	 */
	public void logAction(MixedInitiativeOp op)
	{
		if (logWriters != null)
		{
			try
			{
				if (logWriters != null)
				{
					synchronized (incomingOpsBuffer)
					{
						op.translateToXML(incomingOpsBuffer);
					}

					final int bufferLength = incomingOpsBuffer.length();
					if ((thread != null) && (bufferLength > maxBufferSizeToWrite))
					{
						synchronized (threadSemaphore)
						{
							debugA("interrupting thread to do i/o now: " + bufferLength + "/" + maxOpsBeforeWrite);
							thread.interrupt(); // end sleep in that thread prematurely
							// to
							// do i/o
						}
					}
				}
			}
			catch (XMLTranslationException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Returns the size of the list of log ops. May not be the correct value if called during logging. This method should
	 * only be used for playback purposes.
	 * 
	 * @return the size of opSequence.
	 */
	public int size()
	{
		return this.opSequence.size();
	}

	/**
	 * Write the start of the log header out to the log file OR, send the begining logging file message so that logging
	 * server write the start of the log header. <p/> Then start the looping thread that periodically wakes up and
	 * performs log i/o.
	 */
	public void start()
	{
		if ((logWriters != null) && (thread == null))
		{
			final Prologue prologue = getPrologue();
			String uid = Pref.lookupString("uid", "0");
			Logging.this.debug("Logging: Sending Prologue userID:" + uid);
			prologue.setUserID(uid);

			// necessary for acquiring wrapper characters, like <op_sequence>
			final SendPrologue sendPrologue = new SendPrologue(Logging.this, prologue);

			for (LogWriter logWriter : logWriters)
			{
				logWriter.writePrologue(sendPrologue);
			}

			thread = new Thread(this);
			thread.setPriority(THREAD_PRIORITY);
			thread.start();
		}
	}

	/**
	 * Finishes writing any queued actions, then sends the epilogue; then shuts down.
	 * 
	 */
	public synchronized void stop()
	{
		if (thread != null)
		{
			finished = true;
			thread = null;
			if (logWriters != null)
			{
				writeBufferedOps();

				final Epilogue epilogue = getEpilogue();
				// necessary for acquiring wrapper characters, like </op_sequence>
				final SendEpilogue sendEpilogue = new SendEpilogue(this, epilogue);
				for (LogWriter logWriter : logWriters)
				{
					logWriter.writeLogMessage(sendEpilogue);
					logWriter.close();
				}
				logWriters = null;
			}
		}
	}

	/**
	 * Logging to a file is delayed to the actions of this thread, because otherwise, it can mess up priorities in the
	 * system, because events get logged in the highest priority thread. <p/> This MUST be the only thread that ever
	 * calls writeQueuedActions().
	 */
	public void run()
	{
		lastGcTime = System.currentTimeMillis();
		while (!finished)
		{
			Thread.interrupted();
			Generic.sleep(SLEEP_TIME, false);

			synchronized (threadSemaphore)
			{
				writeBufferedOps();
			}

			long now = System.currentTimeMillis();
			long deltaT = now - lastGcTime;

			if (deltaT >= KICK_GC_INTERVAL)
			{
				debug("kick GC");
				lastGcTime = now;
				Memory.reclaim();
			}
		}
	}

	/**
	 * Use the LogWriter, if there is one, to output queued actions to the log. <p/> NB: This method is SINGLE Threaded!
	 * It is not thread safe. It must only be called from the run() method.
	 */
	protected void writeBufferedOps()
	{
		if (logWriters == null)
		{
			weird("attempting to run logging without a log writer; disabling logging.");
			this.stop();
			return;
		}

		StringBuilder bufferToWrite = incomingOpsBuffer;
		synchronized (bufferToWrite)
		{
			int size = bufferToWrite.length();

			if (size == 0)
				return;
			swapBuffers();
			// what was incomingOps is now outgoing!
			// String firstEntry = ourQueueToWrite.get(0);

			// debug("Logging: writeQueuedActions() start of output loop.");

			for (LogWriter logWriter : logWriters)
			{
				logWriter.writeBufferedOps(bufferToWrite);
			}

			/*
			 * if (size == 1) { logWriter.consumeOp(firstEntry); } else { // allocate storage with a reasonable size
			 * estimate logWriter.consumeOp(firstEntry); for (int i = 1; i < size; i++) { String thatEntry =
			 * ourQueueToWrite.get(i); logWriter.consumeOp(thatEntry); } }
			 */

			// debug("Logging: writeQueuedActions() after output loop.");
			bufferToWrite.setLength(0);
		}

	}

	/**
	 * A message at the beginnging of the log. This method may be overridden to return a subclass of Prologue, by
	 * subclasses of this, that wish to emit application specific information at the start of a log.
	 * 
	 * @return
	 */
	protected Prologue getPrologue()
	{
		return new Prologue();
	}

	/**
	 * A message at the end of the log. This method may be overridden to return a subclass of Epilogue, by subclasses of
	 * this, that wish to emit application specific information at the end of a log.
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
	 * Objects that process queuedActions for writing, either to a local file, or, using the network, to the
	 * LoggingServer.
	 * 
	 * @author andruid
	 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
	 */
	protected abstract class LogWriter
	{
		LogWriter() throws IOException
		{

		}

		/**
		 * Get the bufferToLog() from the LogRequestMessage. Write it out!
		 * 
		 * @param logRequestMessage
		 */
		void writeLogMessage(LogRequestMessage logRequestMessage)
		{
			StringBuilder buffy = logRequestMessage.bufferToLog();
			writeLogMessage(buffy);

		}

		abstract void writeLogMessage(StringBuilder xmlBuffy);

		void writePrologue(SendPrologue sendPrologue)
		{
			writeLogMessage(sendPrologue);
		}

		void writeBufferedOps(StringBuilder opsBuffer)
		{
			writeLogMessage(opsBuffer);
		}

		abstract void close();
	}

	/**
	 * LogWriter that uses a memory-mapped local file for logging.
	 * 
	 * @author andruid
	 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
	 */
	protected class MemoryMappedFileLogWriter extends LogWriter
	{
		// BufferedWriter bufferedWriter;
		MappedByteBuffer			buffy						= null;

		FileChannel					channel					= null;

		private CharsetEncoder	encoder					= Charset.forName("ASCII").newEncoder();

		private File				logFile;

		/**
		 * The base size for the log file, and the amount it will be incremented whenever its buffer overflows.
		 */
		static final int			LOG_FILE_INCREMENT	= 1024 * 512;

		private int					endOfMappedBytes		= LOG_FILE_INCREMENT;

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
		@Override void writeLogMessage(StringBuilder buffer)
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

		@Override public void close()
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

					if (numTries == 10)
					{
						debug("Tried to unmap file 10 times; failing now.");

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
					debug("not enough space in the buffer: " + remaining + " remaining, " + incoming.remaining()
							+ " needed.");
					debug("last range file range: " + (endOfMappedBytes - LOG_FILE_INCREMENT) + "-" + endOfMappedBytes);
					debug("new range will be: " + (endOfMappedBytes) + "-" + (endOfMappedBytes + LOG_FILE_INCREMENT));

					byte[] temp = new byte[remaining];
					incoming.get(temp, incoming.position(), remaining);

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
	 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
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
		@Override void writeLogMessage(StringBuilder buffy)
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

		@Override void close()
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
	 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
	 */
	protected class NetworkLogWriter extends LogWriter
	{
		NIOClient		loggingClient;

		/** Object for sending a batch of ops to the LoggingServer. */
		final LogOps	logOps;

		NetworkLogWriter(NIOClient loggingClient) throws IOException
		{
			if (loggingClient == null)
				throw new IOException("Can't log to Network with null loggingClient.");
			this.loggingClient = loggingClient;
			Logging.this.debug("Logging to service via connection: " + loggingClient);

			// logOps = new LogOps(maxBufferSizeToWrite);
			logOps = new LogOps();
		}

		/**
		 * Write the prologue -- special stuff at the beginning of a session.
		 * 
		 * @param prologue
		 */
		@Override void writePrologue(SendPrologue sendPrologue)
		{
			debug("logging client writing prologue");
			writeLogMessage(sendPrologue);
		}

		@Override void writeLogMessage(LogRequestMessage message)
		{
			try
			{
				loggingClient.nonBlockingSendMessage(message);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}

		@Override void writeBufferedOps(StringBuilder buffy)
		{
			logOps.setBuffer(buffy);
			// logOps.appendToBuffer(buffy);
			try
			{
				loggingClient.nonBlockingSendMessage(logOps);

				logOps.clear();
				buffy.setLength(0);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		/**
		 * Close the connection to the loggingServer.
		 */
		@Override void close()
		{
			debug("close.");

			loggingClient.disconnect();
			loggingClient = null;
		}

		@Override void writeLogMessage(StringBuilder xmlBuffy)
		{
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * @return the opSequence
	 */
	public ArrayListState<T> getOpSequence()
	{
		return opSequence;
	}
}
