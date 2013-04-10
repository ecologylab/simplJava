/**
 * 
 */
package ecologylab.serialization.library.apple;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_tag;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 */
@simpl_inherit
@simpl_tag("true")
public class TrueProperty extends BooleanProperty
{

	/**
	 * 
	 */
	public TrueProperty()
	{
		this.contents = true;
	}

}
