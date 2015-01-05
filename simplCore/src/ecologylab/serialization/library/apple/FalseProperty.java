/**
 * 
 */
package ecologylab.serialization.library.apple;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_tag;

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
