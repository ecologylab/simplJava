package simpl.serialization.stringformats;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import simpl.annotations.dbal.FieldUsage;
import simpl.core.TranslationContext;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.exceptions.SIMPLTranslationException;
import simpl.formats.enums.Format;
import simpl.tools.XMLTools;


/**
 * 
 * @author nabeel
 */
public class BibtexSerializer extends StringSerializer
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
			serialize(object,null, appendable,
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
		serializationPreHook(object, translationContext);

		writeObjectStart(rootObjectFieldDescriptor, appendable);

		ArrayList<? extends FieldDescriptor> allFieldDescriptors = getClassDescriptor(object)
				.allFieldDescriptors();

		serializeFields(object, appendable, translationContext, allFieldDescriptors);

		writeClose(appendable);

		serializationPostHook(object, translationContext);
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
		Object compositeObject = fd.getValue(object);
		
		FieldDescriptor compositeAsScalarFD = getClassDescriptor(compositeObject)
				.getScalarValueFieldDescripotor();

		if (compositeAsScalarFD != null)
		{
			writeScalarBibtexAttribute(compositeObject, compositeAsScalarFD, appendable, translationContext);
		}
	}

	private void writeScalarBibtexAttribute(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		if (!fd.isDefaultValueFromContext(object))
		{
			fd.appendValue(appendable, object, translationContext, Format.BIBTEX);
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
		if (!fd.isDefaultValueFromContext(object))
		{
		}

		fd.appendValue(appendable, object, translationContext, Format.BIBTEX);

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
		Object scalarCollectionObject = fd.getValue(object);
		Collection<?> scalarCollection = XMLTools.getCollection(scalarCollectionObject);
		
		String delim = "author".equals("") ? " and " : translationContext
				.getDelimiter();

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
					writeScalarBibtexAttribute(collectionObject, compositeAsScalarFD, appendable,
							translationContext);
				}

				if (++numberOfItems < scalarCollection.size())
					appendable.append(delim);
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
		Object scalarCollectionObject = fd.getValue(object);
		Collection<?> scalarCollection = XMLTools.getCollection(scalarCollectionObject);

		String delim = "author".equals("") ? " and " : translationContext
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
		fd.appendCollectionScalarValue(appendable, collectionObject, translationContext, Format.BIBTEX);
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
		appendable.append("");
		appendable.append('{');
	}
}
