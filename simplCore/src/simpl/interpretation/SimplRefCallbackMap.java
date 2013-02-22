package simpl.interpretation;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.UpdateClassDescriptorCallback;

/**
 * A collection of callbacks used to handle updating of values in object resolution.
 * An UpdateSimplRefCallback is called to set the value of a given object reference to some other value.
 * Sorted by the corresponding ref / ID
 * @author tom
 *
 */
public class SimplRefCallbackMap extends UpdateCallbackMap<String, UpdateSimplRefCallback, Object>{
	
	
}