/**
 * 
 */
package ecologylab.xml.library.jnlp.applet;

import ecologylab.xml.ElementState;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 *
 */
public class Param extends ElementState
{
    @xml_attribute private String name;
    @xml_attribute private String value;

    /**
     * 
     */
    public Param()
    {
        super();
    }

}
