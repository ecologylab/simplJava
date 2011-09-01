package ecologylab.serialization.serializers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.TranslationScope.GRAPH_SWITCH;

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

	protected ClassDescriptor<? extends FieldDescriptor> getClassDescriptor(Object object)
	{
		return ClassDescriptor.getClassDescriptor(object.getClass());
	}

	protected void serializationPostHook(Object object)
	{
		if (object instanceof ISimplSerializationPost)
		{
			((ISimplSerializationPost) object).serializationPostHook();
		}
	}

	protected void serializationPreHook(Object object)
	{
		if (object instanceof ISimplSerializationPre)
		{
			((ISimplSerializationPre) object).serializationPreHook();
		}
	}
	
	protected boolean alreadySerialized(Object object, TranslationContext translationContext)
	{
		return TranslationScope.graphSwitch == GRAPH_SWITCH.ON
				&& translationContext.alreadyMarshalled(object);
	}
}