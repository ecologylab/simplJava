package ecologylab.serialization.tlv;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.TranslationScope;

public class TLVParser implements FieldTypes
{
	private TLVEvents					listenerObject;

	private TranslationScope	translationScope;

	private ClassDescriptor		currentClassDescriptor;

	final int									HEADER_SIZE	= 8;

	public TLVParser(TLVEvents listenerObject, TranslationScope translationScope)
	{
		this.listenerObject = listenerObject;
		this.translationScope = translationScope;
	}

	public void parse(byte[] dataArray)
	{
		// Start event for TLV data.
		listenerObject.startTLV();

		int type = Utils.getInt(dataArray, 0);
		int length = Utils.getInt(dataArray, 4);

		currentClassDescriptor = translationScope.getClassDescriptorByTLVId(type);

		String rootObjectName = currentClassDescriptor.getTagName();

		listenerObject.startObject(rootObjectName);

		parseTLVBlock(dataArray, HEADER_SIZE, length, currentClassDescriptor.pseudoFieldDescriptor());

		listenerObject.endObject(rootObjectName);

		// End event for TLV data
		listenerObject.endTLV();
	}

	public void parseTLVBlock(byte[] dataArray, int start, int offset,
			FieldDescriptor currentFieldDescriptor)
	{
		try
		{
			int type = Utils.getInt(dataArray, start);
			int length = Utils.getInt(dataArray, start + 4);
			FieldDescriptor localCurrentFieldDescriptor = currentFieldDescriptor;
			boolean isScalar = false;

			final int currentType = currentFieldDescriptor.getType();

			// if (!currentFieldDescriptor.isCollection() && currentType != WRAPPER)
			currentFieldDescriptor = (currentType == WRAPPER) ? currentFieldDescriptor.getWrappedFD()
					: currentClassDescriptor.getFieldDescriptorByTLVId(type);

			String currentObjectName = currentFieldDescriptor.elementStart();

			listenerObject.startObject(currentObjectName);

			isScalar = currentFieldDescriptor.isScalar();

			if (!isScalar)
			{
				if (currentFieldDescriptor.getType() == COMPOSITE_ELEMENT
						|| currentFieldDescriptor.getType() == COLLECTION_ELEMENT
						|| currentFieldDescriptor.getType() == MAP_ELEMENT)
				{
					currentClassDescriptor = currentFieldDescriptor.elementClassDescriptor();
				}

				// if its not scalar then there is another tlv block ahead
				parseTLVBlock(dataArray, start + HEADER_SIZE, length, currentFieldDescriptor);
			}
			else
			{
				String value = new String(dataArray, start + HEADER_SIZE, length);
				listenerObject.primitive(value);
			}

			listenerObject.endObject(currentObjectName);

			//restore parsing states for more tlv blocks
			currentClassDescriptor = currentFieldDescriptor.getDeclaringClassDescriptor();
			currentFieldDescriptor = localCurrentFieldDescriptor;

			int tlvBlockSize = HEADER_SIZE + length;

			// if there are more TLV blocks parse them
			if (tlvBlockSize < offset)
				parseTLVBlock(dataArray, start + tlvBlockSize, offset - tlvBlockSize,
						currentFieldDescriptor);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
