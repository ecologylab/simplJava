/**
 * 
 */
package ecologylab.xml.library.media;

import ecologylab.xml.ElementState;

/**
 * Yahoo Media XML Namespace Group element.
 * Found in abcnews.
 * 
 * @author andruid
 */
public class Group extends ElementState
{
	@simpl_composite	Thumbnail 	thumbnail;
	
	/**
	 * Lookup a NestedNameSpace element child of this, in case there is one,
	 * declared as xmlns:media.
	 * Yahoo Media metadata declarations.
	 * 
	 * @return Returns the Media nested namespace element, or null..
	 */
	public Media lookupMedia()
	{
		return (Media) lookupNestedNameSpace("media");
	}
	   
	
}
