// Copyright 1996 by Creating Media.  All rights reserved.
// stuck here from blocks as a convenience for minimal packaging 7/6/96
package cm.generic;

import java.lang.Object;

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

}
