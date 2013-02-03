package simpl.types.newStuff;

import simpl.core.ISimplStringMarshaller;

/**
 * An abstract class to represent a scalar type mapping in simpl.
 * Handles marshalling of Objects of the scalar type to the common simpl string representation.
 * (This common representation gets escaped in a format for serialization)
 * (The object returned from the common representation will get put into a given deserialized object
 * by another class.) 
 * @author tom
 *
 */
public abstract class ScalarType implements ISimplStringMarshaller{

	
	
	

}
