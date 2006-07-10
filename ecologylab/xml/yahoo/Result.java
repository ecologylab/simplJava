package ecologylab.xml.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementStateWithLeafElements;

/**
 * Result from a Yahoo Search.
 * 
 * This implementation has fields for "web search" and "image search".
 * I expect other fields will get added for other types of search.
 *
 * @author andruid
 */
public class Result extends ElementStateWithLeafElements
{
	public String				Title;
	public String				Summary;
	/**
	 * For web search, the URL of the document.
	 * For image search, the URL of the image.
	 */
	public ParsedURL			Url;
	// there is also a field called ClickUrl. for image search, it duplicates Url.
	// for web search ClickUrl is that nasty url that takes you through yahoo, and includes
	// a url-encoded : after http, in the middle
	/**
	 * For image search, this is the Container web page!
	 */
	public ParsedURL			RefererUrl;
	
	/**
	 * Another field for image search only. How useful! 
	 * Lets us know if we want to work with the thumbnail or just download the whole image.
	 * Seems to be in bytes.
	 */
	public int					FileSize;
	
	public int					Width;
	public int					Height;
	
	/**
	 * For image search only. This seems to be the file suffix, though they use jpeg instead of jpg.
	 */
	public String				FileFormat;
	
	/**
	 * Specific to news search.
	 */
	public String				NewsSource;
	
	/**
	 * For web search only. Like file format, except its true mime type, like text/html.
	 * Very nice.
	 */
	public String				MimeType;
	
	// DisplayUrl -- boring. Web search only.
	
	// Modification Date -- again, we need a Date type!!! looks like they are providing the long int format
	// For web search and image search.
	
	/**
	 * Cool! For image search, direct access to their thumbnail image.
	 */
	public Thumbnail			thumbnail;
	
	   
	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= 
	{"Title", "Summary", "Url", "RefererUrl", "FileSize", "Width",
	 "Height", "FileFormat", "MimeType", "NewsSource"};
	
	static
	{
		defineLeafElementFieldNames(LEAF_ELEMENT_FIELD_NAMES);
	}

}
