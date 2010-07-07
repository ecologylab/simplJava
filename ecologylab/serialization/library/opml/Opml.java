/**
 * 
 */
package ecologylab.serialization.library.opml;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.TranslationScope;

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
	public static TranslationScope getTranslationScope()
	{
		return TranslationScope.get(PACKAGE_NAME, TRANSLATIONS);
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
