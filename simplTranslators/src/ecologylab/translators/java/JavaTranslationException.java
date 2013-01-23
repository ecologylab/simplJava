package ecologylab.translators.java;

import ecologylab.translators.CodeTranslationException;

public class JavaTranslationException extends CodeTranslationException
{
	
	private static final long	serialVersionUID	= -8326348358064487418L;

	public JavaTranslationException()
	{
		super();
	}

	public JavaTranslationException(String msg)
	{
		super(msg);
	}

	public JavaTranslationException(String msg, Exception e)
	{
		super(msg, e);
	}

	public JavaTranslationException(int exceptionType)
	{
		super(exceptionType);
	}

	public JavaTranslationException(String msg, int exceptionType)
	{
		super(msg, exceptionType);
	}

	public JavaTranslationException(String msg, Exception e, int exceptionType)
	{
		super(msg, e, exceptionType);
	}

}

