package ecologylab.serialization.serializers;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.TranslationContext;

public class XMLSerializer extends FormatSerializer
{

	public void serialize(Object object, Appendable appendable,
			TranslationContext translationContext)
	{
			ClassDescriptor rootObjectClassDescriptor = ClassDescriptor.getClassDescriptor(object.getClass());
	}
}
