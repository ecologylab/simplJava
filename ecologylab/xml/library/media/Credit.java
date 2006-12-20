package ecologylab.xml.library.media;

import ecologylab.xml.ElementState;

/**
 * Leaf node with attributes (so not declared as such).
 * The actual value is a Text node.
 *
 * @author andruid
 */
public class Credit extends ElementState
{
	@xml_attribute String		role;

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
