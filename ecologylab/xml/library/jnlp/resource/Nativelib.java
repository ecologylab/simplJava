/**
 * 
 */
package ecologylab.xml.library.jnlp.resource;

import ecologylab.xml.xml_inherit;

/**
 * A nativelib element specifies a JAR file that contains native libraries. For example:
 * 
 * <nativelib href="lib/windows/corelib.jar"/>
 * 
 * The JNLP client must ensure that each file entry in the root directory of the JAR file (i.e., /) can be loaded into
 * the running process using the System.loadLibrary method. Each entry must contain a platform-dependent shared library
 * with the correct naming convention, e.g., *.dll on Windows or lib*.so on Solaris/Linux. The application is
 * responsible for doing the actual call to System.loadLibrary.
 * 
 * Native libraries would typically be included in a resources element that is geared toward a particular operating
 * system and architecture. For example:
 * 
 * <resources os="SunOS" arch="sparc"> <nativelib href="lib/solaris/corelibs.jar"/> </resource>
 * 
 * By default, jar and nativelib resources will be downloaded eagerly, i.e., they are downloaded and available locally
 * to the JVM running the application before the application is launched. The jar and nativelib elements also allow a
 * resource to be specified as lazy. This means the resource does not have to be downloaded onto the client system
 * before the application is launched.
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public @xml_inherit class Nativelib extends Jar
{

    /**
     * 
     */
    public Nativelib()
    {
        super();
    }

}
