package ecologylab.fundamental;

import java.util.ArrayList;

import org.junit.Test;

import simpl.annotations.dbal.FieldUsage;
import simpl.annotations.dbal.simpl_collection;
import simpl.annotations.dbal.simpl_scalar;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.FieldDescriptor;
import simpl.tools.XMLTools;
import static org.junit.Assert.*;


public class DBALTests {
	//TODO: validation on these is super sparse... just to prove out where
	// exceptions happen. Will fix eventually. 

	@Test
	public void simpl_Collection_of_enums_transmitsNameIntoFD()
	{
		final class myCollectionClass
		{
			@simpl_collection("collection_test")
			private ArrayList<FieldUsage> collectionTest;	
		}
		
		String tagName = XMLTools.getXmlTagName(myCollectionClass.class.getDeclaredFields()[0]);
		assertNotNull(tagName);
		System.out.println(tagName);
		
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(myCollectionClass.class);
		FieldDescriptor fd = (FieldDescriptor)cd.allFieldDescriptors().get(0);
		assertNotNull(fd.getTagName());
	}
	
	@Test
	public void simpl_collection_of_composites_transmistNameIntoFD()
	{
		final class someCompositeClass
		{
			@simpl_scalar
			private Integer myInteger;
		}
		
		final class myCollectionClass
		{
			@simpl_collection("excluded_usage")
			private ArrayList<someCompositeClass> excludedUsages;	
		}
		
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(myCollectionClass.class);
		FieldDescriptor fd = (FieldDescriptor)cd.allFieldDescriptors().get(0);
		assertNotNull(fd.getTagName());
	}
	
	@Test
	public void simpl_Collection_of_scalars_transmitsNameIntoFD()
	{
		final class myCollectionClass
		{
			@simpl_collection("excluded_usage")
			private ArrayList<Integer> excludedUsages;	
		}
		
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(myCollectionClass.class);
		FieldDescriptor fd = (FieldDescriptor)cd.allFieldDescriptors().get(0);
		assertNotNull(fd.getTagName());
	}
	
	@Test
	public void simpl_collection_of_collection_of_scalars_works()
	{
		final class myCollectionClass
		{
			@simpl_collection("excluded_usage")
			private ArrayList<ArrayList<Integer>> excludedUsages;	
		}
		
		ClassDescriptor cd = ClassDescriptor.getClassDescriptor(myCollectionClass.class);
		FieldDescriptor fd = (FieldDescriptor)cd.allFieldDescriptors().get(0);
		assertNotNull(fd.getTagName());	
	}
	
	
}
