/**
 * 
 */
package ecologylab.xml.library.jnlp.application;

import ecologylab.xml.ElementState;

/**
 * @author Zach
 *
 */
public class Argument extends ElementState
{
    @xml_leaf String arg;

    /**
     * 
     */
    public Argument()
    {
        super();
    }

    public Argument(String arg)
    {
        super();
        this.arg = arg;
    }
    
    @Override public String getTextNodeString()
    {
        return arg;
    }

    @Override public void setTextNodeString(String str)
    {
        this.arg = str;
    }
}
