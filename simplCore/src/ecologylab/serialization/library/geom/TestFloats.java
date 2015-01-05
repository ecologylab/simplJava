/*
 * Created on Nov 14, 2006
 */
package ecologylab.serialization.library.geom;

import ecologylab.serialization.ElementState;
import ecologylab.serialization.annotations.simpl_inherit;
import ecologylab.serialization.annotations.simpl_scalar;

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
