/**
 * 
 */
package ecologylab.serialization.library.apple;

import simpl.annotations.dbal.Hint;
import simpl.annotations.dbal.simpl_hints;
import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_tag("string")
@simpl_inherit
public class StringProperty extends Property
{
	@simpl_hints(
	{ Hint.XML_TEXT })
	@simpl_scalar
	String	contents;

	/**
	 * 
	 */
	public StringProperty()
	{
	}

	public StringProperty(String string)
	{
		this.contents = string;
	}

	public String getContents()
	{
		return contents;
	}

	@Override
	public String toString()
	{
		return "StringProperty: " + contents;
	}
}
