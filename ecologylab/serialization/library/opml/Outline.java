/**
 * 
 */
package ecologylab.serialization.library.opml;

import java.util.ArrayList;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * Nestable Outline Processor Markup Language construct.
 * 
 * @author andruid
 */
@simpl_inherit
public class Outline extends ElementState
{
	@simpl_scalar	String				title;
	@simpl_scalar	ParsedURL			xmlUrl;
	@simpl_scalar	ParsedURL			htmlUrl;
	
	@simpl_collection ArrayList<Outline>	outline; 
	
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
	
	public ArrayList<Outline> getOutline() {
		if (outline != null)
			return outline;
		return outline = new ArrayList<Outline>();
	}

}
