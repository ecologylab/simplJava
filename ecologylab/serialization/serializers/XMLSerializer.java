package ecologylab.serialization.serializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.TranslationScope.GRAPH_SWITCH;

public class XMLSerializer extends FormatSerializer implements FieldTypes
{
	private static final String	START_CDATA			= "<![CDATA[";

	private static final String	END_CDATA				= "]]>";

	private static final String	SIMPL_NAMESPACE	= " xmlns:simpl=\"http://ecologylab.net/research/simplGuide/serialization/index.html\"";

	private boolean							isRoot					= true;

	private static final String	SIMPL_REF				= "simpl:ref";

	private static final String	SIMPL_ID				= "simpl:id";

	public XMLSerializer()
	{
	}

	@Override
	public void serialize(Object object, Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{
		translationContext.resolveGraph(object);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = ClassDescriptor
				.getClassDescriptor(object.getClass());

		serialize(object, rootObjectClassDescriptor.pseudoFieldDescriptor(), appendable,
				translationContext);
	}

	private void serialize(Object object, FieldDescriptor rootObjectFieldDescriptor,
			Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{

		if (alreadySerialized(object, translationContext))
		{
			writeSimplRef(object, rootObjectFieldDescriptor, appendable);
			return;
		}

		translationContext.mapObject(object);

		serializationPreHook(object);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = getClassDescriptor(object);

		writeObjectStart(rootObjectFieldDescriptor, appendable);

		serializeAttributes(object, appendable, translationContext, rootObjectClassDescriptor);

		ArrayList<? extends FieldDescriptor> elementFieldDescriptors = rootObjectClassDescriptor
				.elementFieldDescriptors();

		boolean hasXMLText = rootObjectClassDescriptor.hasScalarFD();
		boolean hasElements = elementFieldDescriptors.size() > 0;

		if (!hasElements && !hasXMLText)
		{
			// close tag no more elements
			writeCompleteClose(appendable);
		}
		else
		{
			writeClose(appendable);

			if (hasXMLText)
			{
				writeValueAsText(object, rootObjectClassDescriptor.getScalarTextFD(), appendable);
			}

			serializeFields(object, appendable, translationContext, elementFieldDescriptors);

			writeObjectClose(rootObjectFieldDescriptor, appendable);
		}

		serializationPostHook(object);
	}

	private void serializeAttributes(Object object, Appendable appendable,
			TranslationContext translationContext,
			ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor)
			throws SIMPLTranslationException, IOException
	{
		ArrayList<? extends FieldDescriptor> attributeFieldDescriptors = rootObjectClassDescriptor
				.attributeFieldDescriptors();

		for (FieldDescriptor childFd : attributeFieldDescriptors)
		{
			try
			{
				writeValueAsAtrribute(object, childFd, appendable, translationContext);
			}
			catch (Exception ex)
			{
				throw new SIMPLTranslationException("serialize for attribute " + object, ex);
			}
		}

		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			if (translationContext.needsHashCode(object))
			{
				writeSimplIdAttribute(object, appendable);
			}

			if (isRoot && translationContext.isGraph())
			{
				writeSimplNameSpace(appendable);
				isRoot = false;
			}
		}
	}

	private void serializeFields(Object object, Appendable appendable,
			TranslationContext translationContext,
			ArrayList<? extends FieldDescriptor> elementFieldDescriptors)
			throws SIMPLTranslationException, IOException
	{
		for (FieldDescriptor childFd : elementFieldDescriptors)
		{
			switch (childFd.getType())
			{
			case SCALAR:
				writeValueAsLeaf(object, childFd, appendable, translationContext);
				break;
			case COMPOSITE_ELEMENT:
				Object compositeObject = childFd.getObject(object);
				FieldDescriptor compositeObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
						compositeObject).pseudoFieldDescriptor()
						: childFd;
				writeWrap(childFd, appendable, false);
				serialize(compositeObject, compositeObjectFieldDescriptor, appendable, translationContext);
				writeWrap(childFd, appendable, true);
				break;
			case COLLECTION_SCALAR:
			case MAP_SCALAR:
				Collection<?> scalarCollection = XMLTools.getCollection(object);
				writeWrap(childFd, appendable, false);
				for (Object collectionObject : scalarCollection)
				{
					writeValueAsLeaf(collectionObject, childFd, appendable, translationContext);
				}
				writeWrap(childFd, appendable, true);
				break;
			case COLLECTION_ELEMENT:
			case MAP_ELEMENT:
				Object collectionObject = childFd.getObject(object);
				Collection<?> compositeCollection = XMLTools.getCollection(collectionObject);
				writeWrap(childFd, appendable, false);
				for (Object collectionComposite : compositeCollection)
				{
					FieldDescriptor collectionObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
							collectionComposite).pseudoFieldDescriptor()
							: childFd;
					serialize(collectionComposite, collectionObjectFieldDescriptor, appendable,
							translationContext);
				}
				writeWrap(childFd, appendable, true);
				break;
			}
		}
	}

	private void writeSimplRef(Object object, FieldDescriptor fd, Appendable appendable)
			throws IOException
	{
		writeObjectStart(fd, appendable);
		writeSimplRefAttribute(object, appendable);
		writeCompleteClose(appendable);
	}



	private void writeObjectStart(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append('<').append(fd.elementStart());
	}

	private void writeObjectClose(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append('<').append('/').append(fd.elementStart()).append('>');
	}

	private void writeCompleteClose(Appendable appendable) throws IOException
	{
		appendable.append('/').append('>');
	}

	private void writeWrap(FieldDescriptor fd, Appendable appendable, boolean close)
			throws IOException
	{
		if (fd.isWrapped())
		{
			appendable.append('<');
			if (close)
				appendable.append('/');
			appendable.append(fd.getTagName()).append('>');
		}
	}

	private void writeValueAsLeaf(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		appendable.append('<').append(fd.elementStart()).append('>');

		fd.appendValue(appendable, object, translationContext, Format.XML);

		appendable.append('<').append('/').append(fd.elementStart()).append('>');
	}

	private void writeValueAsText(Object object, FieldDescriptor fd, Appendable appendable)
			throws SIMPLTranslationException, IOException
	{
		if (fd.isDefaultValue(object))
		{
			if (fd.isCDATA())
				appendable.append(START_CDATA);
			fd.appendValue(appendable, object, null, Format.XML);
			if (fd.isCDATA())
				appendable.append(END_CDATA);
		}
	}

	private void writeClose(Appendable appendable) throws IOException
	{
		appendable.append('>');
	}

	private void writeValueAsAtrribute(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		if (object != null)
		{
			if (!fd.isDefaultValue(object))
			{
				appendable.append(' ');
				appendable.append(fd.getTagName());
				appendable.append('=');
				appendable.append('"');

				fd.appendValue(appendable, object, translationContext, Format.XML);

				appendable.append('"');
			}
		}
	}

	private void writeSimplNameSpace(Appendable appendable) throws IOException
	{
		appendable.append(SIMPL_NAMESPACE);
	}

	private void writeSimplRefAttribute(Object object, Appendable appendable) throws IOException
	{
		appendable.append(' ');
		appendable.append(SIMPL_REF);
		appendable.append('=');
		appendable.append('"');
		appendable.append(((Integer) object.hashCode()).toString());
		appendable.append('"');
	}

	private void writeSimplIdAttribute(Object object, Appendable appendable) throws IOException
	{
		appendable.append(' ');
		appendable.append(SIMPL_ID);
		appendable.append('=');
		appendable.append('"');
		appendable.append(((Integer) object.hashCode()).toString());
		appendable.append('"');
	}
}
