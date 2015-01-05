/**
 * 
 */
package ecologylab.serialization.library.jnlp.resource;

import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;
import ecologylab.serialization.annotations.simpl_tag;

/**
 * The j2se element specifies what Java 2 SE Runtime Environment (JRE) versions an application is supported on, as well
 * as standard parameters to the Java Virtual Machine. If several JREs are specified, this indicates a prioritized list
 * of the supported JREs, with the most preferred version first. For example:
 * 
 * <j2se version="1.3" initial-heap-size="64m" max-heap-size="128m"/> <j2se version="1.4.2+"
 * href="http://java.sun.com/products/autodl/j2se" java-vm-args="-esa -Xnoclassgc"/>
 * 
 * @author Zachary O. Toups (zach@ecologylab.net)
 * 
 */
public @simpl_inherit @simpl_tag("j2se") class J2se extends HrefBasedResource
{
    /**
     * The version attribute refers, by default, to a platform version (specification version) of the Java 2 platform.
     * Currently defined platform versions are 1.2, 1.3, 1.4 and 1.5. (A platform version will not normally contain a
     * micro version number; e.g., 1.4.2.)
     * 
     * Exact product versions (implementation versions) may also be specified. by including the href attribute. For
     * example, 1.3.1_07, 1.4.2, or 1.5.0-beta2 by Sun Microsystems, Inc. For example,
     * 
     * <j2se version="1.4.2" href="http://java.sun.com/products/autodl/j2se"/
     * 
     * or
     * 
     * <j2se version="1.4.2_04" href="http://java.sun.com/products/autodl/j2se"/>
     * 
     * If a platform version is specified (i.e., no href attribute is provided), Java Web Start will not consider an
     * installed non-FCS (i.e., milestone) JRE as a match. E.g., a request of the form
     * 
     * <j2se version="1.4+"/>
     * 
     * would not consider an installed 1.4.1-ea or 1.4.2-beta JRE as a match for the request. Starting with 1.3.0, a JRE
     * from Sun Microsystems, Inc., is by convention a non-FCS (milestone) JRE if there is a dash (-) in the version
     * string.
     */
    @simpl_scalar private String                          version;

    /**
     * The java-vm-args attribute of the j2se element specifies a preferred set of virtual machine arguments to use when
     * launching java.
     * 
     * <j2se version="1.4+" java-vm-args="-ea -Xincgc"/>
     * 
     * The following java-vm-args are supported by this version:
     * 
     * -client -server -verbose -showversion -esa -enablesystemassertions -dsa -disablesystemassertions -Xmixed -Xint
     * -Xnoclassgc -Xincgc -Xbatch -Xprof -Xdebug -Xrs -XX:+ForceTimeHighResolution -XX:-ForceTimeHighResolution
     * 
     * Plus any argument starting with one of the following:
     * 
     * -ea: -enableassertions: -da: -disableassertions: -verbose: -Xms -Xmx -Xss -XX:NewRatio -XX:NewSize -XX:MaxNewSize
     * -XX:PermSize -XX:MaxPermSize -XX:MaxHeapFreeRatio -XX:MinHeapFreeRatio -XX:UseSerialGC -XX:ThreadStackSize
     * -XX:MaxInlineSize -XX:ReservedCodeCacheSize
     * 
     * 
     */
    @simpl_scalar @simpl_tag("java-vm-args") private String javaVmArgs;

    @simpl_scalar @simpl_tag("initial-heap-size") private String initialHeapSize;
    
    @simpl_scalar @simpl_tag("max-heap-size") private String maxHeapSize;
    
    /**
     * 
     */
    public J2se()
    {
        super();
    }

}
