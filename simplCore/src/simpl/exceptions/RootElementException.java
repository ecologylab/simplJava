/**
 * 
 */
package simpl.exceptions;

import ecologylab.serialization.SimplTypesScope;

/**
 * An exception to throw when lookup of the root element fails in the TranslationSpace.
 * 
 * @author andruid
 */
public class RootElementException extends SIMPLTranslationException
{
	/**
	 * @param msg
	 */
	public RootElementException(String tag, SimplTypesScope tSpace)
	{
		super("Can't resolve root element <" + tag + "> in TranslationSpace " + tSpace.getName());
	}


}
