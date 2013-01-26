/*
 * Created on Dec 12, 2006
 */
package ecologylab.serialization.library.endnote;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;

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
