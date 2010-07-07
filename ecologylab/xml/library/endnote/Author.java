/*
 * Created on Dec 12, 2006
 */
package ecologylab.xml.library.endnote;

import ecologylab.xml.ElementState;
import ecologylab.xml.Hint;
import ecologylab.xml.simpl_inherit;

public @simpl_inherit
class Author extends ElementState
{
	@simpl_scalar @simpl_hints(Hint.XML_TEXT)
	String	authorName;

	public Author()
	{

	}

	/**
	 * @return the authorName
	 */
	public String getAuthorName()
	{
		return authorName;
	}
}
