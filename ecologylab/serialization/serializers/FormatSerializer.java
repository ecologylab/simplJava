package ecologylab.serialization.serializers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;

public abstract class FormatSerializer
{

	public String serialize(Object object, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{
		final StringBuilder sb = new StringBuilder();
		serialize(object, sb, translationContext);
		return sb.toString();
	}

	public void serialize(Object object, final StringBuilder stringBuilder,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		OutputStream outputStream = new OutputStream()
		{
			@Override
			public void write(int b) throws IOException
			{
				stringBuilder.append((char) b);
			}
		};

		serialize(object, new PrintStream(outputStream), translationContext);
	}

	public void serialize(Object object, Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{
		// method overriden by derived classes to provide serialization functionally relevant to a
		// particular format
	}
}