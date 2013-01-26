/**
 * 
 */
package ecologylab.serialization.library.jnlp.information;

import simpl.annotations.dbal.simpl_composite;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;

/**
 * shortcut element: The optional shortcut element can be used to indicate an application's preferences for desktop
 * integration. The shortcut element and it's sub-elements provide hints that the JNLP Client may or may not use. The
 * shortcut element can contain the optional online attribute, and the two optional sub-elements, desktop and menu.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 * 
 */
public class ShortcutElement extends ElementState
{
    @simpl_scalar private boolean   online;

    @simpl_composite private ElementState desktop;

    @simpl_composite private MenuElement  menu;

    /**
     * 
     */
    public ShortcutElement()
    {
        super();
    }
}
