/**
 * 
 */
package ecologylab.xml.library.jnlp;

import java.util.ArrayList;

import ecologylab.xml.xml_inherit;
import ecologylab.xml.ElementState.xml_collection;
import ecologylab.xml.library.jnlp.applet.AppletDesc;
import ecologylab.xml.library.jnlp.application.ApplicationDesc;
import ecologylab.xml.library.jnlp.information.InformationElement;
import ecologylab.xml.library.jnlp.resource.ResourceElementArray;
import ecologylab.xml.types.element.ArrayListState;

/**
 * Parses JNLP files for Java web launch.
 * 
 * Field comments from:
 * 
 * http://java.sun.com/j2se/1.5.0/docs/guide/javaws/developersguide/syntax.html
 * 
 * @author Zach
 * 
 */
public @xml_inherit class JnlpState extends ArrayListState<InformationElement>
{
    /**
     * This attribute must be 1.0 or higher to work with this release. The default value is "1.0+". Thus, it can
     * typically be omited. Note that this version supports both spec version 1.0 and version 1.5, whereas previous
     * versions support only 1.0. A jnlp file specifying spec="1.5+" will work with this version, but not previous
     * versions of Java Web Start.
     */
    @xml_attribute private String                                  spec;

    /** All relative URLs specified in href attributes in the JNLP file are using this URL as a base. */
    @xml_attribute private String                                  codebase;

    /** This is a URL pointing to the location of the JNLP file itself. */
    @xml_attribute private String                                  href;

     @xml_collection("information") private ArrayList<InformationElement> informations = new
     ArrayList<InformationElement>();
//    @xml_nested private InformationElement                         information;

    @xml_nested private ArrayListState<AllPermissionsElement>      security;

    @xml_nested private ResourceElementArray                       resources;

    @xml_collection("application-desc") ArrayList<ApplicationDesc> applicationDesc = new ArrayList<ApplicationDesc>();

    @xml_collection("applet-desc") ArrayList<AppletDesc>           appletDesc      = new ArrayList<AppletDesc>();

    /**
     * No-arg constructor for XML translation.
     */
    public JnlpState()
    {
        super();
    }
}
