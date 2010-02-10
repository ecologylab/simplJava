/**
 * 
 */
package ecologylab.xml.library.jnlp.resource;

/**
 * The property element defines a system property that will be available through the System.getProperty and
 * System.setProperties methods. It has two required attributes: name and value. For example:
 * 
 * <property name="key" value="overwritten"/>
 * 
 * Properties set in the jnlp file will normally be set by Java Web Start after the VM is started but before the
 * application is invoked. Some properties are considered "secure" properties and can be passed as -Dkey=value arguments
 * on the java invocation command line.
 * 
 * The following properties are considered "secure" and will be passed to the VM in this way:
 * 
 * sun.java2d.noddraw javaws.cfg.jauthenticator swing.useSystemFontSettings swing.metalTheme http.agent http.keepAlive
 * 
 * For an untrusted application, system properties set in the JNLP file will only be set by Java Web Start if they are
 * considered secure, or if the property name begins with "jnlp." or "javaws.".
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public class Property extends ResourceElement
{
    @xml_attribute private String name;

    @xml_attribute private String value;

    /**
     * 
     */
    public Property()
    {
        // TODO Auto-generated constructor stub
    }

}
