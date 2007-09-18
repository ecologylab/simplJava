/**
 * 
 */
package ecologylab.xml.library.jnlp.applet;

import ecologylab.xml.ElementState;

/**
 * @author Zach
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
