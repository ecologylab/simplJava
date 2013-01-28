package simpl.core;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.EnumerationDescriptor;

/**
 * A SimplTypesScope represents a set of mappings between "tag names" (viewed
 * as format invariant names for types; the name "tag" is a legacy holdover from when S.im.pl
 * was just an XML de/serialization layer.) and class descriptions. 
 * 
 * SimplTypeScopes can inherit tag-description mappings from other simpl types scopes, 
 * can have descriptions mocked for testing purposes, and handle inheritance, and of course can handle
 * the mapping between tag name and description.  
 *
 */
public interface ISimplTypesScope {
	/**
	 * Sets the name of this SimplTypesScope
	 * @param name
	 */
	void setName(String name);
	
	/**
	 * Gets the unique name for this SimplTypesScope
	 * @return
	 */
	String getName();
	
	/**
	 * Adds a translation for a given class to this Simpl Types Scope
	 * @param classObj
	 */
	void addTranslation(Class<?> classObj);
	
	/**
	 * Removes a translation for a given class from this Simpl Types Scope
	 * @param classObj
	 */
	void removeTranslation(Class<?> classObj);
	
	
	void overrideWithMockTranslation(Class<?> classObj);
	
	/**
	 * Determines if an enumeration or a class description exists for a given tag name
	 * @param tag The tag name to look up
	 * @return True if such a description exists. 
	 */
    boolean containsDescriptorForTag(String tag);
    
    /**
     * Returns a class descriptor for a tag name, or null if no such descriptor exists.
     * @param tag
     * @return
     */
	ClassDescriptor<?> getClassDescriptorByTag(String tag);
	
	/**
	 * Returns an enumeration descriptor for a tag name, or null if no such descriptor exists.
	 * @param tag
	 * @return
	 */
	EnumerationDescriptor getEnumerationDescriptorByTag(String tag);
	
	/**
	 * Inherit all of the translations from the parent "sts" into this Simpl Types Scope.
	 * @param sts
	 */
	void inheritFrom(SimplTypesScope sts);
}
