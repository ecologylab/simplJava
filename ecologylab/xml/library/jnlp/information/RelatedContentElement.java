/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;

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
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class RelatedContentElement extends ElementState
{
    @xml_attribute private String   href;

    @xml_attribute private String   title;

    @xml_nested private Description description;

    @xml_nested private Icon        icon;

    /**
     * 
     */
    public RelatedContentElement()
    {
        // TODO Auto-generated constructor stub
    }

}
