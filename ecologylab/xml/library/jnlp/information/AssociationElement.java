/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class AssociationElement extends ElementState
{
    @xml_attribute @xml_tag("mime-type") private String mimeType;

    @xml_attribute private String                       extensions;

    /**
     * 
     */
    public AssociationElement()
    {
        super();
    }

}
