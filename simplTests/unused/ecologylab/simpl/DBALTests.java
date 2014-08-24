package ecologylab.simpl;

import java.util.ArrayList;

import org.junit.Test;
import static org.junit.Assert.*;

import ecologylab.serialization.ClassDescriptor;
import ecologylab.serialization.FieldDescriptor;
import ecologylab.serialization.XMLTools;
import ecologylab.serialization.annotations.FieldUsage;
import ecologylab.serialization.annotations.simpl_collection;
import ecologylab.serialization.annotations.simpl_scalar;

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
