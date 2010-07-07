/**
 * 
 */
package ecologylab.serialization.library.jnlp.resource;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;

/**
 * The resources element is used to specify all the resources, such as Java class files, native libraries, and system properties, that are part of the application.  A resource definition can be restricted to a specific operating system, architecture, or locale using the os, arch, and locale attributes.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public @simpl_inherit class ResourceElementArray extends ElementState
{
    @simpl_scalar private String os;
    @simpl_scalar private String arch;
    @simpl_scalar private String locale;
    
    @simpl_collection("ResourceElement") ArrayList<ResourceElement> resourceElements;

    /**
     * 
     */
    public ResourceElementArray()
    {
        super();
    }

}
