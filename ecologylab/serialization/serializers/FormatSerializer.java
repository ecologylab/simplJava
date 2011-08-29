package ecologylab.serialization.serializers;

import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;

public abstract class FormatSerializer
{

	public void serialize(Object object, Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		// method overriden by derived classes to provide serialization functionally relevant to a
		// particular format
	}
}
