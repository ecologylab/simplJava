/**
 * 
 */
package ecologylab.xml.library.jnlp.information;

import ecologylab.xml.ElementState;

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
    @xml_attribute private boolean   online;

    @xml_nested private ElementState desktop;

    @xml_nested private MenuElement  menu;

    /**
     * 
     */
    public ShortcutElement()
    {
        super();
    }
}
