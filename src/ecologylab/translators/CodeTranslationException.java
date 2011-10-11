package ecologylab.translators;

import ecologylab.generic.Debug;
import ecologylab.net.ParsedURL;

/**
 * 
 * @author quyin
 *
 */
public class CodeTranslationException extends Exception implements CodeTranslationExceptionTypes
{

	private static final long	serialVersionUID	= -4500817123206529069L;

	private int								exceptionType			= 0;
	
	public CodeTranslationException()
	{
		super();
	}

	public CodeTranslationException(String msg)
	{
		super(msg);
	}

	public CodeTranslationException(String msg, Exception e)
	{
		super(String.format("%s: %s\nMessage: %s\n", e.getClass().getSimpleName(), e.toString(), msg));
		e.printStackTrace();
	}

	public CodeTranslationException(int exceptionType)
	{
		this();

		this.exceptionType = exceptionType;
	}

	public CodeTranslationException(String msg, int exceptionType)
	{
		this(msg);

		this.exceptionType = exceptionType;
	}

	public CodeTranslationException(String msg, Exception e, int exceptionType)
	{
		this(msg, e);

		this.exceptionType = exceptionType;
	}

	/**
	 * @return The exception type code.
	 * @see {@link CodeTranslationExceptionTypes}
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
			that.warning("File not found: " + msg + "; Message: " + purlMsg);
			break;
		case NULL_PURL:
			Debug.weird(that, "\tCan't open " + msg + " - ParsedURL is null");
			break;
		case UNSUPPORTED_DATATYPE:
			Debug.weird(this, "Unsupported data type.");
		default:
			this.printStackTrace();
			break;
		}
	}
	
}
