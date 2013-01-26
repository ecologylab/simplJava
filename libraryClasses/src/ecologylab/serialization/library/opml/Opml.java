/**
 * 
 */
package ecologylab.serialization.library.opml;

import simpl.annotations.dbal.simpl_composite;
import ecologylab.serialization.ElementState;
import ecologylab.serialization.SimplTypesScope;

/**
 * Outline Processor Markup Language root element.
 * 
 * @author andruid
 */
public class Opml extends ElementState
{
	@simpl_composite	Body	body;
	
	/**
	 * 
	 */
	public Opml()
	{
		super();
	}

	public  static final String PACKAGE_NAME				= "ecologylab.serialization.library.opml";
	
	public static final Class TRANSLATIONS[]	= 
	{
		Opml.class,
		Body.class,
		Outline.class,
	};
	/**
	 * Get an appropriate TranslationSpace for OPML.
	 * 
	 * @return
	 */  
	public static SimplTypesScope getTranslationScope()
	{
		return SimplTypesScope.get(PACKAGE_NAME, TRANSLATIONS);
	}
	/**
	 * @return Returns the body.
	 */
	public Body getBody()
	{
		return body;
	}
	/**
	 * @param body The body to set.
	 */
	public void setBody(Body body)
	{
		this.body = body;
	}
}
