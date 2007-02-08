package ecologylab.services.logging;

import ecologylab.xml.XmlTranslationException;
import ecologylab.xml.xml_inherit;

/**
 * Allows the application to send application-specific content to the log, at the end of a session.
 * <p/>
 * NB: this class should *never* be extended in an application specific way, because the LoggingServer should never
 * need to know the TranslationSpace for such a super class.
 * What you do extend is the {@link Epilogue Epilogue} object.
 * 
 * @author andruid
 * @author eunyee
 */
@xml_inherit
public final class SendEpilogue extends LogueMessage
{
	@xml_nested Epilogue		epilogue;
	
	public SendEpilogue(Logging logging, Epilogue epilogue)
	{
		super(logging);
		this.epilogue	= epilogue;
	}

	public SendEpilogue()
	{
		super();
	}

	protected String getMessageString()
	{
		try
		{
			return (Logging.OP_SEQUENCE_END + super.getMessageString() + endLog() );
		} catch (XmlTranslationException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public String endLog()
 	{
 		return "</" + logName() + ">";
 	}
}
