/**
 * 
 */
package ecologylab.xml.library.jnlp.application;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author Zach
 *
 */
public @xml_inherit @xml_tag("application-desc") class ApplicationDesc extends ArrayListState<Argument>
{
    @xml_attribute @xml_tag("main-class") private String mainClass;
    

    /**
     * 
     */
    public ApplicationDesc()
    {
        super();
    }

}
