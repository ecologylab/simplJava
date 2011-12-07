package ecologylab.serialization.serializers.binaryformats;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.SimplTypesScope;
import ecologylab.serialization.SimplTypesScope.GRAPH_SWITCH;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.formatenums.Format;

/**
 * 
 * @author nabeel
 * 
 */
public class TLVSerializer extends BinarySerializer implements FieldTypes
{

	public TLVSerializer()
	{

	}

	/**
	 * 
	 * @param object
	 * @param dataOutputStream
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws
	 * @throws IOException
	 */
	@Override
	public void serialize(Object object, DataOutputStream dataOutputStream,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		translationContext.resolveGraph(object);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = ClassDescriptor
				.getClassDescriptor(object.getClass());

		try
		{
			serialize(object, rootObjectClassDescriptor.pseudoFieldDescriptor(), dataOutputStream,
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
	 * @param dataOutputStream
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 * @throws IOException
	 */
	private void serialize(Object object, FieldDescriptor rootObjectFieldDescriptor,
			DataOutputStream dataOutputStream, TranslationContext translationContext)
			throws SIMPLTranslationException, IOException
	{

		if (alreadySerialized(object, translationContext))
		{
			writeSimplRef(object, rootObjectFieldDescriptor, dataOutputStream);
			return;
		}

		translationContext.mapObject(object);

		serializationPreHook(object, translationContext);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = getClassDescriptor(object);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream outputBuffer = new DataOutputStream(byteArrayOutputStream);

		int id = rootObjectFieldDescriptor.getTLVId();

		ArrayList<? extends FieldDescriptor> elementFieldDescriptors = rootObjectClassDescriptor
				.allFieldDescriptors();

		serializeFields(object, outputBuffer, translationContext, elementFieldDescriptors);

		writeHeader(dataOutputStream, byteArrayOutputStream, id);

		serializationPostHook(object, translationContext);

	}

	/**
	 * 
	 * @param dataOutputStream
	 * @param buffer
	 * @param tlvId
	 * @throws SIMPLTranslationException
	 */
	private void writeHeader(DataOutputStream dataOutputStream, ByteArrayOutputStream buffer,
			int tlvId) throws SIMPLTranslationException
	{
		try
		{
			dataOutputStream.writeInt(tlvId);

			dataOutputStream.writeInt(buffer.size());
			buffer.writeTo(dataOutputStream);
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IOException", e);
		}
	}

	/**
	 * 
	 * @param object
	 * @param dataOutputStream
	 * @param translationContext
	 * @param allFieldDescriptors
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private void serializeFields(Object object, DataOutputStream outputBuffer,
			TranslationContext translationContext,
			ArrayList<? extends FieldDescriptor> allFieldDescriptors) throws SIMPLTranslationException,
			IOException
	{

		if (SimplTypesScope.graphSwitch == GRAPH_SWITCH.ON)
		{
			if (translationContext.needsHashCode(object))
			{
				writeSimplIdAttribute(object, outputBuffer);
			}
		}

		for (FieldDescriptor childFd : allFieldDescriptors)
		{
			ByteArrayOutputStream byteArrayOutputStreamCollection = new ByteArrayOutputStream();
			DataOutputStream collectionBuffer = new DataOutputStream(byteArrayOutputStreamCollection);

			switch (childFd.getType())
			{
			case SCALAR:
				writeValue(object, childFd, outputBuffer, translationContext);
				break;
			case COMPOSITE_ELEMENT:
				Object compositeObject = childFd.getValue(object);
					
				FieldDescriptor compositeObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
						compositeObject).pseudoFieldDescriptor()
						: childFd;
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				serialize(compositeObject, compositeObjectFieldDescriptor, outputBuffer, translationContext);
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				break;
			case COLLECTION_SCALAR:
			case MAP_SCALAR:
				Object scalarCollectionObject = childFd.getValue(object);
				Collection<?> scalarCollection = XMLTools.getCollection(scalarCollectionObject);
				for (Object collectionObject : scalarCollection)
				{
					writeScalarCollectionLeaf(collectionObject, childFd, collectionBuffer, translationContext);
				}
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				break;
			case COLLECTION_ELEMENT:
			case MAP_ELEMENT:
				Object compositeCollectionObject = childFd.getValue(object);
				Collection<?> compositeCollection = XMLTools.getCollection(compositeCollectionObject);
				for (Object collectionComposite : compositeCollection)
				{
					FieldDescriptor collectionObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
							collectionComposite).pseudoFieldDescriptor()
							: childFd;
					serialize(collectionComposite, collectionObjectFieldDescriptor, collectionBuffer,
							translationContext);
				}
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				break;
			}
		}
	}

	/**
	 * 
	 * @param object
	 * @param outputBuffer
	 * @throws IOException
	 */
	private void writeSimplIdAttribute(Object object, DataOutputStream outputBuffer)
			throws IOException
	{
		outputBuffer.writeInt(TranslationContext.SIMPL_ID.hashCode());
		outputBuffer.writeInt(4);
		outputBuffer.writeInt(object.hashCode());
	}

	/**
	 * 
	 * @param fd
	 * @param outputBuffer
	 * @param collectionBuffy
	 * @throws SIMPLTranslationException
	 */
	private void writeWrap(FieldDescriptor fd, DataOutputStream outputBuffer,
			ByteArrayOutputStream collectionBuffy) throws SIMPLTranslationException
	{
		try
		{
			if (fd.isWrapped())
			{
				outputBuffer.writeInt(fd.getWrappedTLVId());
				outputBuffer.writeInt(collectionBuffy.size());
				collectionBuffy.writeTo(outputBuffer);
			}
			else
				collectionBuffy.writeTo(outputBuffer);
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IOException", e);
		}
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param outputBuffer
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 */
	private void writeScalarCollectionLeaf(Object object, FieldDescriptor fd,
			DataOutputStream outputBuffer, TranslationContext translationContext)
			throws SIMPLTranslationException
	{
		try
		{
			if (!fd.isDefaultValue(object.toString()))
			{
				outputBuffer.writeInt(fd.getTLVId());

				// TODO appendValue in scalar types should be able to append bytes to DataOutputStream.
				final StringBuilder buffy = new StringBuilder();
				OutputStream outputStream = new OutputStream()
				{
					@Override
					public void write(int b) throws IOException
					{
						buffy.append((char) b);
					}
				};

				fd.appendCollectionScalarValue(new PrintStream(outputStream), object, translationContext,
						Format.TLV);

				ByteArrayOutputStream temp = new ByteArrayOutputStream();
				DataOutputStream tempStream = new DataOutputStream(temp);
				tempStream.writeBytes(buffy.toString());

				outputBuffer.writeInt(tempStream.size());
				temp.writeTo(outputBuffer);
			}
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IOException", e);
		}
	}

	/**
	 * 
	 * @param object
	 * @param fd
	 * @param outputBuffer
	 * @param translationContext
	 * @throws SIMPLTranslationException
	 */
	private void writeValue(Object object, FieldDescriptor fd, DataOutputStream outputBuffer,
			TranslationContext translationContext) throws SIMPLTranslationException
	{
		try
		{
			if (!fd.isDefaultValueFromContext(object))
			{
				outputBuffer.writeInt(fd.getTLVId());

				// TODO appendValue in scalar types should be able to append bytes to DataOutputStream.
				final StringBuilder buffy = new StringBuilder();
				OutputStream outputStream = new OutputStream()
				{
					@Override
					public void write(int b) throws IOException
					{
						buffy.append((char) b);
					}
				};

				fd.appendValue(new PrintStream(outputStream), object, translationContext, Format.TLV);

				ByteArrayOutputStream temp = new ByteArrayOutputStream();
				DataOutputStream tempStream = new DataOutputStream(temp);
				tempStream.writeBytes(buffy.toString());

				outputBuffer.writeInt(tempStream.size());
				temp.writeTo(outputBuffer);
			}
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("IOException", e);
		}
	}

	/**
	 * 
	 * @param object
	 * @param rootObjectFieldDescriptor
	 * @param dataOutputStream
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void writeSimplRef(Object object, FieldDescriptor fd, DataOutputStream outputStream)
			throws IOException, SIMPLTranslationException
	{
		ByteArrayOutputStream simplRefData = new ByteArrayOutputStream();
		DataOutputStream outputBuffer = new DataOutputStream(simplRefData);
		outputBuffer.writeInt(TranslationContext.SIMPL_REF.hashCode());
		outputBuffer.writeInt(4);
		outputBuffer.writeInt(object.hashCode());

		writeHeader(outputStream, simplRefData, fd.getTLVId());
	}
}
