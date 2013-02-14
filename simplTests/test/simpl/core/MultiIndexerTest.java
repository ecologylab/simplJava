package simpl.core;

import static org.junit.Assert.*;

import org.junit.Test;

import simpl.core.indexers.MultiIndexer;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.indexers.ClassDescriptorIndexer;


public class MultiIndexerTest {

	@Test
	public void testInsert() {

		// A class that we'll obtain a class descriptor for, and then index.
		final class myClass{
		}
		// Get the class descriptor. 
		ClassDescriptor<?> relevant = ClassDescriptors.getClassDescriptor(myClass.class);

		// Create a multi indexer
		MultiIndexer<ClassDescriptor<?>> sut = new ClassDescriptorIndexer();
		
		sut.Insert(relevant);
	
		assertTrue("Our multi indexer should have a single item in it!", 1==sut.size());
		
		// Attempt to fetch a value from the multi-indexer
		ClassDescriptor<?> result = sut.by("tagname").get(relevant.getTagName());
		
		assertEquals("We expect to retrieve the class descriptor... Retrieval failed!", relevant, result);
	}
	
	@Test
	public void testRemove()
	{
		final class myClass{}
		
		MultiIndexer<ClassDescriptor<?>> sut = new ClassDescriptorIndexer();
		
		ClassDescriptor<?> relevant = ClassDescriptors.getClassDescriptor(myClass.class);
		
		sut.Insert(relevant);
		
		assertTrue(1==sut.size());
		
		sut.Remove(relevant);
		
		assertTrue(0==sut.size());
		
		//assertNull(sut.by("simplname").get(relevant.getClassSimpleName()));
	}

}
