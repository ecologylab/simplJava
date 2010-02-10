/**
 * 
 */
package ecologylab.xml.library.jnlp.applet;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;
import ecologylab.xml.types.element.ArrayListState;

/**
 * @author Zachary O. Toups (zach@ecologylab.net)
 *
 */
public @xml_inherit @xml_tag("applet-desc") class AppletDesc extends ArrayListState<Param>
{
    @xml_attribute private String documentBase;
    @xml_attribute private String name;
    @xml_attribute @xml_tag("main-class") private String mainClass;
    @xml_attribute private int width;
    @xml_attribute private int height;

    /**
     * 
     */
    public AppletDesc()
    {
        super();
    }

}
