/**
 * 
 */
package ecologylab.xml.library.jnlp.resource;

import ecologylab.xml.xml_inherit;

/**
 * A jar element specifies a JAR file that is part of the application's classpath. For example:
 * 
 * <jar href="myjar.jar"/>
 * 
 * The jar file will be loaded into the JVM using a ClassLoader object. The jar file will typically contain Java classes
 * that contain the code for the particular application, but can also contain other resources, such as icons and
 * configuration files, that are available through the getResource mechanism.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public @xml_inherit class Jar extends HrefBasedResource
{
    /**
     * The download attribute is used to control whether a resource is downloaded eagerly or lazily. For example:
     * 
     * <jar href="sound.jar" download="lazy"/> <nativelib href="native-sound.jar" download="eager"/>
     */
    @xml_attribute protected String download;

    /**
     * 
     */
    public Jar()
    {
        super();
    }
}
