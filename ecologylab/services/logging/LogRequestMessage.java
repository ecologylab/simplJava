package ecologylab.services.logging;

import ecologylab.xml.xml_inherit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import ecologylab.collections.Scope;
import ecologylab.generic.Debug;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.RequestMessage;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.XMLTranslationException;
import ecologylab.xml.ElementState.xml_leaf;

/**
 * Transport for getting log data to the server, without need for any additional translation.
 * 
 * Pre-translated XML fragments are loaded into the internal buffer, and then serialized to a server with this class.
 * 
 * To facilitate logging, this object writes its contents to a Writer (generally a file) during its performService
 * method. The Writer must be configured prior to invoking this method by calling the setWriter method. This sequence is
 * automatically handled by a Logging server in its LoggingContextManager.
 * 
 * @author eunyee
 * @author andruid
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
@xml_inherit abstract public class LogRequestMessage extends RequestMessage
{
	@xml_leaf(CDATA) protected StringBuilder	bufferToLog;

	Writer												outputStreamWriter;

	/** No argument constructor for serialization. */
	public LogRequestMessage()
	{
		super();
	}

	/** Construct a new LogRequestMessage with a specific buffer size, to prevent unnecessary allocation later. */
	public LogRequestMessage(int bufferSize)
	{
		bufferToLog = new StringBuilder(bufferSize);
	}

	/**
	 * Save the logging messages to the pre-set writer.
	 */
	@Override public ResponseMessage performService(Scope objectRegistry, String sessionId)
	{
		debug("services: received Logging Messages ");

		if (outputStreamWriter != null)
		{
			try
			{
				final StringBuilder bufferToLog = bufferToLog();

				debug(bufferToLog);

				outputStreamWriter.append(bufferToLog);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
			error("Can't log because there is no outputStreamWriter.");

		debug("services: sending OK response");

		return OkResponse.get();

	}

	/**
	 * Sets the writer that will be used to record this object's buffer's contents during the performService method.
	 * 
	 * @param outputStreamWriter
	 */
	public void setWriter(OutputStreamWriter outputStreamWriter)
	{
		this.outputStreamWriter = outputStreamWriter;
	}

	/**
	 * Loads pre-translated XML fragments into this object's buffer for later sending over the network.
	 * 
	 * @param opsBuffer
	 */
	public void appendToBuffer(StringBuilder opsBuffer)
	{
		bufferToLog.append(opsBuffer);
	}

	public void setBuffer(StringBuilder bufferToLog)
	{
		this.bufferToLog = bufferToLog;
	}

	/**
	 * Stuff to write to the log file based on the contents of this message.
	 * 
	 * @return ops, the buffer in which MixedInitiativeOps have been passed here.
	 */
	protected StringBuilder bufferToLog()
	{
		return bufferToLog;
	}

	/**
	 * Clear the buffer for re-use, presumably after sending it.
	 */
	public void clear()
	{
		bufferToLog = null;
	}
}