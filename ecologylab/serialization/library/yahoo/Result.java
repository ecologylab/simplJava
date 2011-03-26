package ecologylab.serialization.library.yahoo;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.ElementState.xml_tag;
import ecologylab.serialization.Hint;

/**
 * Result from a Yahoo Search.
 * 
 * This implementation has fields for "web search" and "image search".
 * I expect other fields will get added for other types of search.
 *
 * @author andruid
 */
public 
@xml_tag("Result")
class Result extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Title")	String				title;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Summary")	String				summary;
	/**
	 * For web search, the URL of the document.
	 * For image search, the URL of the image.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Url")		ParsedURL			url;

	// this is a mess double-stuffed url from yahoo. nice to ignore.
//	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("ClickUrl")		ParsedURL	clickUrl;
	
	// there is also a field called ClickUrl. for image search, it duplicates Url.
	// for web search ClickUrl is that nasty url that takes you through yahoo, and includes
	// a url-encoded : after http, in the middle
	/**
	 * For image search, this is the Container web page!
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("RefererUrl")	ParsedURL		refererUrl;
	
	/**
	 * Another field for image search only. How useful! 
	 * Lets us know if we want to work with the thumbnail or just download the whole image.
	 * Seems to be in bytes.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("FileSize")	int				fileSize;
	
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Width")		int				width;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("Height")		int				height;
	
	/**
	 * For image search only. This seems to be the file suffix, though they use jpeg instead of jpg.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("FileFormat")	String			fileFormat;
	
	/**
	 * Specific to news search.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("NewsSource")	String			newsSource;
	
	/**
	 * For web search only. Like file format, except its true mime type, like text/html.
	 * Very nice.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	@xml_tag("MimeType")	String			mimeType;
	
	// DisplayUrl -- boring. Web search only.
	
	// Modification Date -- again, we need a Date type!!! looks like they are providing the long int format
	// For web search and image search.
	
	/**
	 * Cool! For image search, direct access to their thumbnail image.
	 */
	@simpl_composite	@xml_tag("Thumbnail")	ThumbnailState	thumbnail;
	
	   
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public ParsedURL getUrl()
	{
		return url;
	}

	public void setUrl(ParsedURL url)
	{
		this.url = url;
	}

	public ParsedURL getRefererUrl()
	{
		return refererUrl;
	}

	public void setRefererUrl(ParsedURL refererUrl)
	{
	    this.refererUrl = refererUrl;
	}

	public int getFileSize()
	{
		return fileSize;
	}

	public void setFileSize(int fileSize)
	{
		this.fileSize = fileSize;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public String getFileFormat()
	{
		return fileFormat;
	}

	public void setFileFormat(String fileFormat)
	{
		this.fileFormat = fileFormat;
	}

	public String getNewsSource()
	{
		return newsSource;
	}

	public void setNewsSource(String newsSource)
	{
		this.newsSource = newsSource;
	}

	public String getMimeType()
	{
		return mimeType;
	}

	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	public ThumbnailState getThumbnail()
	{
		return this.thumbnail;
	}

	public void setThumbnail(ThumbnailState thumbnail)
	{
		this.thumbnail = thumbnail;
	}
}
