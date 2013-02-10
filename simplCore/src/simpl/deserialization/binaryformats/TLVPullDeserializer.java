package simpl.deserialization.binaryformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import simpl.core.DeserializationHookStrategy;
import simpl.core.ElementState;
import simpl.core.SimplTypesScope;
import simpl.core.TranslationContext;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.FieldType;
import simpl.deserialization.DeserializationProcedureState;
import simpl.exceptions.SIMPLTranslationException;
import simpl.types.element.IMappable;


/**
 * 
 * @author nabeelshahzad
 *
 */
public class TLVPullDeserializer extends BinaryPullDeserializer
{
	private static final int	HEADER_SIZE	= 8;

	DataInputStream						inputStream;

	int												blockType;

	int												blockLength;

	boolean										isEos				= false;

	public TLVPullDeserializer(SimplTypesScope translationScope,
			TranslationContext translationContext, DeserializationHookStrategy deserializationHookStrategy)
	{
		super(translationScope, translationContext);
	}

	@Override
	public Object parse(byte[] byteArray) throws SIMPLTranslationException
	{
		try
		{
			configure(new ByteArrayInputStream(byteArray));
			return parse();
		}
		catch (Exception ex)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", ex);
		}
	}

	@Override
	public Object parse(InputStream inputStream) throws SIMPLTranslationException
	{
		try
		{
			configure(inputStream);
			return parse();
		}
		catch (Exception ex)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", ex);
		}
	}

	/**
	 * 
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private Object parse() throws SIMPLTranslationException, IOException
	{
		Object root = null;

		nextHeader();

		ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor = null;

		if (rootClassDescriptor == null)
		{
			throw new SIMPLTranslationException(
					"cannot find the class descriptor for root element; make sure if translation scope is correct.");
		}

		root = rootClassDescriptor.getInstance();

		deserializationPreHook(root, translationContext);
		if (deserializationHookStrategy != null)
			deserializationHookStrategy.deserializationPreHook(root, null);

		return createObjectModel(root, rootClassDescriptor, type(), length());
	}

	/**
	 * 
	 * @param root
	 * @param rootClassDescriptor
	 * @param type
	 * @param length
	 * @return
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 */
	private Object createObjectModel(Object root,
			ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor, int type, int length)
			throws IOException, SIMPLTranslationException
	{

		FieldDescriptor currentFieldDescriptor = null;
		int bytesRead = 0;

		DeserializationProcedureState state = DeserializationProcedureState.INIT;

		while (!isEos && bytesRead < length)
		{
			bytesRead += nextHeader();

			if (type() == TranslationContext.SIMPL_ID.hashCode())
			{
				Integer simplId = inputStream.readInt();
				translationContext.markAsUnmarshalled(simplId.toString(), root);
				bytesRead += 4;
				continue;
			}

			if (type() == TranslationContext.SIMPL_REF.hashCode())
			{
				Integer simplRef = inputStream.readInt();
				return translationContext.getFromMap(simplRef.toString());
			}

			currentFieldDescriptor = null;

			FieldType fieldType = currentFieldDescriptor.getType();

			switch (fieldType)
			{
			case SCALAR:
				bytesRead += deserializeScalar(root, currentFieldDescriptor);
				break;
			case COLLECTION_SCALAR:
				bytesRead += deserializeScalarCollectionElement(root, currentFieldDescriptor);
				break;
			case COMPOSITE_ELEMENT:
				bytesRead += deserializeComposite(root, currentFieldDescriptor);
				break;
			case COLLECTION_ELEMENT:
				bytesRead += deserializeCompositeCollectionElement(root, currentFieldDescriptor);
				break;
			case MAP_ELEMENT:
				bytesRead += deserializeCompositeMapElement(root, currentFieldDescriptor);
				break;
			case WRAPPER:
				currentFieldDescriptor = currentFieldDescriptor.getWrappedFD();
				switch (currentFieldDescriptor.getType())
				{
				case COLLECTION_SCALAR:
					bytesRead += deserializeScalarCollection(root, currentFieldDescriptor);
					break;
				case COLLECTION_ELEMENT:
					bytesRead += deserializeCompositeCollection(root, currentFieldDescriptor);
					break;
				case MAP_ELEMENT:
					bytesRead += deserializeCompositeMap(root, currentFieldDescriptor);
					break;
				case COMPOSITE_ELEMENT:
					//TODO: wrapped composites in tlv?
					break;
				}
				break;
			}
			
			state = nextDeserializationProcedureState(state, fieldType);
			if (state == DeserializationProcedureState.ATTRIBUTES_DONE)
			{
				// when we know that definitely all attributes are done, we do the in-hook
				deserializationInHook(root, translationContext);
				if (deserializationHookStrategy != null)
					deserializationHookStrategy.deserializationInHook(root, currentFieldDescriptor);
				state = DeserializationProcedureState.ELEMENTS;
			}
		}
		
		state = DeserializationProcedureState.ELEMENTS_DONE;
		
		deserializationPostHook(root, translationContext);
		if (deserializationHookStrategy != null)
			deserializationHookStrategy.deserializationPostHook(root, 
					currentFieldDescriptor == null || currentFieldDescriptor.getType() == FieldType.IGNORED_ELEMENT
					? null : currentFieldDescriptor);
		
		return root;
	}

	/**
	 * 
	 * @param root
	 * @param fd
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeCompositeMap(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		int bytesRead = 0;
		int length = length();
		do
		{
			bytesRead += nextHeader();
			bytesRead += deserializeCompositeMapElement(root, fd);
		}
		while (!isEos && bytesRead < length);
		return bytesRead;
	}

	/**
	 * 
	 * @param root
	 * @param fd
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeCompositeCollection(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		int bytesRead = 0;
		int length = length();
		do
		{
			bytesRead += nextHeader();
			bytesRead += deserializeCompositeCollectionElement(root, fd);
		}
		while (!isEos && bytesRead < length);
		return bytesRead;
	}

	/**
	 * 
	 * @param root
	 * @param fd
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeScalarCollection(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		int bytesRead = 0;
		int length = length();
		do
		{
			bytesRead += nextHeader();
			bytesRead += deserializeScalarCollectionElement(root, fd);
		}
		while (!isEos && bytesRead < length);
		return bytesRead;
	}

	/**
	 * 
	 * @param root
	 * @param fd
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeCompositeMapElement(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		Object subRoot;
		int length = length();

		subRoot = getSubRoot(fd, root);
		if (subRoot instanceof IMappable<?>)
		{
			final Object key = ((IMappable<?>) subRoot).key();
			Map map = null;//(Map) fd.automaticLazyGetCollectionOrMap(root);
			map.put(key, subRoot);
		}

		return length;
	}

	/**
	 * 
	 * @param root
	 * @param fd
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeCompositeCollectionElement(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		Object subRoot;
		int length = length();

		subRoot = getSubRoot(fd, root);
		Collection collection =null;// (Collection) fd.automaticLazyGetCollectionOrMap(root);
		collection.add(subRoot);
		return length;
	}

	/**
	 * 
	 * @param root
	 * @param currentFieldDescriptor
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeComposite(Object root, FieldDescriptor currentFieldDescriptor)
			throws SIMPLTranslationException, IOException
	{
		int length = length();
		Object subRoot = getSubRoot(currentFieldDescriptor, root);
		currentFieldDescriptor.setFieldToComposite(root, subRoot);
		return length;
	}

	/**
	 * 
	 * @param currentFieldDescriptor
	 * @param root
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private Object getSubRoot(FieldDescriptor currentFieldDescriptor, Object root)
			throws SIMPLTranslationException, IOException
	{
		Object subRoot = null;
		ClassDescriptor<? extends FieldDescriptor> subRootClassDescriptor = null;//currentFieldDescriptor
				//.getChildClassDescriptor(type());

		subRoot = subRootClassDescriptor.getInstance();
		deserializationPreHook(subRoot, translationContext);
		if (deserializationHookStrategy != null)
			deserializationHookStrategy.deserializationPreHook(subRoot, currentFieldDescriptor);

		if (subRoot != null)
		{
			if (subRoot instanceof ElementState && root instanceof ElementState)
			{
				((ElementState) subRoot).setupInParent((ElementState) root);
			}
		}

		createObjectModel(subRoot, subRootClassDescriptor, type(), length());
		
		if (deserializationHookStrategy != null && subRoot != null)
		{
			Object newSubRoot= deserializationHookStrategy.changeObjectIfNecessary(subRoot, currentFieldDescriptor);
			if (newSubRoot != null)
				subRoot = newSubRoot;
		}
		
		return subRoot;
	}

	/**
	 * 
	 * @param root
	 * @param fd
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	private int deserializeScalarCollectionElement(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		byte[] value = new byte[length()];
		inputStream.read(value);
		String stringValue = new String(value);
		//fd.addLeafNodeToCollection(root, stringValue, translationContext);
		return length();
	}

	/**
	 * 
	 * @param root
	 * @param currentFieldDescriptor
	 * @return
	 * @throws IOException
	 */
	private int deserializeScalar(Object root, FieldDescriptor currentFieldDescriptor)
			throws IOException
	{
		byte[] value = new byte[length()];
		inputStream.read(value);
		String stringValue = new String(value);
		currentFieldDescriptor.setFieldToScalar(root, stringValue, translationContext);
		return length();
	}

	/**
	 * 
	 * @param stream
	 */
	private void configure(InputStream stream)
	{
		inputStream = new DataInputStream(stream);
	}

	/**
	 * 
	 * @return
	 */
	private int nextHeader()
	{
		try
		{
			blockType = inputStream.readInt();
			blockLength = inputStream.readInt();
			return HEADER_SIZE;
		}
		catch (Exception e)
		{
			isEos = true;
			return 0;
		}

	}

	/**
	 * 
	 * @return
	 */
	private int length()
	{
		return blockLength;
	}

	/**
	 * 
	 * @return
	 */
	private int type()
	{
		return blockType;
	}

	@Override
	public Object parse(InputStream inputStream, Charset charSet) throws SIMPLTranslationException
	{
		// TODO Auto-generated method stub
		return parse(inputStream);
	}
}
