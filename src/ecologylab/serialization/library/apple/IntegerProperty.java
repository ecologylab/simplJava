/**
 * 
 */
package ecologylab.serialization.library.apple;

import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_nowrap;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

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

}
