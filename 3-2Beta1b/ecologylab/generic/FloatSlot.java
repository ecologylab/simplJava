package ecologylab.generic;

import java.lang.Object;

/**
 * Reference version of a float type. Re-writable, unlike java.lang.Float.
 */
public class FloatSlot
extends Object
{
   public float 	value;
   public FloatSlot(float b)
     {
	super();
	value = b;
     }
}
