package simpl.deserialization.binaryformats.tlv;

import simpl.core.SimplTypesScope;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;

/**
 * This is the basic tlv parser for parsing tlv messages generated from
 * <code>ecologylab.serialization</code> translation framework.
 * 
 * <p/>
 * The framework works in conjunction with the translation scope to map tlv ids with their tag
 * names. TLV messages are type-length-value triplet. <code>type</code> is a 32 bit integer takes up
 * to 4 bytes. <code>length</code> is also a 32 bit integer and takes up to 4 bytes of the message
 * header of the tlv message.
 * 
 * 
 * 
 * @author Nabeel Shahzad
 * 
 * @version 1.0
 */
public class TLVParser
{
	/**
	 * an object which implements TLVEvents will get the appropriate events from the parser.
	 */
	private TLVEvents					listenerObject;

	/**
	 * translation scope which maps tlv ids to tag names.
	 */
	private SimplTypesScope	translationScope;

	/**
	 * local state variable.
	 */
	private ClassDescriptor		currentClassDescriptor;

	/**
	 * constant for type field size.
	 */
	final int									TYPE_SIZE		= 4;

	/**
	 * constant for length field size
	 */
	final int									LENGTH_SIZE	= 4;

	/**
	 * contant for header size.
	 */
	final int									HEADER_SIZE	= TYPE_SIZE + LENGTH_SIZE;

	/**
	 * Constructor for tlv parser.
	 * 
	 * @param listenerObject
	 * @param translationScope
	 */
	public TLVParser(TLVEvents listenerObject, SimplTypesScope translationScope)
	{
		this.listenerObject = listenerObject;
		this.translationScope = translationScope;
	}

	/**
	 * Parse method that creates an element state object which wraps whole message
	 * 
	 * @param dataArray
	 */
	public void parse(byte[] dataArray)
	{
		// Start event for TLV data.
		listenerObject.startTLV();

		int type = Utils.getInt(dataArray, 0);
		int length = Utils.getInt(dataArray, 4);

		currentClassDescriptor = null;

		String rootObjectName = currentClassDescriptor.getTagName();

		// start of first object.
		listenerObject.startObject(rootObjectName);

		parseTLVBlock(dataArray, HEADER_SIZE, length, null);

		// end of first object .
		listenerObject.endObject(rootObjectName);

		// End event for TLV data
		listenerObject.endTLV();
	}

	/**
	 * Method which recursively looks down for tlv blocks and parses them also calls methods on the
	 * TLVEvents interface.
	 * 
	 * @param dataArray
	 * @param start
	 * @param offset
	 * @param currentFieldDescriptor
	 */
	public void parseTLVBlock(byte[] dataArray, int start, int offset,
			FieldDescriptor currentFieldDescriptor)
	{
		try
		{
			int type = Utils.getInt(dataArray, start);
			int length = Utils.getInt(dataArray, start + 4);
			FieldDescriptor localCurrentFieldDescriptor = currentFieldDescriptor;
			boolean isScalar = false;

			FieldType currentType = currentFieldDescriptor.getType();

			currentFieldDescriptor = (currentType == FieldType.WRAPPER) ? currentFieldDescriptor.getWrappedFD()
					: null;

			//if(currentFieldDescriptor.isPolymorphic()) currentFieldDescriptor = currentFieldDescriptor.elementClassDescriptor(type).pseudoFieldDescriptor();
			
			String currentObjectName = "";
			
			listenerObject.startObject(currentObjectName);

			isScalar = currentFieldDescriptor.isScalar();

			if (!isScalar)
			{
				if (currentFieldDescriptor.getType() == FieldType.COMPOSITE_ELEMENT
						|| currentFieldDescriptor.getType() == FieldType.COLLECTION_ELEMENT
						|| currentFieldDescriptor.getType() == FieldType.MAP_ELEMENT)
				{
					currentClassDescriptor = currentFieldDescriptor.elementClassDescriptor(type);
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

			// restore parsing states for more tlv blocks
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
