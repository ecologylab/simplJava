package ecologylab.services.logging;

import java.io.IOException;
import java.io.Writer;

import ecologylab.collections.Scope;
import ecologylab.services.messages.ErrorResponse;
import ecologylab.services.messages.OkResponse;
import ecologylab.services.messages.ResponseMessage;
import ecologylab.xml.SIMPLTranslationException;
import ecologylab.xml.simpl_inherit;

/**
 * Allows the application to send application-specific content to the log, at
 * the end of a session. <p/> NB: this class should *never* be extended in an
 * application specific way, because the LoggingServer should never need to know
 * the TranslationSpace for such a super class. What you do extend is the
 * {@link Epilogue Epilogue} object.
 * 
 * @author andruid
 * @author eunyee
 */
@simpl_inherit public final class SendEpilogue extends LogueMessage
{
	public SendEpilogue(Logging logging, Epilogue epilogue)
	{
		super(logging);
		try
		{
			bufferToLog = epilogue.serialize((StringBuilder) null);
			bufferToLog.insert(0, Logging.OP_SEQUENCE_END);
			bufferToLog.append(endLog());
		}
		catch (SIMPLTranslationException e)
		{
			e.printStackTrace();
		}
	}

	public SendEpilogue()
	{
		super();
	}

	public String endLog()
	{
		return "</" + logName() + ">";
	}

	@Override public ResponseMessage performService(LoggingContextScope contextScope)
	{
		debug("received epiliogue");

		// let the superclass handle writing any epilogue data
		ResponseMessage msg = super.performService(contextScope);

		// get the stream to shut it down
		if (msg.isOK())
		{
			Writer outputStreamWriter = contextScope.getOutputStreamWriter();

			if (outputStreamWriter != null)
			{
				try
				{
					outputStreamWriter.flush();
					outputStreamWriter.close();

					return OkResponse.get();
				}
				catch (IOException e)
				{
					e.printStackTrace();

					return new ErrorResponse(e.getMessage());
				}
				finally
				{
					// remove the output stream from the scope
					contextScope.shutdown();
				}
			}
			else
			{
				error("can't log because there is no outputStreamWriter; was there a prologue?");

				return new ErrorResponse(
						"can't log because there is no outputStreamWriter; was there a prologue?");
			}
		}
		else
		{
			return msg;
		}
	}
}
