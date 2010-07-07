package ecologylab.serialization.library.media;

import ecologylab.serialization.ElementState;

/**
 * Leaf node with attributes (so not declared as such).
 * The actual value is a Text node.
 *
 * @author andruid
 */
public class Credit extends ElementState
{
	@simpl_scalar String		role;

	/**
	 * @return Returns the role.
	 */
	protected String getRole()
	{
		return role;
	}

	/**
	 * @param role The role to set.
	 */
	protected void setRole(String role)
	{
		this.role = role;
	}
}
