/**
 * 
 */
package ecologylab.xml;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ecologylab.generic.Debug;

/**
 *
 *
 * @author andruid
 */
public class SAXHandler extends Debug implements ContentHandler
{
	final TranslationSpace	translationSpace;
	
	final ElementState		root;
	
	private XMLReader 		parser;
	
	/**
	 * 
	 */
	public SAXHandler(TranslationSpace translationSpace, ElementState root)
	{
		this.translationSpace		= translationSpace;
		this.root					= root;

		try 
		{
			parser 					= XMLReaderFactory.createXMLReader();
			parser.setContentHandler(this);
			debug("XMLReader="+parser);
		} catch (Exception e)
		{
			parser					= null;
		}
	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void endElement(String arg0, String arg1, String arg2)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#characters(char[], int, int)
	 */
	public void characters(char[] chars, int startIndex, int length) 
	throws SAXException
	{

		if (false) // if current PTE is leaf
		{
	         String leafValue = new String(chars, startIndex, length);
		}
	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */
	public void endDocument() throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#endPrefixMapping(java.lang.String)
	 */
	public void endPrefixMapping(String prefix) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int, int)
	 */
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#processingInstruction(java.lang.String, java.lang.String)
	 */
	public void processingInstruction(String target, String data)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#setDocumentLocator(org.xml.sax.Locator)
	 */
	public void setDocumentLocator(Locator locator)
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#skippedEntity(java.lang.String)
	 */
	public void skippedEntity(String name) throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#startDocument()
	 */
	public void startDocument() throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 *
	 * ${tags}
	 *
	 * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
	 */
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		println("YO!!!");
		new SAXHandler(null, null);

	}

	public XMLReader getParser()
	{
		return parser;
	}

}
