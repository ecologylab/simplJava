/**
 * 
 */
package simpl.descriptions;

import simpl.core.ElementState;
import simpl.core.SimplTypesScope;
import ecologylab.generic.Debug;

/**
 * Declare an XML Namespace, by mapping a URI String to an ElementState subclass.
 * 
 * @author andruid
 */
public class NameSpaceDecl extends Debug
{
	public final String							urn;
	public final Class<? extends ElementState>		esClass;
	final SimplTypesScope					translationScope;
	
	/**
	 * 
	 */
	public NameSpaceDecl(String urn, Class<? extends ElementState> esClass, SimplTypesScope translationScope)
	{
		this.urn				= urn;
		this.esClass			= esClass;
		this.translationScope	= translationScope;
	}

}
