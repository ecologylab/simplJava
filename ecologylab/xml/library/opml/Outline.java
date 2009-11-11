/**
 * 
 */
package ecologylab.xml.library.opml;

import ecologylab.net.ParsedURL;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Nestable Outline Processor Markup Language construct.
 * 
 * @author andruid
 */
@xml_inherit
public class Outline extends ArrayListState<Outline>
{
	@xml_attribute	String		title;
	@xml_attribute	ParsedURL	xmlUrl;
	@xml_attribute	ParsedURL	htmlUrl;
	
	
	/**
	 * 
	 */
	public Outline()
	{
		super();
	}


	/**
	 * @return Returns the htmlUrl.
	 */
	public ParsedURL getHtmlUrl()
	{
		return htmlUrl;
	}


	/**
	 * @param htmlUrl The htmlUrl to set.
	 */
	public void setHtmlUrl(ParsedURL htmlUrl)
	{
		this.htmlUrl = htmlUrl;
	}


	/**
	 * @return Returns the title.
	 */
	public String getTitle()
	{
		return title;
	}


	/**
	 * @param title The title to set.
	 */
	public void setTitle(String title)
	{
		this.title = title;
	}


	/**
	 * @return Returns the xmlUrl.
	 */
	public ParsedURL getXmlUrl()
	{
		return xmlUrl;
	}


	/**
	 * @param xmlUrl The xmlUrl to set.
	 */
	public void setXmlUrl(ParsedURL xmlUrl)
	{
		this.xmlUrl = xmlUrl;
	}

}
