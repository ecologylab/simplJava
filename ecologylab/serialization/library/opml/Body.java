/**
 * 
 */
package ecologylab.serialization.library.opml;

import ecologylab.serialization.ElementState;

/**
 * OPML <body> element
 * 
 * @author andruid
 */
public class Body extends ElementState
{
	@simpl_composite	Outline		outline;
	
	/**
	 * 
	 */
	public Body()
	{
		super();

	}

	/**
	 * @return Returns the outline.
	 */
	public Outline getOutline()
	{
		return outline;
	}

	/**
	 * @param outline The outline to set.
	 */
	public void setOutline(Outline outline)
	{
		this.outline = outline;
	}

}
