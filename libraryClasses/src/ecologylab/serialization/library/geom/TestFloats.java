/*
 * Created on Nov 14, 2006
 */
package ecologylab.serialization.library.geom;

import simpl.annotations.dbal.simpl_inherit;
import simpl.annotations.dbal.simpl_scalar;
import simpl.core.ElementState;

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
