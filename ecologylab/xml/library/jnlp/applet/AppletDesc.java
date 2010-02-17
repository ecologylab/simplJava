/**
 * 
 */
package ecologylab.xml.library.jnlp.applet;

import java.util.ArrayList;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_tag;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public @xml_inherit @xml_tag("applet-desc") class AppletDesc extends ElementState
{
    @xml_attribute private String documentBase;
    @xml_attribute private String name;
    @xml_attribute @xml_tag("main-class") private String mainClass;
    @xml_attribute private int width;
    @xml_attribute private int height;
    
    @xml_nowrap
    @xml_collection("Param")
    ArrayList<Param> params;

    /**
     * 
     */
    public AppletDesc()
    {
        super();
    }

}
