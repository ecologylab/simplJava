package ecologylab.serialization.deserializers.pullhandlers.stringformats;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.codehaus.jackson.JsonParseException;

import ecologylab.generic.StringInputStream;
import ecologylab.net.ParsedURL;
import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.DeserializationHookStrategy;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.SIMPLTranslationException;
import ecologylab.serialization.TranslationContext;
import ecologylab.serialization.TranslationScope;
import ecologylab.serialization.types.element.IMappable;

/**
 * Pull API implementation to transform XML documets to corresponding object models. Utilizes
 * XMLStreamReader to get sequential access to tags in XML.
 * 
 * @author nabeel
 */
public class XMLPullDeserializer extends StringPullDeserializer
{

	XMLStreamReader	xmlStreamReader	= null;

	public XMLPullDeserializer(TranslationScope translationScope,
			TranslationContext translationContext, DeserializationHookStrategy deserializationHookStrategy)
	{
		super(translationScope, translationContext, deserializationHookStrategy);
	}

	public XMLPullDeserializer(TranslationScope translationScope,
			TranslationContext translationContext)
	{
		super(translationScope, translationContext);
	}

	/**
	 * Parses a charsequence of the XML document and returns the corresponding object model.
	 * 
	 * @param charSequence
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws SIMPLTranslationException
	 * @throws IOException
	 */
	public Object parse(CharSequence charSequence) throws SIMPLTranslationException
	{
		try
		{
			InputStream xmlStream = new StringInputStream(charSequence, StringInputStream.UTF8);
			xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(xmlStream, "UTF-8");

			Object root = null;

			xmlStreamReader.next();

			if (xmlStreamReader.getEventType() != XMLStreamConstants.START_ELEMENT)
			{
				throw new SIMPLTranslationException("start of an element expected");
			}

			String rootTag = xmlStreamReader.getLocalName();

			ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor = translationScope
					.getClassDescriptorByTag(rootTag);

			if (rootClassDescriptor == null)
			{
				throw new SIMPLTranslationException("cannot find the class descriptor for root element <"
						+ rootTag + ">; make sure if translation scope is correct.");
			}

			root = rootClassDescriptor.getInstance();
			deserializeAttributes(root, rootClassDescriptor);

			createObjectModel(root, rootClassDescriptor);

			return root;
		}
		catch (Exception ex)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", ex);
		}
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
	private void createObjectModel(Object root,
			ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor) throws IOException,
			SIMPLTranslationException, XMLStreamException
	{
		FieldDescriptor currentFieldDescriptor = null;
		Object subRoot = null;
		int event = 0;
		event = nextEvent();
		while (xmlStreamReader.hasNext() && (event) != XMLStreamConstants.END_ELEMENT)
		{
			// debug();
			if (event == XMLStreamConstants.START_ELEMENT)
			{
				currentFieldDescriptor = (currentFieldDescriptor != null)
						&& (currentFieldDescriptor.getType() == IGNORED_ELEMENT) ? FieldDescriptor.IGNORED_ELEMENT_FIELD_DESCRIPTOR
						: (currentFieldDescriptor != null && currentFieldDescriptor.getType() == WRAPPER) ? currentFieldDescriptor
								.getWrappedFD()
								: rootClassDescriptor.getFieldDescriptorByTag(xmlStreamReader.getLocalName(),
										translationScope, null);

				int fieldType = currentFieldDescriptor.getType();

				switch (fieldType)
				{
				case SCALAR:
					xmlStreamReader.next();
					String value = xmlStreamReader.getText();
					currentFieldDescriptor.setFieldToScalar(root, value, translationContext);
					xmlStreamReader.next();
					event = nextEvent();
					break;
				case COMPOSITE_ELEMENT:
					String tagName = xmlStreamReader.getLocalName();
					subRoot = getSubRoot(currentFieldDescriptor, tagName);
					currentFieldDescriptor.setFieldToComposite(root, subRoot);
					event = nextEvent();
					break;
				case COLLECTION_ELEMENT:
					while (currentFieldDescriptor.isCollectionTag(xmlStreamReader.getLocalName()))
					{
						if (event == XMLStreamConstants.START_ELEMENT)
						{
							String compositeTagName = xmlStreamReader.getLocalName();
							subRoot = getSubRoot(currentFieldDescriptor, compositeTagName);
							Collection collection = (Collection) currentFieldDescriptor
									.automaticLazyGetCollectionOrMap(root);
							collection.add(subRoot);

							event = xmlStreamReader.nextTag();							
						}
					}
					break;
				case MAP_ELEMENT:
					while (currentFieldDescriptor.isCollectionTag(xmlStreamReader.getLocalName()))
					{
						if (event == XMLStreamConstants.START_ELEMENT)
						{
							String compositeTagName = xmlStreamReader.getLocalName();
							subRoot = getSubRoot(currentFieldDescriptor, compositeTagName);
							if (subRoot instanceof IMappable)
							{
								final Object key = ((IMappable) subRoot).key();
								Map map = (Map) currentFieldDescriptor.automaticLazyGetCollectionOrMap(root);
								map.put(key, subRoot);
							}

							event = xmlStreamReader.nextTag();
						}
					}
					break;
				case WRAPPER:
					event = nextEvent();
					break;
				default:
					event = nextEvent();
				}
			}
			else
			{
				event = nextEvent();
			}
		}
	}

