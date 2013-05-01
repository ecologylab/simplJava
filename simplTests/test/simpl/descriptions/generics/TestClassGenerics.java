package simpl.descriptions.generics;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ecologylab.generic.Generic;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
import simpl.descriptions.FieldDescriptor;
import simpl.descriptions.GenericDescriptions;
import simpl.descriptions.GenericTypeVar;
import simpl.descriptions.generics.testclasses.*;

public class TestClassGenerics {

	static Class[] classes = {
		SearchResult.class,
		Search.class,
		Media.class,
		MediaSearchResult.class,
		MediaSearch.class,
		Image.class,
		FlickrSearchResult.class,
		ImageSearch.class,
	};
	
	public static ISimplTypesScope getTestingTypesScope()
	{
		return SimplTypesScopeFactory.name("test-simpl-generics").translations(classes).create();
	}

	
	@Test
	public void testGenericConstraintIsConcreteClass()
	{
		// case 1: constraint is a concrete class
		List<GenericTypeVar> vars = GenericDescriptions.getClassTypeVariables(Search.class);
		Assert.assertEquals("Expecting only one type variable", vars.size(), 1);
		
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertSame(var1.getConstraintClassDescriptor(), ClassDescriptors.getClassDescriptor(SearchResult.class));
	}
	
	@Test
	public void testClassDescriptorContainsExpectedGenerics()
	{
		List<GenericTypeVar> vars = ClassDescriptors.getClassDescriptor(Search.class).getGenericTypeVariables();
		
		Assert.assertEquals("Expecting only one type variable", vars.size(), 1);
		
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertSame(var1.getConstraintClassDescriptor(), ClassDescriptors.getClassDescriptor(SearchResult.class));

		
	}
	
	@Test
	public void testGenericConstraintParametizedWithWildcard()
	{
		List<GenericTypeVar> vars = GenericDescriptions.getClassTypeVariables(MediaSearch.class);
		Assert.assertEquals("Expecting two generic type vars", vars.size(), 2);
		GenericTypeVar var1 = vars.get(0);
		GenericTypeVar var2 = vars.get(1);
		Assert.assertEquals(var2.getName(), "T");
		Assert.assertSame(var2.getConstraintClassDescriptor(), ClassDescriptors.getClassDescriptor(MediaSearchResult.class));
		ArrayList<GenericTypeVar> var2args = var2.getConstraintGenericTypeVarArgs();
		Assert.assertEquals(var2args.size(), 1);
		GenericTypeVar var2arg1 = var2args.get(0);
		Assert.assertEquals(var2arg1.getName(), "?");
		Assert.assertTrue("This should be a wildcard", var2arg1.isWildcard());
		Assert.assertEquals(var2arg1.getConstraintGenericTypeVar(), var1);
	}
	
	@Test
	public void testGenericConstraintParametizedWithoutWildcard()
	{
		List<GenericTypeVar> vars = GenericDescriptions.getClassTypeVariables(ImageSearch.class);
		Assert.assertEquals(vars.size(), 3);
		GenericTypeVar var1 = vars.get(0);
		GenericTypeVar var2 = vars.get(1);
		GenericTypeVar var3 = vars.get(2);
		Assert.assertEquals(var2.getName(), "X");
		Assert.assertSame(var2.getConstraintGenericTypeVar(), var1);
		Assert.assertEquals(var3.getName(), "T");
		Assert.assertSame(var3.getConstraintClassDescriptor(), ClassDescriptors.getClassDescriptor(MediaSearchResult.class));
		ArrayList<GenericTypeVar> var3args = var3.getConstraintGenericTypeVarArgs();
		Assert.assertEquals(var3args.size(), 1);
		GenericTypeVar var3arg1 = var3args.get(0);
		Assert.assertEquals(var3arg1.getName(), "X");
		Assert.assertEquals(var3arg1.getReferredGenericTypeVar(), var2);
		
		
	}
	
	

	/*
	@Test
	public void testSuperClassGenerics()
	{
		// public class FlickrSearchResult extends MediaSearchResult<Image>
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(FlickrSearchResult.class);
		ArrayList<GenericTypeVar> scvars = c.getSuperClassGenericTypeVars();
		Assert.assertEquals(scvars.size(), 1);
		GenericTypeVar var1 = scvars.get(0);
		Assert.assertSame(var1.getClassDescriptor(), ClassDescriptors.getClassDescriptor(Image.class));
		
		// public class MediaSearch<M extends Media, T extends MediaSearchResult<? extends M>> extends Search<T>
		c = ClassDescriptors.getClassDescriptor(MediaSearch.class);
		ArrayList<GenericTypeVar> vars = c.getGenericTypeVars();
		scvars = c.getSuperClassGenericTypeVars();
		Assert.assertEquals(scvars.size(), 1);
		var1 = scvars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertSame(var1.getReferredGenericTypeVar(), vars.get(1));
		
		// public class ImageSearch<I extends Image, X extends I, T extends MediaSearchResult<X>> extends MediaSearch<X, T>
		c = ClassDescriptors.getClassDescriptor(ImageSearch.class);
		vars = c.getGenericTypeVars();
		scvars = c.getSuperClassGenericTypeVars();
		Assert.assertEquals(scvars.size(), 2);
		var1 = scvars.get(0);
		Assert.assertEquals(var1.getName(), "X");
		Assert.assertSame(var1.getReferredGenericTypeVar(), vars.get(1));
		GenericTypeVar var2 = scvars.get(1);
		Assert.assertEquals(var2.getName(), "T");
		Assert.assertSame(var2.getReferredGenericTypeVar(), vars.get(2));
	}
	*/
	
	

}
