/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import simpl.annotations.dbal.simpl_scalar;
import simpl.annotations.dbal.simpl_tag;
import ecologylab.serialization.ElementState;

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
