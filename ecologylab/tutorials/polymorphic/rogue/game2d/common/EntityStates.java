/*
 * Created on Apr 21, 2006
 */
package ecologylab.tutorials.polymorphic.rogue.game2d.common;

/**
 * Describes constants that are used for making up an Entity's state. These are
 * used as bit masks to derive a composite state.
 * 
 * @author Zachary O. Toups (toupsz@cs.tamu.edu)
 */
public interface EntityStates
{
    public static final short ONLINE    = 0;

    public static final short IN           = 1;

    public static final short SAFE = 2;

    public static final short UPDATED      = 3;
}
