package simpl.descriptions.generics;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import simpl.core.ISimplTypesScope;
import simpl.core.SimplTypesScopeFactory;
import simpl.descriptions.ClassDescriptor;
import simpl.descriptions.ClassDescriptors;
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

	/*
	@Test
	public void testClassGenerics()
	{
		// case 1: constraint is a concrete class
		ClassDescriptor c = ClassDescriptors.getClassDescriptor(Search.class);
		ArrayList<GenericTypeVar> vars = c.getGenericTypeVars();
		Assert.assertEquals(vars.size(), 1);
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertSame(var1.getConstraintClassDescriptor(), ClassDescriptor.getClassDescriptor(SearchResult.class));
		
		// case 2: constraint is parameterized with wildcard
		c = ClassDescriptor.getClassDescriptor(MediaSearch.class);
		vars = c.getGenericTypeVars();
		Assert.assertEquals(vars.size(), 2);
		var1 = vars.get(0);
		GenericTypeVar var2 = vars.get(1);
		Assert.assertEquals(var2.getName(), "T");
		Assert.assertSame(var2.getConstraintClassDescriptor(), ClassDescriptor.getClassDescriptor(MediaSearchResult.class));
		ArrayList<GenericTypeVar> var2args = var2.getConstraintGenericTypeVarArgs();
		Assert.assertEquals(var2args.size(), 1);
		GenericTypeVar var2arg1 = var2args.get(0);
		Assert.assertEquals(var2arg1.getName(), "?");
		Assert.assertEquals(var2arg1.getConstraintGenericTypeVar(), var1);
		
		// case 2: constraint is parameterized without wildcard
		c = ClassDescriptor.getClassDescriptor(ImageSearch.class);
		vars = c.getGenericTypeVars();
		Assert.assertEquals(vars.size(), 3);
		var1 = vars.get(0);
		var2 = vars.get(1);
		GenericTypeVar var3 = vars.get(2);
		Assert.assertEquals(var2.getName(), "X");
		Assert.assertSame(var2.getConstraintGenericTypeVar(), var1);
		Assert.assertEquals(var3.getName(), "T");
		Assert.assertSame(var3.getConstraintClassDescriptor(), ClassDescriptor.getClassDescriptor(MediaSearchResult.class));
		ArrayList<GenericTypeVar> var3args = var3.getConstraintGenericTypeVarArgs();
		Assert.assertEquals(var3args.size(), 1);
		GenericTypeVar var3arg1 = var3args.get(0);
		Assert.assertEquals(var3arg1.getName(), "X");
		Assert.assertEquals(var3arg1.getReferredGenericTypeVar(), var2);
	}
	
	@Test
	public void testSuperClassGenerics()
	{
		// public class FlickrSearchResult extends MediaSearchResult<Image>
		ClassDescriptor c = ClassDescriptor.getClassDescriptor(FlickrSearchResult.class);
		ArrayList<GenericTypeVar> scvars = c.getSuperClassGenericTypeVars();
		Assert.assertEquals(scvars.size(), 1);
		GenericTypeVar var1 = scvars.get(0);
		Assert.assertSame(var1.getClassDescriptor(), ClassDescriptor.getClassDescriptor(Image.class));
		
		// public class MediaSearch<M extends Media, T extends MediaSearchResult<? extends M>> extends Search<T>
		c = ClassDescriptor.getClassDescriptor(MediaSearch.class);
		ArrayList<GenericTypeVar> vars = c.getGenericTypeVars();
		scvars = c.getSuperClassGenericTypeVars();
		Assert.assertEquals(scvars.size(), 1);
		var1 = scvars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertSame(var1.getReferredGenericTypeVar(), vars.get(1));
		
		// public class ImageSearch<I extends Image, X extends I, T extends MediaSearchResult<X>> extends MediaSearch<X, T>
		c = ClassDescriptor.getClassDescriptor(ImageSearch.class);
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
	
	@Test
	public void testGenericField()
	{
		// Search.results
		ClassDescriptor c = ClassDescriptor.getClassDescriptor(Search.class);
		ArrayList<GenericTypeVar> cvars = c.getGenericTypeVars();
		FieldDescriptor f = c.getFieldDescriptorByFieldName("results");
		ArrayList<GenericTypeVar> vars = f.getGenericTypeVars();
		GenericTypeVar var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(0));
		
		// MediaSearch.results
		c = ClassDescriptor.getClassDescriptor(MediaSearch.class);
		cvars = c.getGenericTypeVars();
		f = c.getFieldDescriptorByFieldName("results");
		vars = f.getGenericTypeVars();
		var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "T");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(1));
		
		// MediaSearch.firstResult
		f = c.getFieldDescriptorByFieldName("firstResult");
		vars = f.getGenericTypeVars();
		var1 = vars.get(0);
		Assert.assertSame(var1.getClassDescriptor(), ClassDescriptor.getClassDescriptor(Media.class));
		
		// MediaSearchResult.media
		c = ClassDescriptor.getClassDescriptor(MediaSearchResult.class);
		cvars = c.getGenericTypeVars();
		f = c.getFieldDescriptorByFieldName("media");
		vars = f.getGenericTypeVars();
		var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "M");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(0));
		
		// MeidaSearchResult.ms
		f = c.getFieldDescriptorByFieldName("ms");
		vars = f.getGenericTypeVars();
		var1 = vars.get(0);
		Assert.assertEquals(var1.getName(), "M");
		Assert.assertEquals(var1.getReferredGenericTypeVar(), cvars.get(0));
		GenericTypeVar var2 = vars.get(1);
		Assert.assertSame(var2.getClassDescriptor(), ClassDescriptor.getClassDescriptor(MediaSearchResult.class));
		ArrayList<GenericTypeVar> var2args = var2.getGenericTypeVarArgs();
		Assert.assertEquals(var2args.size(), 1);
		GenericTypeVar var2arg1 = var2args.get(0); 
		Assert.assertEquals(var2arg1.getName(), "?");
		Assert.assertEquals(var2arg1.getConstraintGenericTypeVar(), cvars.get(0));
	}
	*/ 

}
