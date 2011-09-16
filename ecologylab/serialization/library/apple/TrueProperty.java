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
