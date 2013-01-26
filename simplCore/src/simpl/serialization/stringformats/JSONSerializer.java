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


/***
 * JSONSerializaton. Guides serialization of data in JSON. Contains code that is specific to
 * creating a valid JSON of the provided object. Supports graph handling.
 * 
 * @author nabeel
 * 
 */
public class JSONSerializer extends StringSerializer
{
	private int	numOfFields;

	public JSONSerializer()
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
			writeStart(appendable);

			serialize(object, rootObjectClassDescriptor.pseudoFieldDescriptor(), appendable,
					translationContext, true);

			writeClose(appendable);
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
	 * @param withTag
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void serialize(Object object, FieldDescriptor rootObjectFieldDescriptor,
			Appendable appendable, TranslationContext translationContext, boolean withTag)
			throws SIMPLTranslationException, IOException
	{
		if (alreadySerialized(object, translationContext))
		{
			writeSimplRef(object, rootObjectFieldDescriptor, withTag, appendable, translationContext);
			return;
		}

		translationContext.mapObject(object);

		serializationPreHook(object, translationContext);

		writeObjectStart(rootObjectFieldDescriptor, appendable, withTag);

		//numOfFields = 0;
		
		ClassDescriptor<? extends FieldDescriptor> classDescriptor = getClassDescriptor(object);
		
		serializeFields(object, appendable, translationContext, classDescriptor);

		writeClose(appendable);

		serializationPostHook(object, translationContext);
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param allFieldDescriptors
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void serializeFields(Object object, Appendable appendable,
			TranslationContext translationContext,
			ClassDescriptor<? extends FieldDescriptor> classDescriptor) throws SIMPLTranslationException,
			IOException
	{
		ArrayList<? extends FieldDescriptor> allFieldDescriptors = classDescriptor.allFieldDescriptors();

		if (SimplTypesScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			if (translationContext.needsHashCode(object))
			{
				writeSimplIdAttribute(object, appendable, allFieldDescriptors.size() <= 0);
			}
		}
			
		ArrayList<? extends FieldDescriptor> attributeFieldDescriptors = classDescriptor.attributeFieldDescriptors();
		int numOfFields = serializeFieldsHelper(appendable, object, translationContext, attributeFieldDescriptors, 0);
		ArrayList<? extends FieldDescriptor> elementFieldDescriptors = classDescriptor.elementFieldDescriptors();
		serializeFieldsHelper(appendable, object, translationContext, elementFieldDescriptors,numOfFields);
	}

	private int serializeFieldsHelper(Appendable appendable, Object object,
			TranslationContext translationContext,
			ArrayList<? extends FieldDescriptor> fieldDescriptorList, int numOfFields) throws SIMPLTranslationException,
			IOException
	{
		
		for (FieldDescriptor childFd : fieldDescriptorList)
		{
			if (isSerializable(childFd, object))
			{				
				if (numOfFields++ > 0)
					appendable.append(',');

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
					if (childFd.isPolymorphic())
						serializePolymorphicCollection(object, appendable, translationContext, childFd);
					else
						serializeCompositeCollection(object, appendable, translationContext, childFd);
					break;
				}
			}
		}
		return numOfFields;
	}

	/**
	 * check if the field is of default value or null. we don't have to serialize that field
	 * 
	 * @param childFd
	 * @param object
	 * @return
	 * @throws SIMPLTranslationException
	 */
	private boolean isSerializable(FieldDescriptor childFd, Object object)
			throws SIMPLTranslationException
	{
		switch (childFd.getType())
		{
		case SCALAR:
			if (childFd.isDefaultValueFromContext(object))
				return false;
			break;
		case COMPOSITE_ELEMENT:
		case COLLECTION_ELEMENT:
		case MAP_ELEMENT:
			Object obj = childFd.getValue(object);
			if (obj == null)
				return false;
			break;
		case COLLECTION_SCALAR:
		case MAP_SCALAR:
			Object scalarCollectionObject = childFd.getValue(object);
			Collection<?> scalarCollection = XMLTools.getCollection(scalarCollectionObject);
			if (scalarCollection == null || scalarCollection.size() <= 0)
				return false;
			break;
		}

		return true;
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param childFd
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void serializeComposite(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor childFd)
			throws SIMPLTranslationException, IOException
	{
		Object compositeObject = childFd.getValue(object);
		FieldDescriptor compositeObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
				compositeObject).pseudoFieldDescriptor() : childFd;
		serialize(compositeObject, compositeObjectFieldDescriptor, appendable, translationContext, true);
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param childFd
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeCompositeCollection(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor childFd) throws IOException,
			SIMPLTranslationException
	{
		Object collectionObject = childFd.getValue(object);
		Collection<?> compositeCollection = XMLTools.getCollection(collectionObject);
		
		if(compositeCollection != null)
		{
			int numberOfItems = 0;
	
			writeWrap(childFd, appendable, false);
			writeCollectionStart(childFd, appendable);
			for (Object collectionComposite : compositeCollection)
			{
				FieldDescriptor collectionObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
						collectionComposite).pseudoFieldDescriptor()
						: childFd;
	
				serialize(collectionComposite, collectionObjectFieldDescriptor, appendable,
						translationContext, false);
	
				if (++numberOfItems < compositeCollection.size())
					appendable.append(',');
			}
			writeCollectionEnd(appendable);
			writeWrap(childFd, appendable, true);
		}
	}

