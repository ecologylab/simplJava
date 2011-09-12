package ecologylab.serialization.serializers.stringformats;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.serializers.FormatSerializer;

public abstract class StringSerializer extends FormatSerializer
{
	/**
	 * 
	 * @param object
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public StringBuilder serialize(Object object) throws SIMPLTranslationException
	{
		return serialize(object, new TranslationContext());
	}

	/**
	 * 
	 * @param object
	 * @param translationContext
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public StringBuilder serialize(Object object, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		final StringBuilder sb = new StringBuilder();
		serialize(object, sb, translationContext);
		return sb;
	}

	/**
	 * 
	 * @param object
	 * @param stringBuilder
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public void serialize(Object object, final StringBuilder stringBuilder,
			TranslationContext translationContext) throws SIMPLTranslationException
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

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public abstract void serialize(Object object, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException;
}
