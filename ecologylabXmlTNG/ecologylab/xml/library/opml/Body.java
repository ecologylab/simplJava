/**
 * 
 */
package ecologylab.xml.library.opml;

import ecologylab.xml.ElementState;

/**
 * OPML <body> element
 * 
 * @author andruid
 */
public class Body extends ElementState
{
	@xml_nested	Outline		outline;
	
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
