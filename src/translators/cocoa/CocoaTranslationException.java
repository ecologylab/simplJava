package ecologylab.translators.cocoa;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;

/**
 * Exception handling class for translation.
 * 
 * @author Nabeel Shahzad
 */
public class CocoaTranslationException extends Exception implements CocoaTranslationExceptionTypes
{
	private static final long	serialVersionUID	= -8326348358064487418L;

	private int								exceptionType			= 0;

	public CocoaTranslationException()
	{
		super();
	}

	public CocoaTranslationException(String msg)
	{
		super(msg);
	}

	public CocoaTranslationException(String msg, Exception e)
	{
		super("CocoaTranslationException\n" + msg + "\n\tThe error is " + e.toString() + " in" + "\n\t"
				+ e.getStackTrace()[0] + "\n\t" + e.getStackTrace()[1] + "\n\t" + e.getStackTrace()[2]
				+ "\n\t" + e.getStackTrace()[3] + "\n\t");
	}

	public CocoaTranslationException(int exceptionType)
	{
		super();

		this.exceptionType = exceptionType;
	}

	public CocoaTranslationException(String msg, int exceptionType)
	{
		this(msg);

		this.exceptionType = exceptionType;
	}

	public CocoaTranslationException(String msg, Exception e, int exceptionType)
	{
		this(msg, e);

		this.exceptionType = exceptionType;
	}

	/**
	 * Returns the type of exception that generated the XmlTranslationException. These can be
	 * referenced from the interface XmlTranslationExceptionTypes.
	 * 
	 * @return
	 */
	public int getExceptionType()
	{
		return exceptionType;
	}

	public void printTraceOrMessage(Debug that, String msg, ParsedURL purl)
	{
		switch (getExceptionType())
		{
		case FILE_NOT_FOUND:
			String purlMsg = (purl == null) ? "" : purl.toString();
			that.warning("File not found - " + msg + " - " + purlMsg);
			break;
		case NULL_PURL:
			Debug.weird(that, "\tCan't open " + msg + " - ParsedURL is null");
			break;
		case UNSUPPORTED_DATATYPE:
			Debug
					.weird(this,
							"The datatype supplies is not supported by the cocoa.internaltranslator and ecologylab.serialization");
		default:
			this.printStackTrace();
			break;
		}
	}
}
