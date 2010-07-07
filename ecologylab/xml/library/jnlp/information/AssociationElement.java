/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class AssociationElement extends ElementState
{
    @simpl_scalar @xml_tag("mime-type") private String mimeType;

    @simpl_scalar private String                       extensions;

    /**
     * 
     */
    public AssociationElement()
    {
        super();
    }

}
