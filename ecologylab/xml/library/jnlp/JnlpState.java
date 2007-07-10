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
    @xml_attribute private String                                        spec;

    /** All relative URLs specified in href attributes in the JNLP file are using this URL as a base. */
    @xml_attribute private String                                        codebase;

    /** This is a URL pointing to the location of the JNLP file itself. */
    @xml_attribute private String                                        href;

    @xml_collection("information") private ArrayList<InformationElement> informations    = new ArrayList<InformationElement>();

    // @xml_nested private InformationElement information;

    @xml_nested private ArrayListState<AllPermissionsElement>            security;

    @xml_nested private ResourceElementArray                             resources;

    @xml_collection("application-desc") ArrayList<ApplicationDesc>       applicationDesc = new ArrayList<ApplicationDesc>();

    @xml_collection("applet-desc") ArrayList<AppletDesc>                 appletDesc      = new ArrayList<AppletDesc>();

    /**
     * No-arg constructor for XML translation.
     */
    public JnlpState()
    {
        super();
    }

    /**
     * @return the applet description, if any (returns null if there is not one)
     */
    public AppletDesc getAppletDesc()
    {
        return (appletDesc == null || appletDesc.size() == 0 ? null : appletDesc.get(0));
    }

    /**
     * Sets the applet description and clears the application description (since there can only be one or the other) and
     * any previous applet descriptions.
     * 
     * @param appletDesc
     *            the appletDesc to set
     */
    public void setAppletDesc(AppletDesc appletDesc)
    {
        if (this.appletDesc == null)
        {
            this.appletDesc = new ArrayList<AppletDesc>();
        }

        this.appletDesc.clear();
        this.appletDesc.add(appletDesc);
    }

    /**
     * @return the application description, if any (returns null if there is not one)
     */
    public ApplicationDesc getApplicationDesc()
    {
        return (applicationDesc == null || applicationDesc.size() == 0 ? null : applicationDesc.get(0));
    }

    /**
     * Sets the application description and clears the applet description (since there can only be one or the other) and
     * any previous application descriptions.
     * 
     * @param applicationDesc
     *            the applicationDesc to set
     */
    public void setApplicationDesc(ApplicationDesc applicationDesc)
    {
        if (this.applicationDesc == null)
        {
            this.applicationDesc = new ArrayList<ApplicationDesc>();
        }

        this.applicationDesc.clear();
        this.applicationDesc.add(applicationDesc);
    }

    /**
     * @return the href
     */
    public String getHref()
    {
        return href;
    }

    /**
     * @param href
     *            the href to set
     */
    public void setHref(String href)
    {
        this.href = href;
    }
}
