package ecologylab.serialization.deserializers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.jackson.JsonParseException;

import ecologylab.generic.Debug;
import ecologylab.generic.StringInputStream;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.FieldTypes;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.ScalarUnmarshallingContext;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;

public class XMLPullDeserializer extends Debug implements ScalarUnmarshallingContext, FieldTypes
{
	TranslationScope						translationScope;

	TranslationContext					translationContext;

	DeserializationHookStrategy	deserializationHookStrategy;

	XMLStreamReader							xmlStreamReader	= null;

	/**
	 * Constructs that creates a XML deserialization handler
	 * 
	 * @param translationScope
	 *          translation scope to use for de/serializing subsequent char sequences
	 * @param translationContext
	 *          used for graph handling
	 */
	public XMLPullDeserializer(TranslationScope translationScope,
			TranslationContext translationContext)
	{
		this.translationScope = translationScope;
		this.translationContext = translationContext;
		this.deserializationHookStrategy = null;
	}

	public Object parse(CharSequence charSequence) throws XMLStreamException,
			FactoryConfigurationError, SIMPLTranslationException, JsonParseException, IOException
	{
		InputStream xmlStream = new StringInputStream(charSequence, StringInputStream.UTF8);
		xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(xmlStream, "UTF-8");

		Object root = null;

		ClassDescriptor rootClassDescriptor = translationScope.getClassDescriptorByTag(xmlStreamReader
				.getLocalName());

		root = rootClassDescriptor.getInstance();

		createObjectModel(root, rootClassDescriptor);

		return root;
	}

	/**
	 * Recursive method that moves forward in the CharSequence through JsonParser to create a
	 * corresponding object model
	 * 
	 * @param root
	 *          instance of the root element created by the calling method
	 * @param rootClassDescriptor
	 *          instance of the classdescriptor of the root element created by the calling method
	 * @throws JsonParseException
	 * @throws IOException
	 * @throws SIMPLTranslationException
	 * @throws XMLStreamException
	 */
	private void createObjectModel(Object root, ClassDescriptor rootClassDescriptor)
			throws JsonParseException, IOException, SIMPLTranslationException, XMLStreamException
	{
		FieldDescriptor currentFieldDescriptor = null;
		Object subRoot = null;
		while (xmlStreamReader.hasNext())
		{
			xmlStreamReader.next();

			currentFieldDescriptor = (currentFieldDescriptor != null)
					&& (currentFieldDescriptor.getType() == IGNORED_ELEMENT) ? FieldDescriptor.IGNORED_ELEMENT_FIELD_DESCRIPTOR
					: (currentFieldDescriptor != null && currentFieldDescriptor.getType() == WRAPPER) ? currentFieldDescriptor
							.getWrappedFD()
							: rootClassDescriptor.getFieldDescriptorByTag(xmlStreamReader.getLocalName(),
									translationScope, null);

			int fieldType = currentFieldDescriptor.getType();
			
			switch(fieldType)
			{
			case SCALAR: 
				
			}
			
			if (xmlStreamReader.getEventType() == XMLStreamReader.START_ELEMENT)
			{
				System.out.println(xmlStreamReader.getLocalName());
			}

		}

	}

	@Override
	public File fileContext()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ParsedURL purlContext()
	{
		// TODO Auto-generated method stub
		return null;
	}
}
