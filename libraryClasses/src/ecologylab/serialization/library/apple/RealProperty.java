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

	public double getContents()
	{
		return this.contents;
	}

}
