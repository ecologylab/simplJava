package simpl.serialization.stringformats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import simpl.annotations.dbal.FieldUsage;
import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.core.SimplTypesScope.GRAPH_SWITCH;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.tools.XMLTools;


/**
 * XML Specific serializer. contains functionalities specific to ouput syntax for XML from an objet
 * model.
 * 
 * @author nabeel
 */
public class XMLSerializer extends StringSerializer
{
	private static final String	START_CDATA	= "<![CDATA[";

	private static final String	END_CDATA		= "]]>";

	private boolean							isRoot			= true;

	public XMLSerializer()
	{
	}

	@Override
	public void serialize(Object object, Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		translationContext.resolveGraph(object);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = ClassDescriptor
				.getClassDescriptor(object.getClass());

		try
		{
			serialize(object, null, appendable,
					translationContext);
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IO Exception occurred", e);
		}
	}

	/**
	 * 
	 * @param object
	 * @param rootObjectFieldDescriptor
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void serialize(Object object, FieldDescriptor rootObjectFieldDescriptor,
			Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{

		if (object == null)
			return;

		if (alreadySerialized(object, translationContext))
		{
			writeSimplRef(object, rootObjectFieldDescriptor, appendable, translationContext);
			return;
		}

		translationContext.mapObject(object);

		serializationPreHook(object, translationContext);

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

		serializationPostHook(object, translationContext);
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param rootObjectClassDescriptor
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
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

		if (SimplTypesScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			if (translationContext.needsHashCode(object))
			{
				writeSimplIdAttribute(object, appendable, translationContext);
			}

			if (isRoot && translationContext.isGraph())
			{
				writeSimplNameSpace(appendable);
				isRoot = false;
			}
		}
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param elementFieldDescriptors
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
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
				Object compositeObject = childFd.getValue(object);
				if (compositeObject != null)
				{
					FieldDescriptor compositeObjectFieldDescriptor = childFd.isPolymorphic() ? null						: childFd;
					writeWrap(childFd, appendable, false);
					serialize(compositeObject, compositeObjectFieldDescriptor, appendable, translationContext);
					writeWrap(childFd, appendable, true);
				}
				break;
			case COLLECTION_SCALAR:
			case MAP_SCALAR:
				Object scalarCollectionObject = childFd.getValue(object);
				Collection<?> scalarCollection = XMLTools.getCollection(scalarCollectionObject);
				if (scalarCollection != null && scalarCollection.size() > 0)
				{
					writeWrap(childFd, appendable, false);

					for (Object collectionScalar : scalarCollection)
					{
						writeScalarCollectionLeaf(collectionScalar, childFd, appendable, translationContext);
					}
					writeWrap(childFd, appendable, true);
				}
				break;
			case COLLECTION_ELEMENT:
			case MAP_ELEMENT:
				Object compositeCollectionObject = childFd.getValue(object);
				Collection<?> compositeCollection = XMLTools.getCollection(compositeCollectionObject);
				if (compositeCollection != null && compositeCollection.size() > 0)
				{
					writeWrap(childFd, appendable, false);
					for (Object collectionComposite : compositeCollection)
					{
						FieldDescriptor collectionObjectFieldDescriptor = childFd.isPolymorphic() ? null
								: childFd;
						serialize(collectionComposite, collectionObjectFieldDescriptor, appendable,
								translationContext);
					}
					writeWrap(childFd, appendable, true);
				}
				break;
			}
		}
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @throws IOException
	 */
	private void writeSimplRef(Object object, FieldDescriptor fd, Appendable appendable, TranslationContext translationContext)
			throws IOException
	{
		writeObjectStart(fd, appendable);
		writeSimplRefAttribute(object, appendable, translationContext);
		writeCompleteClose(appendable);
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @throws IOException
	 */
	private void writeObjectStart(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append('<').append(fd.elementStart());
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @throws IOException
	 */
	private void writeObjectClose(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append('<').append('/').append(fd.elementStart()).append('>');
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeCompleteClose(Appendable appendable) throws IOException
	{
		appendable.append('/').append('>');
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @param close
	 * @throws IOException
	 */
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

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void writeValueAsLeaf(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		if (!fd.isDefaultValueFromContext(object))
		{
			appendable.append('<').append(fd.elementStart()).append('>');
			fd.appendValue(appendable, object, translationContext, Format.XML);
			appendable.append('<').append('/').append(fd.elementStart()).append('>');
		}
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void writeScalarCollectionLeaf(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		appendable.append('<').append(fd.elementStart()).append('>');
		fd.appendCollectionScalarValue(appendable, object, translationContext, Format.XML);
		appendable.append('<').append('/').append(fd.elementStart()).append('>');
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void writeValueAsText(Object object, FieldDescriptor fd, Appendable appendable)
			throws SIMPLTranslationException, IOException
	{
		if (!fd.isDefaultValueFromContext(object))
		{
			if (fd.isCDATA())
				appendable.append(START_CDATA);
			fd.appendValue(appendable, object, null, Format.XML);
			if (fd.isCDATA())
				appendable.append(END_CDATA);
		}
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeClose(Appendable appendable) throws IOException
	{
		appendable.append('>');
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void writeValueAsAtrribute(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		if (object != null)
		{
			if (!fd.isDefaultValueFromContext(object))
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

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeSimplNameSpace(Appendable appendable) throws IOException
	{
		appendable.append(TranslationContext.SIMPL_NAMESPACE);
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @throws IOException
	 */
	private void writeSimplRefAttribute(Object object, Appendable appendable, TranslationContext translationContext) throws IOException
	{
		appendable.append(' ');
		appendable.append(TranslationContext.SIMPL_REF);
		appendable.append('=');
		appendable.append('"');
		appendable.append(translationContext.getSimplId(object));
		appendable.append('"');
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @throws IOException
	 */
	private void writeSimplIdAttribute(Object object, Appendable appendable, TranslationContext translationContext) throws IOException
	{
		appendable.append(' ');
		appendable.append(TranslationContext.SIMPL_ID);
		appendable.append('=');
		appendable.append('"');
		appendable.append(translationContext.getSimplId(object));
		appendable.append('"');
	}

}
