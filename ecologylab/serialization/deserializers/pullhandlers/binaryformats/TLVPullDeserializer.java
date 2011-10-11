package ecologylab.serialization.deserializers.pullhandlers.binaryformats;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.xml.stream.XMLStreamConstants;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.types.element.IMappable;

public class TLVPullDeserializer extends BinaryPullDeserializer
{
	private static final int	HEADER_SIZE	= 8;

	DataInputStream						inputStream;

	int												blockType;

	int												blockLength;

	boolean										isEos				= false;

	public TLVPullDeserializer(TranslationScope translationScope,
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

	private Object parse() throws SIMPLTranslationException, IOException
	{
		Object root = null;

		nextHeader();

		ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor = translationScope
				.getClassDescriptorByTlvId(type());

		if (rootClassDescriptor == null)
		{
			throw new SIMPLTranslationException(
					"cannot find the class descriptor for root element; make sure if translation scope is correct.");
		}

		root = rootClassDescriptor.getInstance();

		deserializationPreHook(root, translationContext);
		if (deserializationHookStrategy != null)
			deserializationHookStrategy.deserializationPreHook(root, null);

		createObjectModel(root, rootClassDescriptor, type(), length());

		return root;
	}

	private void createObjectModel(Object root,
			ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor, int type, int length)
			throws IOException, SIMPLTranslationException
	{
		FieldDescriptor currentFieldDescriptor = new FieldDescriptor();
		int bytesRead = 0;

		while (!isEos && bytesRead < length)
		{
			bytesRead += nextHeader();

			currentFieldDescriptor = rootClassDescriptor.getFieldDescriptorByTLVId(type());

			int fieldType = currentFieldDescriptor.getType();

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
				}
				break;
			}
		}

	}

	private int deserializeCompositeMap(Object root, FieldDescriptor fd) throws SIMPLTranslationException, IOException
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

	private int deserializeCompositeMapElement(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		Object subRoot;
		int length = length();

		subRoot = getSubRoot(fd, root);
		if (subRoot instanceof IMappable<?>)
		{
			final Object key = ((IMappable<?>) subRoot).key();
			Map map = (Map) fd.automaticLazyGetCollectionOrMap(root);
			map.put(key, subRoot);
		}

		return length;
	}

	private int deserializeCompositeCollectionElement(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		Object subRoot;
		int length = length();

		subRoot = getSubRoot(fd, root);
		Collection collection = (Collection) fd.automaticLazyGetCollectionOrMap(root);
		collection.add(subRoot);
		return length;
	}

	private int deserializeComposite(Object root, FieldDescriptor currentFieldDescriptor)
			throws SIMPLTranslationException, IOException
	{
		int length = length();
		Object subRoot = getSubRoot(currentFieldDescriptor, root);
		currentFieldDescriptor.setFieldToComposite(root, subRoot);
		return length;
	}

	private Object getSubRoot(FieldDescriptor currentFieldDescriptor, Object root)
			throws SIMPLTranslationException, IOException
	{
		Object subRoot = null;
		ClassDescriptor<? extends FieldDescriptor> subRootClassDescriptor = currentFieldDescriptor
				.getChildClassDescriptor(type());

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

		return subRoot;
	}

	private int deserializeScalarCollectionElement(Object root, FieldDescriptor fd)
			throws SIMPLTranslationException, IOException
	{
		byte[] value = new byte[length()];
		inputStream.read(value);
		String stringValue = new String(value);
		fd.addLeafNodeToCollection(root, stringValue, translationContext);
		return length();
	}

	private int deserializeScalar(Object root, FieldDescriptor currentFieldDescriptor)
			throws IOException
	{
		byte[] value = new byte[length()];
		inputStream.read(value);
		String stringValue = new String(value);
		currentFieldDescriptor.setFieldToScalar(root, stringValue, translationContext);
		return length();
	}

	private void configure(InputStream stream)
	{
		inputStream = new DataInputStream(stream);
	}

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

	private int length()
	{
		return blockLength;
	}

	private int type()
	{
		return blockType;
	}
}
