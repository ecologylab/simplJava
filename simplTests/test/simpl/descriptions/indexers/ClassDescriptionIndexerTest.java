package simpl.descriptions.indexers;

import static org.junit.Assert.*;

import java.util.Map.Entry;

import org.junit.Test;

import simpl.descriptions.ClassDescriptor;

public class ClassDescriptionIndexerTest {

	@Test
	public void showOffNewIndexingSyntax() { // soon to be in the SimplTypesScope
		ClassDescriptorIndexer classDescriptors = new ClassDescriptorIndexer();
		classDescriptors.by.TagName.get("class_descriptor");
		classDescriptors.by.TagName.contains("class_descriptor");
		classDescriptors.Insert(ClassDescriptor.getClassDescriptor(ClassDescriptor.class)); 
		classDescriptors.Remove(null);
		
		
		for(ClassDescriptor<?> cd : classDescriptors)
		{
			
		}
		
		classDescriptors.mergeIn(null);
		
	}
	
	
	

}
