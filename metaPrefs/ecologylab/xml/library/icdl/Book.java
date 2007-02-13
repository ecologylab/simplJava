/**
 * 
 */
package ecologylab.xml.library.icdl;

import ecologylab.net.ParsedURL;
import ecologylab.xml.ElementState;

/**
 * Book item in ICDL BookXMLResults response.
 * 
 * @author andruid
 */
public class Book extends ElementState
{
	@xml_leaf	String		id;
	@xml_leaf	String		booktitle;
	@xml_leaf	String		languages;
	
	/**
	 * Location of a thumbnail image for the cover of this book.
	 */
	@xml_leaf	ParsedURL	cover;
	/**
	 * Width of the cover image.
	 */
	@xml_leaf	int			width;
	/**
	 * Height of the cover image.
	 */
	@xml_leaf	int			height;
	
	/**
	 * Evidently supposed to enable browsing the book. But it doesn't work.
	 * 
	 * What they provide looks like:
	 * http://www.childrenslibrary.org/icdl/BookPreview?bookid=yusoldm_00500219&amp;summary=true&amp;categories=false&amp;route=simple_133,265_0_0_English_0&amp;lang=English&amp;msg=
	 * 
	 * What works looks like:
	 * http://www.childrenslibrary.org/icdl/BookPreview?bookid=hergran_00030022&summary=true&categories=false&route=simple_0_0_0_English_0&lang=English&msg=
	 */
	@xml_leaf	ParsedURL	bookurl;
	
	
	/**
	 * 
	 */
	public Book()
	{
		super();

	}


	/**
	 * @return Returns the booktitle.
	 */
	public String getBooktitle()
	{
		return booktitle;
	}


	/**
	 * @param booktitle The booktitle to set.
	 */
	public void setBooktitle(String booktitle)
	{
		this.booktitle = booktitle;
	}


	/**
	 * @return Returns the bookurl.
	 */
	public ParsedURL getBookurl()
	{
		return bookurl;
	}


	/**
	 * @param bookurl The bookurl to set.
	 */
	public void setBookurl(ParsedURL bookurl)
	{
		this.bookurl = bookurl;
	}


	/**
	 * @return Returns the cover.
	 */
	public ParsedURL getCover()
	{
		return cover;
	}


	/**
	 * @param cover The cover to set.
	 */
	public void setCover(ParsedURL cover)
	{
		this.cover = cover;
	}


	/**
	 * @return Returns the height.
	 */
	public int getHeight()
	{
		return height;
	}


	/**
	 * @param height The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}


	/**
	 * @return Returns the id.
	 */
	public String getId()
	{
		return id;
	}


	/**
	 * @param id The id to set.
	 */
	public void setId(String id)
	{
		this.id = id;
	}


	/**
	 * @return Returns the languages.
	 */
	public String getLanguages()
	{
		return languages;
	}


	/**
	 * @param languages The languages to set.
	 */
	public void setLanguages(String languages)
	{
		this.languages = languages;
	}


	/**
	 * @return Returns the width.
	 */
	public int getWidth()
	{
		return width;
	}


	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

}
