package ecologylab.serialization.library.media;

import simpl.annotations.dbal.simpl_scalar;
import ecologylab.serialization.ElementState;

/**
 * Media leaf node, but not marked up as such cause it has its own attribute.
 *
 * @author andruid
 */
public class Description extends ElementState
{
/**
 * Can be plain or html. Plain is the default.
 */
	@simpl_scalar String			type;

	/**
	 * @return Returns the type.
	 */
	protected String getType()
	{
		return type;
	}
	
	/**
	 * @param type The type to set.
	 */
	protected void setType(String type)
	{
		this.type = type;
	}
	
	/**
	 * A text node will be here with the actual description.
	 */
}
