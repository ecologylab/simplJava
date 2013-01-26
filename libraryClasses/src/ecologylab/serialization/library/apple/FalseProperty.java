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
@simpl_tag("false")
public class FalseProperty extends BooleanProperty
{

	/**
	 * 
	 */
	public FalseProperty()
	{
		this.contents = false;
	}

}
