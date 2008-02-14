/**
 * 
 */
package ecologylab.xml;

/**
 * An exception to throw when lookup of the root element fails in the TranslationSpace.
 * 
 * @author andruid
 */
public class RootElementException extends XMLTranslationException
{
	/**
	 * @param msg
	 */
	public RootElementException(String tag, TranslationSpace tSpace)
	{
		super("Can't resolve root element <" + tag + "> in TranslationSpace " + tSpace.getName());
	}


}
