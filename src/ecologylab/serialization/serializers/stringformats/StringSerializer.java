package ecologylab.serialization.serializers.stringformats;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationContextPool;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.serializers.FormatSerializer;

public abstract class StringSerializer extends FormatSerializer
{

	@Override
	public void serialize(Object object, OutputStream outputStream,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
//		serialize(object, (Appendable) new PrintStream(outputStream), translationContext);
		try
		{
			serialize(object, (Appendable) new PrintStream(outputStream, true, "utf-8"), translationContext);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void serialize(Object object, File outputFile, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		try
		{
			XMLTools.createParentDirs(outputFile);

			if (outputFile.getParentFile() != null)
				translationContext.setBaseDirFile(outputFile.getParentFile());

			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
			serialize(object, bufferedWriter, translationContext);

			bufferedWriter.close();
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IO Exception: ", e);
		}
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public StringBuilder serialize(Object object) throws SIMPLTranslationException
	{
		TranslationContext translationContext = TranslationContextPool.get().acquire();
		StringBuilder sb = serialize(object, translationContext);
		TranslationContextPool.get().release(translationContext);
		return sb;
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

		serialize(object, (Appendable) new PrintStream(outputStream), translationContext);
	}

	/**
	 * All methods will eventually call this method which is overridden by derived classes
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
