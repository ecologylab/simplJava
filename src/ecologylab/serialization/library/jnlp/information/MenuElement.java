/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_scalar;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class MenuElement extends ElementState
{
    @simpl_scalar private String submenu;

    /**
     * 
     */
    public MenuElement()
    {
        super();
    }
}
