package simpl.core;

import simpl.descriptions.indexers.ClassDescriptorIndexer;

public interface ISimplTypesScope {
	void setName(String name);
	String getName();
	
	void addTranslation(Class<?> classObj);
	void removeTranslation(Class<?> classObj);
	void overrideWithMockTranslation(Class<?> classObj);
	
	
	
	void inheritFrom(SimplTypesScope sts);
	
}
