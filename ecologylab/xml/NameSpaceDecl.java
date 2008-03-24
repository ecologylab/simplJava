/**
 * 
 */
package ecologylab.xml;

import ecologylab.generic.Debug;

/**
 * Declare an XML Namespace, by mapping a URI String to an ElementState subclass.
 * 
 * @author andruid
 */
public class NameSpaceDecl extends Debug
{
	final String							urn;
	final Class<? extends ElementState>		esClass;
	final TranslationScope					translationSpace;
	
	/**
	 * 
	 */
	public NameSpaceDecl(String urn, Class<? extends ElementState> esClass, TranslationScope translationSpace)
	{
		this.urn				= urn;
		this.esClass			= esClass;
		this.translationSpace	= translationSpace;
	}

}
