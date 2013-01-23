package ecologylab.translators.net;

import ecologylab.translators.CodeTranslationException;

public class DotNetTranslationException extends CodeTranslationException
{

	private static final long	serialVersionUID	= -4177043416527275728L;
	
	private int								exceptionType			= 0;

	public DotNetTranslationException()
	{
		super();
	}

	public DotNetTranslationException(String msg)
	{
		super(msg);
	}

	public DotNetTranslationException(String msg, Exception e)
	{
		super(msg, e);
	}

	public DotNetTranslationException(int exceptionType)
	{
		super(exceptionType);
	}

	public DotNetTranslationException(String msg, int exceptionType)
	{
		super(msg, exceptionType);
	}

	public DotNetTranslationException(String msg, Exception e, int exceptionType)
	{
		super(msg, e, exceptionType);
	}

}
