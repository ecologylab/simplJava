package ecologylab.serialization.serializers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.TranslationScope.GRAPH_SWITCH;

/**
 * FormatSerializer. an abstract base class from where format-specific serializers derive. Its main use is for exposing
 * the API for serialization methods. It contains helper functions and wrapper serialization functions,
 * allowing software developers to use different types of objects for serialization, such as
 * System.out, File, StringBuilder, or return serialized data as StringBuilder
 * 
 * @author nabeel
 *  
 */
public abstract class FormatSerializer
{
	/**
	 * 
	 * @param object
	 * @param outputFile
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public void serialize(Object object, File outputFile) throws SIMPLTranslationException,
			IOException
	{
		XMLTools.createParentDirs(outputFile);

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		serialize(object, bufferedWriter, new TranslationContext(outputFile.getParentFile()));
		bufferedWriter.close();
	}

	/**
	 * 
	 * @param object
	 * @param outputFile
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public void serialize(Object object, File outputFile, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{
		XMLTools.createParentDirs(outputFile);

		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFile));
		serialize(object, bufferedWriter, translationContext);
		bufferedWriter.close();
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public StringBuilder serialize(Object object) throws SIMPLTranslationException, IOException
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
			throws SIMPLTranslationException, IOException
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

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public void serialize(Object object, Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{
		// method overriden by derived classes to provide serialization functionally relevant to a
		// particular format
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	protected ClassDescriptor<? extends FieldDescriptor> getClassDescriptor(Object object)
	{
		return ClassDescriptor.getClassDescriptor(object.getClass());
	}

	/**
	 * 
	 * @param object
	 */
	protected void serializationPostHook(Object object)
	{
		if (object instanceof ISimplSerializationPost)
		{
			((ISimplSerializationPost) object).serializationPostHook();
		}
	}

	/**
	 * 
	 * @param object
	 */
	protected void serializationPreHook(Object object)
	{
		if (object instanceof ISimplSerializationPre)
		{
			((ISimplSerializationPre) object).serializationPreHook();
		}
	}

	/**
	 * 
	 * @param object
	 * @param translationContext
	 * @return
	 */
	protected boolean alreadySerialized(Object object, TranslationContext translationContext)
	{
		return TranslationScope.graphSwitch == GRAPH_SWITCH.ON
				&& translationContext.alreadyMarshalled(object);
	}
}