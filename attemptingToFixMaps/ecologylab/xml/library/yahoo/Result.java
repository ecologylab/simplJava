package ecologylab.xml.library.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

/**
 * Result from a Yahoo Search.
 * 
 * This implementation has fields for "web search" and "image search".
 * I expect other fields will get added for other types of search.
 *
 * @author andruid
 */
public class Result extends ElementState
{
	public @xml_leaf	String				Title;
	@xml_leaf	public String				Summary;
	/**
	 * For web search, the URL of the document.
	 * For image search, the URL of the image.
	 */
	@xml_leaf	public ParsedURL			Url;
	// there is also a field called ClickUrl. for image search, it duplicates Url.
	// for web search ClickUrl is that nasty url that takes you through yahoo, and includes
	// a url-encoded : after http, in the middle
	/**
	 * For image search, this is the Container web page!
	 */
	@xml_leaf	public ParsedURL			RefererUrl;
	
	/**
	 * Another field for image search only. How useful! 
	 * Lets us know if we want to work with the thumbnail or just download the whole image.
	 * Seems to be in bytes.
	 */
	@xml_leaf	public int					FileSize;
	
	@xml_leaf	public int					Width;
	@xml_leaf	public int					Height;
	
	/**
	 * For image search only. This seems to be the file suffix, though they use jpeg instead of jpg.
	 */
	@xml_leaf	public String				FileFormat;
	
	/**
	 * Specific to news search.
	 */
	@xml_leaf	public String				NewsSource;
	
	/**
	 * For web search only. Like file format, except its true mime type, like text/html.
	 * Very nice.
	 */
	@xml_leaf	public String				MimeType;
	
	// DisplayUrl -- boring. Web search only.
	
	// Modification Date -- again, we need a Date type!!! looks like they are providing the long int format
	// For web search and image search.
	
	/**
	 * Cool! For image search, direct access to their thumbnail image.
	 */
	public ThumbnailState			Thumbnail;
	
	   
	static final String[]		LEAF_ELEMENT_FIELD_NAMES	= 
	{"Title", "Summary", "Url", "RefererUrl", "FileSize", "Width",
	 "Height", "FileFormat", "MimeType", "NewsSource"};
	
	/**
	 * The array of Strings with the names of the leaf elements.
	 * 
	 * @return
	 */
	protected String[] leafElementFieldNames()
	{
		return LEAF_ELEMENT_FIELD_NAMES;
	}
}
