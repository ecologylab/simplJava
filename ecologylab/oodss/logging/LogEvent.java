package ecologylab.oodss.logging;

import java.io.IOException;
import java.io.Writer;

import ecologylab.generic.StringTools;
import ecologylab.oodss.messages.ErrorResponse;
import ecologylab.oodss.messages.OkResponse;
import ecologylab.oodss.messages.RequestMessage;
import ecologylab.oodss.messages.ResponseMessage;
import ecologylab.serialization.Hint;
import ecologylab.serialization.simpl_inherit;

/**
 * Transport for getting log data to the server, without need for any additional translation.
 * 
 * Pre-translated XML fragments are loaded into the internal buffer, and then serialized to a server
 * with this class.
 * 
 * To facilitate logging, this object writes its contents to a Writer (generally a file) during its
 * performService method. The Writer must be configured prior to invoking this method by calling the
 * setWriter method. This sequence is automatically handled by a Logging server in its
 * LoggingContextManager.
 * 
 * @author eunyee
 * @author andruid
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
@simpl_inherit
abstract public class LogEvent extends RequestMessage<LoggingContextScope>
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF_CDATA)
	protected StringBuilder			bufferToLog;

	/** No argument constructor for serialization. */
	public LogEvent()
	{
		super();
	}

	/**
	 * Construct a new LogRequestMessage with a specific buffer size, to prevent unnecessary
	 * allocation later.
	 */
	public LogEvent(int bufferSize)
	{
		bufferToLog = new StringBuilder(bufferSize);
	}

	/**
	 * Save the logging messages to the pre-set writer.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ResponseMessage performService(LoggingContextScope contextScope)
	{
		debug("received logging event");

		Writer outputStreamWriter = contextScope.getOutputStreamWriter();

		if (outputStreamWriter != null)
		{
			try
			{
				final StringBuilder bufferToLog = bufferToLog();

				debug(bufferToLog);

				outputStreamWriter.append(bufferToLog);
				clear();

				return OkResponse.get();
			}
			catch (IOException e)
			{
				e.printStackTrace();

				return new ErrorResponse(e.getMessage());
			}
		}
		else
		{
			error("can't log because there is no outputStreamWriter; was there a prologue?");

			return new ErrorResponse(
					"can't log because there is no outputStreamWriter; was there a prologue?");
		}
	}

	public void setBuffer(StringBuilder bufferToLog)
	{
		clear();
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
		if (bufferToLog != null)
		{
			StringTools.clear(bufferToLog);
			bufferToLog = null;
		}
	}
}