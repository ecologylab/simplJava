package simpl;

import simpl.descriptions.ClassDescriptor;

/**
 * A Static class with simpl-level functionality, such as simpl.equals, simpl.hashcode, etc.
 *
 */
public class Simpl {
	
	/**
	 * Takes two objects and compares them for equality. Evaluates scalar types, lists, composites, maps, and even cycles! 
	 * Two objects with identical values inside will be considered equal. 
	 * @param left the object on the lhs of the operation
	 * @param right the object on the rhs of the operation
	 * @return True if and only if left == right. False if their values differ, their types differ, or if one is null and the other is not.
	 */
	public static boolean equals(Object left, Object right)
	{
		return false; // mocked out, stuff is gonna happen here. 
	}
	
	/**
	 * Anything that can have an equals() should obviously have some kind of hash code! 
	 * Two items that are simpl.equals should have the same simpl.hashcode.
	 * @param toHashCode
	 * @return
	 */
	public static int hashcode(Object toHashCode)
	{
		return toHashCode.hashCode();
	}
	
	/**
	 * S.IM.PL imposes a certain set of restrictions and rules behind different aspects of simpl-serialized classes
	 * and instances of classes.
	 * 
	 * Additionally, some invariants / constraints can be a bit harder to see or predict ahead of time. (For example: It can be
	 * very easy to miss reserved names that are reserved in other languages... consider: lambda is a reserved term in python;
	 *  a java developer probably would never expect that!) 
	 *  
	 *  This method makes a call to the SimplClassValidator and validates the given class for all of these invariants. 
	 *  A version in SimplClassValidator will return an assertion with the source of the error; 
	 *  this will simply return false if the class is invalid.
	 *  
	 */
	public static boolean validate(Object instanceOfClass) 
	{
		return SimplClassValidator.validate(instanceOfClass);
	}
}
