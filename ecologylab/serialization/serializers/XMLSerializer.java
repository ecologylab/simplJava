package ecologylab.serialization.serializers;

import java.util.ArrayList;
import java.util.Collection;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;

public class XMLSerializer extends FormatSerializer implements FieldTypes
{

	public XMLSerializer()
	{
	}

	@Override
	public void serialize(Object object, Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = ClassDescriptor
				.getClassDescriptor(object.getClass());


		serialize(object, rootObjectClassDescriptor, null, appendable, translationContext);
	}

	private boolean serialize(Object object,
			ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor,
			FieldDescriptor rootObjectFieldDescriptor, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		ArrayList<? extends FieldDescriptor> attributeFieldDescriptors = rootObjectClassDescriptor
				.attributeFieldDescriptors();

		for (FieldDescriptor childFd : attributeFieldDescriptors)
		{
			try
			{
				writeValueAsAtrribute(object, childFd, appendable);
			}
			catch (Exception ex)
			{
				throw new SIMPLTranslationException("serialize for attribute " + object, ex);
			}
		}

		ArrayList<? extends FieldDescriptor> elementFieldDescriptors = rootObjectClassDescriptor
				.elementFieldDescriptors();

		boolean hasXMLText = rootObjectClassDescriptor.hasScalarFD();
		boolean hasElements = elementFieldDescriptors.size() > 0;

		if (!hasElements && !hasXMLText)
			return true;

		writeClose(appendable);

		if (hasXMLText)
		{
			writeValueAsText(object, rootObjectClassDescriptor.getScalarTextFD());
		}

		for (FieldDescriptor childFd : elementFieldDescriptors)
		{
			switch (childFd.getType())
			{
			case SCALAR:
				writeValueAsLeaf(object, childFd, appendable);
				break;
			case COMPOSITE_ELEMENT:
				Object compositeObject = childFd.getObject(object);
				serialize(compositeObject, appendable, translationContext);
				break;
			case COLLECTION_SCALAR:
			case MAP_SCALAR:
				Collection<?> scalarCollection = XMLTools.getCollection(object);
				writeWrap(childFd, appendable, false);
				for (Object collectionObject : scalarCollection)
				{
					writeValueAsLeaf(collectionObject, childFd, appendable);
				}
				writeWrap(childFd, appendable, true);
				break;
			case COLLECTION_ELEMENT:
			case MAP_ELEMENT:
				Collection<?> compositeCollection = XMLTools.getCollection(object);
				writeWrap(childFd, appendable, false);
				for (Object collectionObject : compositeCollection)
				{
					serialize(collectionObject, appendable, translationContext);
				}
				writeWrap(childFd, appendable, true);
				break;
			}
		}
		return false;
	}

	private void writeWrap(FieldDescriptor childFd, Appendable appendable, boolean close)
	{
		// TODO Auto-generated method stub
	}

	private void writeValueAsLeaf(Object object, FieldDescriptor childFd, Appendable appendable)
	{
		// TODO Auto-generated method stub
	}

	private void writeValueAsText(Object object, FieldDescriptor scalarTextFD)
	{
		// TODO Auto-generated method stub
	}

	private void writeClose(Appendable appendable)
	{
		// TODO Auto-generated method stub
	}

	private void writeValueAsAtrribute(Object object, FieldDescriptor childFd, Appendable appendable)
	{
		// TODO Auto-generated method stub
	}

}
