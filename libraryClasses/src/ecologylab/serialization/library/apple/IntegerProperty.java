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
@simpl_tag("integer")
@simpl_inherit
public class IntegerProperty extends Property
{
	@simpl_hints(
	{ Hint.XML_TEXT })
	@simpl_scalar
	int	contents;

	/**
	 * 
	 */
	public IntegerProperty()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString()
	{
		return "IntegerProperty: "+contents;
	}
}
