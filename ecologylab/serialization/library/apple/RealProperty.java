/**
 * 
 */
package ecologylab.serialization.library.apple;

import ecologylab.serialization.annotations.Hint;
import ecologylab.serialization.annotations.simpl_hints;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_tag("real")
@simpl_inherit
public class RealProperty extends Property
{
	@simpl_hints(
	{ Hint.XML_TEXT })
	@simpl_scalar
	double	contents;

	/**
	 * 
	 */
	public RealProperty()
	{
		// TODO Auto-generated constructor stub
	}

}
