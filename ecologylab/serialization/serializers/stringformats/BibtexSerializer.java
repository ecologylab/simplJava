package ecologylab.serialization.serializers.stringformats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.Format;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;

/**
 * 
 * @author nabeel
 */
public class BibtexSerializer extends StringSerializer implements FieldTypes
{

	public BibtexSerializer()
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
			serialize(object, rootObjectClassDescriptor.pseudoFieldDescriptor(), appendable,
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
		serializationPreHook(object);

		writeObjectStart(rootObjectFieldDescriptor, appendable);

		ArrayList<? extends FieldDescriptor> allFieldDescriptors = getClassDescriptor(object)
				.allFieldDescriptors();

		serializeFields(object, appendable, translationContext, allFieldDescriptors);

		writeClose(appendable);

		serializationPostHook(object);
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param allFieldDescriptors
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeFields(Object object, Appendable appendable,
			TranslationContext translationContext,
			ArrayList<? extends FieldDescriptor> allFieldDescriptors) throws IOException,
			SIMPLTranslationException
	{
		int numOfFields = 0;

		for (FieldDescriptor childFd : allFieldDescriptors)
		{
			switch (childFd.getType())
			{
			case SCALAR:
				serializeScalar(object, childFd, appendable, translationContext);
				break;
			case COMPOSITE_ELEMENT:
				serializeComposite(object, appendable, translationContext, childFd);
				break;
			case COLLECTION_SCALAR:
			case MAP_SCALAR:
				serializeScalarCollection(object, appendable, translationContext, childFd);
				break;
			case COLLECTION_ELEMENT:
			case MAP_ELEMENT:
				if (!childFd.isPolymorphic())
					serializeCompositeCollection(object, appendable, translationContext, childFd);
				break;
			}

			if (++numOfFields < allFieldDescriptors.size())
				appendable.append(',');
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
	private void serializeScalar(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		writeBibtexAttribute(object, fd, appendable, translationContext);
	}

	private void serializeComposite(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor fd) throws SIMPLTranslationException,
			IOException
	{
		Object compositeObject = fd.getObject(object);
		FieldDescriptor compositeAsScalarFD = getClassDescriptor(compositeObject)
				.getScalarValueFieldDescripotor();

		if (compositeAsScalarFD != null)
		{
			writeBibtexAttribute(compositeObject, fd, appendable, translationContext);
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
	private void writeBibtexAttribute(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		if (!fd.isDefaultValue(object))
		{
			if (fd.isBibtexKey())
			{
				appendable.append(fd.getBibtexTagName());
				appendable.append('=');
				appendable.append('{');
			}
		}

		fd.appendValue(appendable, object, translationContext, Format.BIBTEX);

		if (!fd.isBibtexKey())
			appendable.append('}');
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param fd
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeCompositeCollection(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor fd) throws IOException,
			SIMPLTranslationException
	{
		Collection<?> scalarCollection = XMLTools.getCollection(object);

		if (scalarCollection.size() > 0)
		{
			int numberOfItems = 0;

			writeCollectionStart(fd, appendable);
			for (Object collectionObject : scalarCollection)
			{
				FieldDescriptor compositeAsScalarFD = getClassDescriptor(collectionObject)
						.getScalarValueFieldDescripotor();

				if (compositeAsScalarFD != null)
				{
					writeBibtexAttribute(collectionObject, fd, appendable, translationContext);
				}

				if (++numberOfItems < scalarCollection.size())
					appendable.append(',');
			}
			writeCollectionEnd(appendable);
		}
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param fd
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeScalarCollection(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor fd) throws IOException,
			SIMPLTranslationException
	{
		Collection<?> scalarCollection = XMLTools.getCollection(object);

		String delim = "author".equals(fd.getBibtexTagName()) ? " and " : translationContext
				.getDelimiter();

		if (scalarCollection.size() > 0)
		{
			int numberOfItems = 0;

			writeCollectionStart(fd, appendable);
			for (Object collectionObject : scalarCollection)
			{
				writeCollectionScalar(collectionObject, fd, appendable, translationContext);
				if (++numberOfItems < scalarCollection.size())
					appendable.append(delim);
			}
			writeCollectionEnd(appendable);
		}
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @throws IOException
	 */
	private void writeCollectionStart(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append(fd.getTagName());
		appendable.append('=');
		appendable.append("{");
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeCollectionEnd(Appendable appendable) throws IOException
	{
		appendable.append("}");
	}

	/**
	 * 
	 * @param collectionObject
	 * @param fd
	 * @param appendable
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 */
	private void writeCollectionScalar(Object collectionObject, FieldDescriptor fd,
			Appendable appendable, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		fd.appendValue(appendable, collectionObject, translationContext, Format.BIBTEX);
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeClose(Appendable appendable) throws IOException
	{
		appendable.append('}');
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @throws IOException
	 */
	private void writeObjectStart(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append('@');
		appendable.append(fd.getBibtexTagName());
		appendable.append('{');
	}
}
