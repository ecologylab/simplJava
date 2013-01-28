package simpl.core;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.EnumerationDescriptor;
import simpl.descriptions.indexers.ClassDescriptorIndexer;

public interface ISimplTypesScope {
	void setName(String name);
	String getName();
	
	void addTranslation(Class<?> classObj);
	void removeTranslation(Class<?> classObj);
	void overrideWithMockTranslation(Class<?> classObj);
	
    boolean containsDescriptorForTag(String tag);
	ClassDescriptor<?> getClassDescriptorByTag(String tag);
	EnumerationDescriptor getEnumerationDescriptorByTag(String tag);
	
	void inheritFrom(SimplTypesScope sts);
}
