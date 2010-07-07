/*
 * Created on Nov 14, 2006
 */
package ecologylab.xml.library.geom;

import ecologylab.xml.ElementState;
import ecologylab.xml.simpl_inherit;

/**
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public @simpl_inherit class TestFloats extends ElementState implements Cloneable
{
    protected @simpl_scalar float y = 0;

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
