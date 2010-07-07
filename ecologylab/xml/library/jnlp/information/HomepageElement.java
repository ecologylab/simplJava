/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;

/**
 * Contains a single attribute, href, which is a URL locating the home page for the Application. It is used by the Java
 * Application Cache Viewer to point the user to a Web page where more information about the application can be found.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class HomepageElement extends ElementState
{
    private @simpl_scalar String href;

    /**
     * 
     */
    public HomepageElement()
    {
        super();
    }

}
