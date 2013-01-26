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
@simpl_tag("data")
@simpl_inherit
public class DataProperty extends Property
{
	@simpl_hints(
	{ Hint.XML_TEXT })
	@simpl_scalar
	String	contents;

	/**
	 * 
	 */
	public DataProperty()
	{
		// TODO Auto-generated constructor stub
	}

}