	/**
	 * Gets the sub root of the object model if its a composite object. Does graph handling/ Handles
	 * simpl:ref tag to assign an already created instance of the composite object instead of creating
	 * a new one
	 * 
	 * @param currentFieldDescriptor
	 * @return
	 * @throws SIMPLTranslationException
	 * @throws JsonParseException
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private Object getSubRoot(FieldDescriptor currentFieldDescriptor, String tagName)
			throws SIMPLTranslationException, IOException, XMLStreamException
	{
		Object subRoot = null;
		ClassDescriptor<? extends FieldDescriptor> subRootClassDescriptor = currentFieldDescriptor
				.getChildClassDescriptor(tagName);

		String simplReference = null;

		if ((simplReference = getSimpleReference()) != null)
		{
			subRoot = translationContext.getFromMap(simplReference);
			xmlStreamReader.next();
		}
		else
		{
			subRoot = subRootClassDescriptor.getInstance();
			deserializeAttributes(subRoot, subRootClassDescriptor);
			createObjectModel(subRoot, subRootClassDescriptor);
		}

		return subRoot;
	}

	/**
	 * 
	 * @return
	 */
	private String getSimpleReference()
	{
		String simplReference = null;

		for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++)
		{
			String attributePrefix = xmlStreamReader.getAttributePrefix(i);
			String tag = xmlStreamReader.getAttributeLocalName(i);
			String value = xmlStreamReader.getAttributeValue(i);

			if (TranslationContext.SIMPL.equals(attributePrefix))
			{
				if (tag.equals(TranslationContext.REF))
				{
					simplReference = value;
				}
			}
		}

		return simplReference;
	}

	/**
	 * 
	 * @param root
	 * @param rootClassDescriptor
	 * @return
	 */
	private boolean deserializeAttributes(Object root,
			ClassDescriptor<? extends FieldDescriptor> rootClassDescriptor)
	{
		for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++)
		{
			String attributePrefix = xmlStreamReader.getAttributePrefix(i);
			String tag = xmlStreamReader.getAttributeLocalName(i);
			String value = xmlStreamReader.getAttributeValue(i);

			if (TranslationContext.SIMPL.equals(attributePrefix))
			{
				if (tag.equals(TranslationContext.ID))
				{
					translationContext.markAsUnmarshalled(value, root);
				}
			}
			else
			{
				FieldDescriptor attributeFieldDescriptor = rootClassDescriptor.getFieldDescriptorByTag(tag,
						translationScope);

				if (attributeFieldDescriptor != null)
				{
					attributeFieldDescriptor.setFieldToScalar(root, value, translationContext);
				}
				else
				{
					debug("ignoring attribute: " + tag);
				}
			}
		}

		return true;
	}

	/**
	 * 
	 * @return
	 * @throws XMLStreamException
	 */
	private int nextEvent() throws XMLStreamException
	{
		int eventType = 0;

		// skip events that we don't handle.
		while (xmlStreamReader.hasNext())
		{
			eventType = xmlStreamReader.next();
			if (xmlStreamReader.getEventType() == XMLStreamConstants.START_DOCUMENT
					|| xmlStreamReader.getEventType() == XMLStreamConstants.START_ELEMENT
					|| xmlStreamReader.getEventType() == XMLStreamConstants.END_ELEMENT
					|| xmlStreamReader.getEventType() == XMLStreamConstants.END_DOCUMENT
					|| xmlStreamReader.getEventType() == XMLStreamConstants.CHARACTERS)
			{
				break;
			}
		}

		return eventType;
	}

	protected void debug()
	{
		int event = xmlStreamReader.getEventType();
		switch (event)
		{
		case XMLStreamConstants.START_ELEMENT:
			System.out.println(xmlStreamReader.getLocalName());
			break;
		case XMLStreamConstants.END_ELEMENT:
			System.out.println(xmlStreamReader.getLocalName());
			break;
		case XMLStreamConstants.CHARACTERS:
			System.out.println(xmlStreamReader.getText());
			break;
		case XMLStreamConstants.CDATA:
			System.out.println("cdata " + xmlStreamReader.getText());
			break;
		} // end switch
	}

	/**
	 * 
	 * @throws XMLStreamException
	 */
	protected void printParse() throws XMLStreamException
	{
		int event;
		while (xmlStreamReader.hasNext())
		{
			event = xmlStreamReader.getEventType();
			switch (event)
			{
			case XMLStreamConstants.START_ELEMENT:
				System.out.println(xmlStreamReader.getLocalName());
				break;
			case XMLStreamConstants.END_ELEMENT:
				System.out.println(xmlStreamReader.getLocalName());
				break;
			case XMLStreamConstants.CHARACTERS:
				System.out.println(xmlStreamReader.getText());
				break;
			case XMLStreamConstants.CDATA:
				System.out.println("cdata " + xmlStreamReader.getText());
				break;
			} // end switch
			xmlStreamReader.next();
		} // end while
	}

	@Override
	public Object parse(File file)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object parse(ParsedURL purl)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
