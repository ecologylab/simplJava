/**
 * 
 */
package ecologylab.tests;

import ecologylab.xml.ElementState;
import ecologylab.xml.ElementState.xml_tag;

/**
 * @author toupsz
 *
 */
public @xml_tag("CLASS_NAME_TAG") class ClassTagged extends ElementState
{
    @xml_attribute @xml_tag("BLARG") String blarg = null;
    @xml_nested @xml_tag("ASDF:NU") FieldTagged fieldTagged = new FieldTagged();
    
    /**
     * 
     */
    public ClassTagged()
    {
        blarg = "blarg";
    }

}
