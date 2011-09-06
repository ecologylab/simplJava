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
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.TranslationScope.GRAPH_SWITCH;
import ecologylab.serialization.serializers.Format;

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
	 * @throws IOException
	 */
	public void serialize(Object object, DataOutputStream dataOutputStream,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		translationContext.resolveGraph(object);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = ClassDescriptor
				.getClassDescriptor(object.getClass());

		serialize(object, rootObjectClassDescriptor.pseudoFieldDescriptor(), dataOutputStream,
				translationContext);
	}

	/**
	 * 
	 * @param object
	 * @param rootObjectFieldDescriptor
	 * @param dataOutputStream
	 * @param translationContext
	 * @throws SIMPLTranslationException
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

		serializationPreHook(object);

		ClassDescriptor<? extends FieldDescriptor> rootObjectClassDescriptor = getClassDescriptor(object);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		DataOutputStream outputBuffer = new DataOutputStream(byteArrayOutputStream);

		int id = rootObjectFieldDescriptor.getTLVId();

		ArrayList<? extends FieldDescriptor> elementFieldDescriptors = rootObjectClassDescriptor
				.allFieldDescriptors();

		serializeFields(object, outputBuffer, translationContext, elementFieldDescriptors);

		writeHeader(dataOutputStream, byteArrayOutputStream, id);

		serializationPostHook(object);

	}

	private void writeHeader(DataOutputStream dataOutputStream, ByteArrayOutputStream buffer,
			int tlvId) throws IOException
	{
		dataOutputStream.writeInt(tlvId);
		dataOutputStream.writeInt(buffer.size());
		buffer.writeTo(dataOutputStream);
	}

	/**
	 * 
	 * @param object
	 * @param dataOutputStream
	 * @param translationContext
	 * @param allFieldDescriptors
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private void serializeFields(Object object, DataOutputStream outputBuffer,
			TranslationContext translationContext,
			ArrayList<? extends FieldDescriptor> allFieldDescriptors) throws SIMPLTranslationException,
			IOException
	{
		
		if (TranslationScope.graphSwitch == GRAPH_SWITCH.ON)
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
				Object compositeObject = childFd.getObject(object);
				FieldDescriptor compositeObjectFieldDescriptor = childFd.isPolymorphic() ? getClassDescriptor(
						compositeObject).pseudoFieldDescriptor()
						: childFd;
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				serialize(compositeObject, compositeObjectFieldDescriptor, outputBuffer, translationContext);
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				break;
			case COLLECTION_SCALAR:
			case MAP_SCALAR:
				Collection<?> scalarCollection = XMLTools.getCollection(object);
				for (Object collectionObject : scalarCollection)
				{
					writeValue(collectionObject, childFd, collectionBuffer, translationContext);
				}
				writeWrap(childFd, outputBuffer, byteArrayOutputStreamCollection);
				break;
			case COLLECTION_ELEMENT:
			case MAP_ELEMENT:
				Object collectionObject = childFd.getObject(object);
				Collection<?> compositeCollection = XMLTools.getCollection(collectionObject);
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

	private void writeSimplIdAttribute(Object object, DataOutputStream outputBuffer)
	{
		// TODO Auto-generated method stub
		
	}

	private void writeWrap(FieldDescriptor fd, DataOutputStream outputBuffer,
			ByteArrayOutputStream collectionBuffy) throws IOException
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

	private void writeValue(Object object, FieldDescriptor fd, DataOutputStream outputBuffer,
			TranslationContext translationContext) throws SIMPLTranslationException, IOException
	{
		if (!fd.isDefaultValue(object))
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

	/**
	 * 
	 * @param object
	 * @param rootObjectFieldDescriptor
	 * @param dataOutputStream
	 */
	private void writeSimplRef(Object object, FieldDescriptor fd, DataOutputStream dataOutputStream)
	{
		// TODO Auto-generated method stub

	}
}
