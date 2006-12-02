/*
 * Created on Nov 14, 2006
 */
package ecologylab.xml.geom;

import java.awt.geom.Point2D;

import ecologylab.xml.ElementState;
import ecologylab.xml.xml_inherit;

/**
 * @author Zach Toups (toupsz@gmail.com)
 */
public @xml_inherit class TestFloats extends ElementState implements Cloneable
{
    protected @xml_attribute float y = 0;

    /**
     * 
     */
    public TestFloats()
    {
        super();
    }

    public TestFloats(float y)
    {
        this.y = y;
    }

}
