package simpl.descriptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import simpl.interpretation.UpdateCallbackMap;

/**
 * This class holds a collection of callbacks to update class descriptor references.
 * Allows us to avoid recursing beyond a single level...
 * Makes things a bit more verbose at spots, but also cleans up a lot of danging logic from the past. 
 *
 */
class ClassDescriptorCallbackMap extends UpdateCallbackMap<Class<?>, UpdateClassDescriptorCallback, ClassDescriptor>
{		
}