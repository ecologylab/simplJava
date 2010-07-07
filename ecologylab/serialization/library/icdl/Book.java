/**
 * 
 */
package ecologylab.serialization.library.icdl;

import ecologylab.net.ParsedURL;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;

/**
 * Book item in ICDL BookXMLResults response.
 * 
 * http://www.childrenslibrary.org/icdl/BookXMLResults?ids=133,265&amp;prefcollids=474&amp;lang=English&amp;sort=title&amp;ptype=simple
 * 
 * ids: 113	comic_book 		http://www.childrenslibrary.org/library/basic/images/action_adventure_round.gifhttp://www.childrenslibrary.org/library/basic/images/comic_book_round.gif
 * 		133 make_believe_books
 * 		163	action_adventure	http://www.childrenslibrary.org/library/basic/images/action_adventure_round.gif
 * 		170 poetry
 * 		155 mythology_folktales
 * 		166 science_fiction_fantasy
 * 		168 funny / humorous *
 * 		167 scary / horror *
 * 		169	fairy tales and folk tales fairy_folk_tales_round
 * 		265	award winning *
 * 		418	recently added newbooks_round
 * 		300 imaginary creature characters		imaginary_beasts_creature_round
 * 		303	kid characters			kids_round
 * 		301 real animal characters	animals_round
 * 
 * 		16	picture books
 * 		
 * 		fairy_folk_tales_round_over
 * 
 * @author andruid
 */
public class Book extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		id;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		booktitle;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	String		languages;
	
	/**
	 * Location of a thumbnail image for the cover of this book.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL	cover;
	/**
	 * Width of the cover image.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	int			width;
	/**
	 * Height of the cover image.
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	int			height;
	
	/**
	 * Evidently supposed to enable browsing the book. But it doesn't work.
	 * 
	 * What they provide looks like:
	 * http://www.childrenslibrary.org/icdl/BookPreview?bookid=yusoldm_00500219&amp;summary=true&amp;categories=false&amp;route=simple_133,265_0_0_English_0&amp;lang=English&amp;msg=
	 * 
	 * What works looks like:
	 * http://www.childrenslibrary.org/icdl/BookPreview?bookid=hergran_00030022&summary=true&categories=false&route=simple_0_0_0_English_0&lang=English&msg=
	 * 
	 */
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	ParsedURL	bookurl;
	
	
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
