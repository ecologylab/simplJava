package ecologylab.generic;

import java.lang.Object;

/**
 * Reference version of a boolean type. Re-writable, unlike java.lang.Boolean.
 */
public class BooleanSlot
extends Object
{
   public boolean 	value;
   public BooleanSlot(boolean b)
     {
	super();
	value = b;
     }
}
