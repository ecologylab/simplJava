/**
 * 
 */
package ecologylab.xml.library.jnlp.resource;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.types.element.ArrayListState;

/**
 * The resources element is used to specify all the resources, such as Java class files, native libraries, and system properties, that are part of the application.  A resource definition can be restricted to a specific operating system, architecture, or locale using the os, arch, and locale attributes.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public @xml_inherit class ResourceElementArray extends ArrayListState<ResourceElement>
{
    @xml_attribute private String os;
    @xml_attribute private String arch;
    @xml_attribute private String locale;

    /**
     * 
     */
    public ResourceElementArray()
    {
        super();
    }

}
