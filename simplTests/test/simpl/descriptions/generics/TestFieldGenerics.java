package simpl.descriptions.generics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.GenericDescriptions;
import simpl.descriptions.GenericTypeVar;
import simpl.descriptions.generics.testclasses.Media;
import simpl.descriptions.generics.testclasses.MediaSearch;
import simpl.descriptions.generics.testclasses.MediaSearchResult;
import simpl.descriptions.generics.testclasses.Search;

public class TestFieldGenerics {

	// I really don't know how to name these; these should be renamed. 
	// TODO: RENAME. 
	
	@Test
	public void testGenericField0()
	{
		// Search.results
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(Search.class);
		List<GenericTypeVar> cvars = GenericDescriptions.getClassTypeVariables(Search.class);
		FieldDescriptor f = c.fields().by("name").get("results");
		List<GenericTypeVar> vars = f.getGenericTypeVariables();
		
		assertFalse("There should be at least one type variable!", vars.isEmpty());
		assertEquals(1,vars.size());
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		GenericTypeVar vReferred = var1.getReferredGenericTypeVar();
		GenericTypeVar classVar = cvars.get(0);
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(0));
	}
	
	
	@Test
	public void testGenericField1()
	{
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(MediaSearchResult.class);

		// MediaSearch.results
		c = ClassDescriptors.getClassDescriptor(MediaSearch.class);
		MediaSearch ms = new MediaSearch<>();
		assertEquals((Integer)2, (Integer)c.fields().size());
		
		List<GenericTypeVar> cvars = c.getGenericTypeVariables();
		FieldDescriptor f = c.fields().by("name").get("results");
		assertNotNull("Field should not be null!", f);
		
		List<GenericTypeVar> vars = f.getGenericTypeVariables();
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(1));
	}
	
	@Test
	public void testGenericField2()
	{
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(MediaSearchResult.class);

		// MediaSearch.firstResult
		FieldDescriptor f = c.fields().by("name").get("firstResult");
		List<GenericTypeVar> vars = f.getGenericTypeVariables();
		GenericTypeVar var1 = vars.get(0);
		Assert.assertSame(var1.getClassDescriptor(), ClassDescriptors.getClassDescriptor(Media.class));
	}
	
	@Test
	public void testGenericField3()
	{
		// MediaSearchResult.media
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(MediaSearchResult.class);
		List<GenericTypeVar> cvars = c.getGenericTypeVariables();
		FieldDescriptor f = c.fields().by("name").get("media");
		List<GenericTypeVar> vars = f.getGenericTypeVariables();
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "M");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(0));
	}
	
	
	@Test
	public void testGenericField4()
	{
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(MediaSearchResult.class);
		
		List<GenericTypeVar> cvars = c.getGenericTypeVariables();

		
		// MeidaSearchResult.ms
		FieldDescriptor f = c.fields().by("name").get("ms");
		List<GenericTypeVar> vars = f.getGenericTypeVariables();
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "M");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(0));
		GenericTypeVar var2 = vars.get(1);
		Assert.assertSame(var2.getClassDescriptor(), ClassDescriptors.getClassDescriptor(MediaSearchResult.class));
		ArrayList<GenericTypeVar> var2args = var2.getGenericTypeVarArgs();
		Assert.assertEquals(var2args.size(), 1);
		GenericTypeVar var2arg1 = var2args.get(0); 
		Assert.assertEquals(var2arg1.getName(), "?");
		Assert.assertEquals(var2arg1.getConstraintGenericTypeVar(), cvars.get(0));
	}	
}
