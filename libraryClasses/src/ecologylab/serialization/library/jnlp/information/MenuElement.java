/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;

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