	/**
	 * 
	 * @param object
	 * @param appendable
	 * @param translationContext
	 * @param childFd
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializePolymorphicCollection(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor childFd) throws IOException,
			SIMPLTranslationException
	{
		Object collectionObject = childFd.getValue(object);
		Collection<?> compositeCollection = XMLTools.getCollection(collectionObject);
		int numberOfItems = 0;

		if(compositeCollection != null)
		{		
			writePolymorphicCollectionStart(childFd, appendable);
			for (Object collectionComposite : compositeCollection)
			{
				FieldDescriptor collectionObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
						collectionComposite).pseudoFieldDescriptor()
						: childFd;
	
				writeStart(appendable);
				serialize(collectionComposite, collectionObjectFieldDescriptor, appendable,
						translationContext, true);
				writeClose(appendable);
	
				if (++numberOfItems < compositeCollection.size())
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
	 * @param childFd
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeScalarCollection(Object object, Appendable appendable,
			TranslationContext translationContext, FieldDescriptor childFd) throws IOException,
			SIMPLTranslationException
	{
		Object scalarCollectionObject = childFd.getValue(object);
		Collection<?> scalarCollection = XMLTools.getCollection(scalarCollectionObject);
		int numberOfItems = 0;

		if(scalarCollection != null)
		{
			writeWrap(childFd, appendable, false);
			writeCollectionStart(childFd, appendable);
			for (Object collectionObject : scalarCollection)
			{
				writeCollectionScalar(collectionObject, childFd, appendable, translationContext);
				if (++numberOfItems < scalarCollection.size())
					appendable.append(',');
			}
			writeCollectionEnd(appendable);
			writeWrap(childFd, appendable, true);
		}
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @param translationContext
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeScalar(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws IOException, SIMPLTranslationException
	{
		appendable.append('"');
		appendable.append(fd.getTagName());
		appendable.append('"');
		appendable.append(':');
		appendable.append('"');
		fd.appendValue(appendable, object, translationContext, Format.JSON);
		appendable.append('"');
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeCollectionEnd(Appendable appendable) throws IOException
	{
		appendable.append(']');
	}

	private void writeCollectionStart(FieldDescriptor fd, Appendable appendable) throws IOException
	{
		appendable.append('"').append(fd.elementStart()).append('"');
		appendable.append(':');
		appendable.append('[');
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @throws IOException
	 */
	private void writePolymorphicCollectionStart(FieldDescriptor fd, Appendable appendable)
			throws IOException
	{
		appendable.append('"').append(fd.getTagName()).append('"');
		appendable.append(':');
		appendable.append('[');

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
			if (!close)
			{
				appendable.append('"');
				appendable.append(fd.getTagName());
				appendable.append('"').append(':');
				appendable.append('{');
			}
			else
			{
				appendable.append('}');
			}
		}
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param appendable
	 * @param translationContext
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void writeCollectionScalar(Object object, FieldDescriptor fd, Appendable appendable,
			TranslationContext translationContext) throws IOException, SIMPLTranslationException
	{
		appendable.append('"');
		fd.appendCollectionScalarValue(appendable, object, translationContext, Format.JSON);
		appendable.append('"');
	}

	/**
	 * 
	 * @param fd
	 * @param appendable
	 * @param withTag
	 * @throws IOException
	 */
	private void writeObjectStart(FieldDescriptor fd, Appendable appendable, boolean withTag)
			throws IOException
	{
		if (withTag)
		{
			appendable.append('"').append(fd.elementStart()).append('"');
			appendable.append(':');
		}
		appendable.append('{');
	}

	/**
	 * 
	 * @param object
	 * @param rootObjectFieldDescriptor
	 * @param appendable
	 * @throws IOException
	 */
	private void writeSimplRef(Object object, FieldDescriptor fd, boolean withTag,
			Appendable appendable, TranslationContext translationContext) throws IOException
	{
		writeObjectStart(fd, appendable, withTag);
		writeSimplRefAttribute(object, appendable, translationContext);
		writeClose(appendable);
	}

	private void writeSimplRefAttribute(Object object, Appendable appendable, TranslationContext translationContext) throws IOException
	{
		appendable.append('"');
		appendable.append(TranslationContext.JSON_SIMPL_REF);
		appendable.append('"');
		appendable.append(':');
		appendable.append('"');
		appendable.append(translationContext.getSimplId(object));
		appendable.append('"');
	}

	private void writeSimplIdAttribute(Object object, Appendable appendable, boolean last)
			throws IOException
	{
		appendable.append('"');
		appendable.append(TranslationContext.JSON_SIMPL_ID);
		appendable.append('"');
		appendable.append(':');
		appendable.append('"');
		appendable.append(((Integer) object.hashCode()).toString());
		appendable.append('"');

		if (!last)
		{
			appendable.append(',');
		}
	}

	/**
	 * 
	 * @param appendable
	 * @throws IOException
	 */
	private void writeStart(Appendable appendable) throws IOException
	{
		appendable.append('{');
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
}
