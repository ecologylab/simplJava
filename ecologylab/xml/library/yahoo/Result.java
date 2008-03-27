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
	@xml_leaf	String				Title;
	@xml_leaf	String				Summary;
	/**
	 * For web search, the URL of the document.
	 * For image search, the URL of the image.
	 */
	@xml_leaf	ParsedURL			Url;
	// there is also a field called ClickUrl. for image search, it duplicates Url.
	// for web search ClickUrl is that nasty url that takes you through yahoo, and includes
	// a url-encoded : after http, in the middle
	/**
	 * For image search, this is the Container web page!
	 */
	@xml_leaf	ParsedURL			RefererUrl;
	
	/**
	 * Another field for image search only. How useful! 
	 * Lets us know if we want to work with the thumbnail or just download the whole image.
	 * Seems to be in bytes.
	 */
	@xml_leaf	int					FileSize;
	
	@xml_leaf	int					Width;
	@xml_leaf	int					Height;
	
	/**
	 * For image search only. This seems to be the file suffix, though they use jpeg instead of jpg.
	 */
	@xml_leaf	String				FileFormat;
	
	/**
	 * Specific to news search.
	 */
	@xml_leaf	String				NewsSource;
	
	/**
	 * For web search only. Like file format, except its true mime type, like text/html.
	 * Very nice.
	 */
	@xml_leaf	String				MimeType;
	
	// DisplayUrl -- boring. Web search only.
	
	// Modification Date -- again, we need a Date type!!! looks like they are providing the long int format
	// For web search and image search.
	
	/**
	 * Cool! For image search, direct access to their thumbnail image.
	 */
	@xml_nested	ThumbnailState			Thumbnail;
	
	   
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

	public String getTitle()
	{
		return Title;
	}

	public void setTitle(String title)
	{
		Title = title;
	}

	public String getSummary()
	{
		return Summary;
	}

	public void setSummary(String summary)
	{
		Summary = summary;
	}

	public ParsedURL getUrl()
	{
		return Url;
	}

	public void setUrl(ParsedURL url)
	{
		Url = url;
	}

	public ParsedURL getRefererUrl()
	{
		return RefererUrl;
	}

	public void setRefererUrl(ParsedURL refererUrl)
	{
		RefererUrl = refererUrl;
	}

	public int getFileSize()
	{
		return FileSize;
	}

	public void setFileSize(int fileSize)
	{
		FileSize = fileSize;
	}

	public int getWidth()
	{
		return Width;
	}

	public void setWidth(int width)
	{
		Width = width;
	}

	public int getHeight()
	{
		return Height;
	}

	public void setHeight(int height)
	{
		Height = height;
	}

	public String getFileFormat()
	{
		return FileFormat;
	}

	public void setFileFormat(String fileFormat)
	{
		FileFormat = fileFormat;
	}

	public String getNewsSource()
	{
		return NewsSource;
	}

	public void setNewsSource(String newsSource)
	{
		NewsSource = newsSource;
	}

	public String getMimeType()
	{
		return MimeType;
	}

	public void setMimeType(String mimeType)
	{
		MimeType = mimeType;
	}

	public ThumbnailState getThumbnail()
	{
		return Thumbnail;
	}

	public void setThumbnail(ThumbnailState thumbnail)
	{
		Thumbnail = thumbnail;
	}
}
