/**
 * 
 */
package ecologylab.tests;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_composite;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public @simpl_tag("CLASS_NAME_TAG") class ClassTagged extends ElementState
{
    @simpl_scalar @simpl_tag("BLARG") String blarg = null;
    @simpl_composite @simpl_tag("ASDF:NU") FieldTagged fieldTagged = new FieldTagged();
    
    /**
     * 
     */
    public ClassTagged()
    {
        blarg = "blarg";
    }

}
