/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class AssociationElement extends ElementState
{
    @simpl_scalar @simpl_tag("mime-type") private String mimeType;

    @simpl_scalar private String                       extensions;

    /**
     * 
     */
    public AssociationElement()
    {
        super();
    }

}
