/**
 * 
 */
package ecologylab.serialization.library.icdl;

import java.util.ArrayList;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_nowrap;
import simpl.annotations.dbal.simpl_scalar;

import ecologylab.serialization.ElementState;
/**
 * The root element in a reply to ICDL BookXMLResults.
 * 
 * http://www.childrenslibrary.org/icdl/BookXMLResults?ids=133,265&viewids=&prefcollids=474&langid=&text=&lang=English&match=all&sort=title&pnum=1&pgct=8&ptype=simple
 * can be reduced to
 * http://www.childrenslibrary.org/icdl/BookXMLResults?ids=133,265&prefcollids=474&lang=English&sort=title&ptype=simple
 * @author andruid
 */
@simpl_inherit
public class Response extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	int		pnum;
	@simpl_scalar @simpl_hints(Hint.XML_LEAF)	int		total;
	
	@simpl_collection("Book")
	@simpl_nowrap
	ArrayList<Book> books;
	
	/**
	 * 
	 */
	public Response()
	{
		super();

	}
	
	public ArrayList<Book> getBooks()
	{
		if (books != null)
			return books;
		return books = new ArrayList<Book>();
	}

}
