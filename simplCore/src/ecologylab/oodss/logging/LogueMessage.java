package ecologylab.oodss.logging;

import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Base class for SendPrologue and SendEpilogue. Probably should not be used for anything else. Enables passing of the
 * logName, in order to write custom log XML element open and close tags.
 * 
 * @author andruid
 */
@simpl_inherit abstract public class LogueMessage extends LogEvent
{
	@simpl_scalar protected String	logName;

	/**
	 * Constructor for building from the Logging class.
	 * 
	 * @param logging
	 */
	public LogueMessage(Logging logging)
	{
		logName = XMLTools.getXmlTagName(logging.getClass(), "State");
	}

	/**
	 * Constructor for automatic translation;
	 */
	public LogueMessage()
	{
		super();
	}

	public String logName()
	{
		return (logName != null) ? logName : "logging";
	}

}
