package ecologylab.services.messages.cf;

import ecologylab.net.ParsedURL;
import ecologylab.xml.xml_inherit;

/**
 * {@link Seed Seed} element used to tell combinFormation to process a document.
 * 
 * Version for client only:
 * 	<li>data slot definitions only with no other functionality.</li>
 *
 * @author andruid
 */
@xml_inherit
public class DocumentState extends Seed
{
	/**
	 * URL of the document or container specified for downloading and processing.
	 */
	@xml_attribute		ParsedURL	url;
	
	/**
	 * If true, then no media should be collected from this document.
	 * Rather, it will be treated as a collection of links, that will be fed to the focused web crawler agent.
	 */
	@xml_attribute		boolean		justcrawl;
	/**
	 * If true, then no links should be collected from this document and fed to the focused web crawler agent.
	 * Instead, only collect media to form image and text surrogates.
	 */
	@xml_attribute		boolean		justmedia;
	

	public DocumentState()
	{
		super();

	}


	/**
	 * @return Returns the justcrawl.
	 */
	public boolean isJustcrawl()
	{
		return justcrawl;
	}


	/**
	 * @param justcrawl The justcrawl to set.
	 */
	public void setJustcrawl(boolean justcrawl)
	{
		this.justcrawl = justcrawl;
	}


	/**
	 * @return Returns the justmedia.
	 */
	public boolean isJustmedia()
	{
		return justmedia;
	}


	/**
	 * @param justmedia The justmedia to set.
	 */
	public void setJustmedia(boolean justmedia)
	{
		this.justmedia = justmedia;
	}


	/**
	 * @return Returns the url.
	 */
	public ParsedURL getUrl()
	{
		return url;
	}


	/**
	 * @param url The url to set.
	 */
	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}

}
