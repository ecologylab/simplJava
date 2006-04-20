package ecologylab.services.logging;

import ecologylab.xml.XmlTranslationException;

public final class SendEpilogue extends LogueMessage
{
	public Epilogue		epilogue;
	
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
