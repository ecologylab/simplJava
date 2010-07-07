/**
 * 
 */
package ecologylab.tests;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_tag;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public @xml_tag("CLASS_NAME_TAG") class ClassTagged extends ElementState
{
    @simpl_scalar @xml_tag("BLARG") String blarg = null;
    @simpl_composite @xml_tag("ASDF:NU") FieldTagged fieldTagged = new FieldTagged();
    
    /**
     * 
     */
    public ClassTagged()
    {
        blarg = "blarg";
    }

}
