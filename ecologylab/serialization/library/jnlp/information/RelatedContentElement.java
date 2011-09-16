/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * related-content element: The optional related-content element describes an additional piece of related content, such
 * as a readme file, help pages, or links to registration pages, as a hint to a JNLP Client. The application is asking
 * that this content be included in its desktop integration. The related-content element has a mandatory href and title
 * attribute. It can contain any of the following two sub-elements:
 * 
 * description element: A short description of the related content. icon element: The icon can be used by the JNLP
 * Client to identify the related content to the user.
 * 
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class RelatedContentElement extends ElementState
{
    @simpl_scalar private String   href;

    @simpl_scalar private String   title;

    @simpl_composite private Description description;

    @simpl_composite private Icon        icon;

    /**
     * 
     */
    public RelatedContentElement()
    {
        // TODO Auto-generated constructor stub
    }

}
