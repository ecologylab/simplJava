package ecologylab.serialization.deserializers.pullhandlers.stringformats;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import ecologylab.serialization.SIMPLTranslationException;

public class XMLParserAndroid implements XMLParser
{
  private XmlPullParser	xmlPullParser;

  public XMLParserAndroid(XmlPullParser parser)
  {
  	xmlPullParser = parser;
  }
  
  public XmlPullParser getParser()
  {
  	return xmlPullParser;
  }
  
  public void setParser(XmlPullParser parser)
  {
  	xmlPullParser = parser;
  }
  
	public int eventTypeTransfer(int event)
	{
		switch (event)
		{
		case (XmlPullParser.START_TAG):
			return START_ELEMENT;
		case (XmlPullParser.END_TAG):
			return END_ELEMENT;
		case (XmlPullParser.START_DOCUMENT):
			return START_DOCUMENT;
		case (XmlPullParser.END_DOCUMENT):
			return END_DOCUMENT;
		case (XmlPullParser.TEXT):
			return CHARACTERS;
		case (XmlPullParser.CDSECT):
			return CDATA;
		default:
			return ELSE;
		}
	}

	public int getEventType() throws SIMPLTranslationException
	{
		try
		{
			return eventTypeTransfer(xmlPullParser.getEventType());
		}
		catch (XmlPullParserException e)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", e);
		}
	}

	public String getText()
	{
		return xmlPullParser.getText();
	}

	public String getName()
	{
		return xmlPullParser.getName();
	}

	public int next() throws SIMPLTranslationException
	{
		try
		{
			return eventTypeTransfer(xmlPullParser.next());
		}
		catch (XmlPullParserException e)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", e);
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", e);
		}
	}

	public int nextTag() throws SIMPLTranslationException
	{
		 try
		{
			return eventTypeTransfer(xmlPullParser.nextTag());
		}
		catch (XmlPullParserException e)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", e);
		}
		catch (IOException e)
		{
			throw new SIMPLTranslationException("exception occurred in deserialzation ", e);
		}
	}

	public String getPrefix()
	{
		return xmlPullParser.getPrefix();
	}

	public String getLocalName()
	{
		return xmlPullParser.getName();
	}

	public int getAttributeCount()
	{
		return xmlPullParser.getAttributeCount();
	}

	public String getAttributeLocalName(int index)
	{
		return xmlPullParser.getAttributeName(index);
	}

	public String getAttributePrefix(int index)
	{
		return xmlPullParser.getAttributePrefix(index);
	}

	public String getAttributeValue(int index)
	{
		return xmlPullParser.getAttributeValue(index);
	}

}
