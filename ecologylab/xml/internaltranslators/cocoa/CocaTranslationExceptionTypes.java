package ecologylab.xml.internaltranslators.cocoa;

/**
 * Types of exceptions that occur during operation of the translation framework.
 * 
 * @author Nabeel Shahzad
 */
public interface CocaTranslationExceptionTypes
{
	static final int	UNKNOWN								= 0;

	static final int	IO_EXCEPTION					= 1;

	static final int	FILE_NOT_FOUND				= 2;

	static final int	NULL_PURL							= 3;

	static final int	UNSUPPORTED_DATATYPE	= 4;
}
