/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.Hint;
import ecologylab.serialization.simpl_inherit;

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
