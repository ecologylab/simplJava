// Copyright 1996 by Creating Media.  All rights reserved.
package cm.generic;

import java.lang.Object;

/**
 * Object wrapper for int primitive values.
 * Useful for storing in HashMaps, and anywhere else that a reference type is needed.
 */
public class IntSlot
extends Object
{
   public int 	value;

   public IntSlot(int b)
     {
	super();
	value = b;
     }
    /**
     * Returns a hashcode for this Integer.
     *
     * @return  a hash code value for this object. 
     * @since   JDK1.0
     */
    public int hashCode() {
	return value;
    }
   public String toString()
   {
      return "IntSlot[" + value + "]";
   }
}
