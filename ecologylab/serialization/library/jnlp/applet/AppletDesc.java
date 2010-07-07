/**
 * 
 */
package ecologylab.serialization.library.jnlp.applet;

import java.util.ArrayList;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.simpl_inherit;
import ecologylab.serialization.ElementState.xml_tag;
/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public @simpl_inherit @xml_tag("applet-desc") class AppletDesc extends ElementState
{
    @simpl_scalar private String documentBase;
    @simpl_scalar private String name;
    @simpl_scalar @xml_tag("main-class") private String mainClass;
    @simpl_scalar private int width;
    @simpl_scalar private int height;
    
    @simpl_nowrap
    @simpl_collection("Param")
    ArrayList<Param> params;

    /**
     * 
     */
    public AppletDesc()
    {
        super();
    }

}
